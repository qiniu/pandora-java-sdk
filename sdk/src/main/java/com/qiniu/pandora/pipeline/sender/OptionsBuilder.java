package com.qiniu.pandora.pipeline.sender;

import java.io.File;

public class OptionsBuilder {

  private String repoName = "unKnow";
  private String logCacheDir;
  private int logRotateIntervalInSec;
  private long logRotateFileSize;
  private int logRetryIntervalInSec;
  private int logRetryThreadPoolSize;
  private Sender sender;


  public static OptionsBuilder newOpts() {
    OptionsBuilder opts = new OptionsBuilder();
    opts.sender = new MockSender();
    opts.repoName = "unKnow";
    opts.logRotateIntervalInSec = 10 * 60;
    opts.logRotateFileSize = 1024 * 1024 * 100;
    opts.logRetryIntervalInSec = 60;
    opts.logRetryThreadPoolSize = 1;
    return opts;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public OptionsBuilder setRepoName(String repoName) {
    this.repoName = repoName;
    return this;
  }

  public OptionsBuilder setLogCacheDir(String logCacheDir) {
    this.logCacheDir = logCacheDir;
    return this;
  }

  public OptionsBuilder setLogRotateIntervalInSec(int logRotateIntervalInSec) {
    this.logRotateIntervalInSec = logRotateIntervalInSec;
    return this;
  }

  public OptionsBuilder setLogRotateFileSize(long logRotateFileSize) {
    this.logRotateFileSize = logRotateFileSize;
    return this;
  }

  public OptionsBuilder setLogRetryIntervalInSec(int logRetryIntervalInSec) {
    this.logRetryIntervalInSec = logRetryIntervalInSec;
    return this;
  }

  public OptionsBuilder setLogRetryThreadPoolSize(int logRetryThreadPoolSize) {
    this.logRetryThreadPoolSize = logRetryThreadPoolSize;
    return this;
  }

  public String getRepoName() {
    return repoName;
  }

  public String getLogCacheDir() {
    if (logCacheDir == null || logCacheDir.length() == 0) {
      logCacheDir = new File(repoName + "_" + System.currentTimeMillis())
          .getAbsolutePath();
    }
    return logCacheDir;
  }


  public long getLogRotateFileSize() {
    return logRotateFileSize;
  }


  public int getLogRetryThreadPoolSize() {
    return logRetryThreadPoolSize;
  }

  public int getLogRotateIntervalInSec() {
    return logRotateIntervalInSec;
  }

  public int getLogRetryIntervalInSec() {
    return logRetryIntervalInSec;
  }

  public Sender getSender() {
    return sender;
  }
}