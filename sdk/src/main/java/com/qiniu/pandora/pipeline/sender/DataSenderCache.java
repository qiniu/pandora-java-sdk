package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuRuntimeException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSenderCache {

  private volatile boolean isClose;
  private Sender sender;
  private FilenameFilter logFileFilter;
  private String repoName;
  private String logCacheDir;
  private int logRotateIntervalInSec; // in seconds
  private long logRotateFileSize;
  private int logRetryIntervalInSec; // in seconds

  private Map<String, Boolean> retryingFiles;
  private ExecutorService retryService;
  private int currentLogCounter;
  private String currentLogFileName;
  private File currentLogFile;
  private long currentFileLength;
  private Calendar currentLogCalendar;
  private BufferedOutputStream currentLogWriter;
  private long logRotateIntervalInMinutes;
  private long logMaxCacheSize = -1;

  private Thread retryThread = null;

  private DataSenderCache(OptionsBuilder opts) {
    this.repoName = opts.getRepoName();
    this.logCacheDir = opts.getLogCacheDir();
    this.logRotateIntervalInSec = opts.getLogRotateIntervalInSec();
    this.logRetryIntervalInSec = opts.getLogRetryIntervalInSec();
    this.logRotateFileSize = opts.getLogRotateFileSize();
    this.sender = opts.getSender();
    this.logMaxCacheSize = opts.getLogMaxCacheSize();
    this.retryService = Executors.newFixedThreadPool(opts.getLogRetryThreadPoolSize());

    this.logRotateIntervalInMinutes = this.logRotateIntervalInSec / 60;
    this.retryingFiles = new ConcurrentHashMap<String, Boolean>();
    this.currentFileLength = 0;
    this.currentLogCounter = 0;
    final String repo = repoName;
    this.logFileFilter = new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.contains(repo);
      }
    };
    //create the backend task to put logs
    retryThread = new Thread(new Runnable() {
      @Override
      public void run() {
        pushLogs();
      }
    });
    retryThread.start();
  }

  private void pushLogs() {
    while (true) {
      File logCacheDirFile = new File(this.logCacheDir);
      if (logCacheDirFile.exists()) {
        //filter all the log file
        File[] logFiles = logCacheDirFile.listFiles(this.logFileFilter);
        if (logFiles != null) {
          for (File logFile : logFiles) {
            if (logFile.getName().endsWith(".log.tmp") && logFile.length() > 0) {
              //check file last modified
              Date lastModifiedDate = new Date(logFile.lastModified());
              //add log rotate time
              Calendar rotateDate = Calendar.getInstance();
              rotateDate.setTime(lastModifiedDate);
              if (logFiles.length == 1 || (
                  rotateDate.getTimeInMillis() + this.logRotateIntervalInSec * 1000 < System
                      .currentTimeMillis())) {
                //change it to log file with suffix .log
                String logFilePath = logFile.getAbsolutePath();
                File dstLogFile = new File(logFilePath.substring(0, logFilePath.length() - 4));
                synchronized (DataSenderCache.this) {
                  if (logFile.exists()) {
                    logFile.renameTo(dstLogFile);
                    this.currentLogWriter = null;
                  }
                }
              }
            }
          }
        }

        //rescan the log files and upload
        File[] readyLogFiles = logCacheDirFile.listFiles(this.logFileFilter);
        if (readyLogFiles != null) {
          for (final File logFile : readyLogFiles) {
            if (logFile.getName().endsWith(".log")) {
              final String logFileAbsPath = logFile.getAbsolutePath();
              //check whether the last upload finishes
              if (this.retryingFiles.containsKey(logFileAbsPath)) {
                //wait for next scheduled time
                continue;
              }
              this.retryingFiles.put(logFileAbsPath, true);
              //try to upload the data
              retryService.execute(new Runnable() {
                @Override
                public void run() {
                  try {
                    sender.sendFromFile(logFile.getAbsolutePath());
                    //delete the file when sent success
                    logFile.delete();
                  } catch (IOException e) {
                  }
                  //whether success or not, delete key
                  retryingFiles.remove(logFileAbsPath);
                }
              });
            }
          }
        }
      }
      //wait for the next run
      try {
        TimeUnit.SECONDS.sleep(this.logRetryIntervalInSec);
      } catch (InterruptedException e) {
      }

      if (isClose) {
        retryService.shutdown();
        File[] logFiles = logCacheDirFile.listFiles(this.logFileFilter);
        try {
          if (this.currentLogWriter != null) {
            this.currentLogWriter.flush();
            this.currentLogWriter.close();
          }
        } catch (Exception ex) {

        }
        if (logFiles != null && logFiles.length > 0) {
          for (File file : logFiles) {
            if (file.getAbsolutePath().endsWith(".log.tmp")) {
              String filePath = file.getAbsolutePath();
              file.renameTo(new File(filePath.substring(0, filePath.length() - 4)));
            }
          }
        }
        return;
      }
    }
  }

  public static DataSenderCache getInstance(OptionsBuilder opts) throws QiniuRuntimeException {
    synchronized (DataSenderCache.class) {
      makeLogCacheDir(opts.getLogCacheDir());

      return new DataSenderCache(opts);
    }
  }

  public static void makeLogCacheDir(String logCacheDir) throws QiniuRuntimeException {
    File cacheDir = new File(logCacheDir);

    if (!cacheDir.exists()) {
      cacheDir.mkdirs();
    }

    if (!cacheDir.exists() || !cacheDir.isDirectory() || !cacheDir.canWrite()) {
      throw new QiniuRuntimeException(
          logCacheDir + " must exists and be a writable directory");
    }
  }

  public synchronized void write(byte[] logBuffer) throws QiniuRuntimeException {
    if (logMaxCacheSize > 0) {
      File logCacheDirFile = new File(this.logCacheDir);
      File[] logFiles = logCacheDirFile.listFiles(this.logFileFilter);
      if (logFiles != null && logFiles.length > 0) {
        long total = logBuffer.length;
        for (File file : logFiles) {
          total += file.length();
        }
        if (total > logMaxCacheSize) {
          throw new QiniuRuntimeException("Points too large");
        }
      }
    }
    int logBufferLen = logBuffer.length;
    //check rotate interval
    long currentTime = System.currentTimeMillis();
    Date currentDate = new Date(currentTime);
    Calendar currentCalendar = Calendar.getInstance();
    currentCalendar.setTime(currentDate);

    long currentLogMinute = currentCalendar.get(Calendar.MINUTE) / this.logRotateIntervalInMinutes *
        this.logRotateIntervalInMinutes;
    String currentLogFilePrefix = String
        .format("%04d_%02d_%02d_%02d_%02d", currentCalendar.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH),
            currentCalendar.get(Calendar.HOUR_OF_DAY), currentLogMinute);

    if (this.currentLogWriter == null) {
      //read from local disk when app restarted
      while (true) {
        //skip the old files counter
        String oldLogFileName = String
            .format("%s_%010d.%s.log", currentLogFilePrefix, this.currentLogCounter,
                this.repoName);
        File oldLogFile = new File(this.logCacheDir, oldLogFileName);
        if (oldLogFile.exists()) {
          this.currentLogCounter += 1;
          continue;
        }

        break;//found a new log file
      }

      this.currentLogFileName = String.format("%s_%010d.%s.log.tmp", currentLogFilePrefix,
          this.currentLogCounter, this.repoName);
      this.currentLogFile = new File(this.logCacheDir, this.currentLogFileName);
      boolean createNewLog = false;
      if (currentLogFile.exists()) {
        if (logBufferLen + this.currentLogFile.length() < this.logRotateFileSize) {
          try {
            this.currentLogWriter = new BufferedOutputStream(
                new FileOutputStream(this.currentLogFile,
                    true));
            this.currentFileLength = this.currentLogFile.length();
            this.currentLogCalendar = currentCalendar;
          } catch (FileNotFoundException e) {
            e.printStackTrace();
            //NOTE never happens
          }
        } else {
          this.currentLogCounter += 1;
          createNewLog = true;
        }
      } else {
        createNewLog = true;
      }

      if (createNewLog) {
        //rename the old file
        String currentLogFilePath = this.currentLogFile.getAbsolutePath();
        File dstLogFile = new File(
            currentLogFilePath.substring(0, currentLogFilePath.length() - 4));
        //  this.currentLogFile.renameTo(dstLogFile);
        synchronized (DataSenderCache.this) {
          if (this.currentLogWriter != null && new File(currentLogFilePath).exists()) {
            this.currentLogFile.renameTo(dstLogFile);
          }
          this.currentLogWriter = null;
        }
        //create new file
        this.currentLogFileName = String.format("%s_%010d.%s.log.tmp", currentLogFilePrefix,
            this.currentLogCounter, this.repoName);
        this.currentLogFile = new File(this.logCacheDir, this.currentLogFileName);

        try {
          this.currentLogWriter = new BufferedOutputStream(
              new FileOutputStream(this.currentLogFile));
          this.currentFileLength = 0;
          this.currentLogCalendar = currentCalendar;
        } catch (FileNotFoundException e) {
          e.printStackTrace();
          //NOTE never happens
        }
      }
    }

    //when log writer not null, check the file size before write the log
    //when log writer not null, check the log rotate before write the log
    boolean rotateBySize = this.currentFileLength > this.logRotateFileSize;
    boolean rotateByInterval =
        currentCalendar.getTimeInMillis() - this.currentLogCalendar.getTimeInMillis()
            > this.logRotateIntervalInSec * 1000;
    if (rotateBySize || rotateByInterval) {
      if (rotateByInterval) {
        this.currentLogCounter = 0;
      } else {
        this.currentLogCounter += 1;
      }

      //close the old writer and create a new one
      try {
        this.currentLogWriter.flush();
        this.currentLogWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      //change tmp log file to finished log file
      String currentLogFilePath = this.currentLogFile.getAbsolutePath();
      File dstLogFile = new File(currentLogFilePath.substring(0, currentLogFilePath.length() - 4));
      this.currentLogFile.renameTo(dstLogFile);

      //create new log file
      this.currentLogFileName = String.format("%s_%010d.%s.log.tmp", currentLogFilePrefix,
          this.currentLogCounter, this.repoName);
      this.currentLogFile = new File(this.logCacheDir, this.currentLogFileName);
      try {
        this.currentLogWriter = new BufferedOutputStream(new FileOutputStream(this.currentLogFile));
        this.currentFileLength = 0;
        this.currentLogCalendar = currentCalendar;
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        //NOTE ignore
      }
    }

    //write log buffer to file
    try {
      this.currentLogWriter.write(logBuffer);
      this.currentLogWriter.flush();
      this.currentFileLength += logBufferLen;
    } catch (IOException e) {
      throw new QiniuRuntimeException(e.getMessage());
      //NOTE write error, output the log buffer to console
      // System.out.println(new String(logBuffer, Constants.UTF_8));
    }
  }

  public synchronized void close() {
    isClose = true;
    if (retryThread != null) {
      retryThread.interrupt();
    }
  }
}
