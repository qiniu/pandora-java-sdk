package com.qiniu.pandora.pipeline.sender;

import java.io.File;

public class OptionsBuilder {

  private String repoName = "unKnow";
  private String logCacheDir;
  private int logRotateIntervalInSec;
  private long logRotateFileSize;
  private int logRetryIntervalInSec;
  private int logRetryThreadPoolSize;
  private long logMaxCacheSize = -1;
  private Sender sender;

  private OptionsBuilder() {
  }

  public static OptionsBuilder newOpts() {
    OptionsBuilder opts = new OptionsBuilder();
    opts.sender = new MockSender();
    opts.repoName = "unKnow";
    opts.logRotateIntervalInSec = 10 * 60;
    opts.logRotateFileSize = 1024 * 1024 * 10;
    opts.logRetryIntervalInSec = 5;
    opts.logRetryThreadPoolSize = 1;
    return opts;
  }

  public long getLogMaxCacheSize() {
    return logMaxCacheSize;
  }

  public OptionsBuilder setLogMaxCacheSize(long logMaxCacheSize) {
    this.logMaxCacheSize = logMaxCacheSize;
    return this;
  }

  public OptionsBuilder setSender(Sender sender) {
    this.sender = sender;
    return this;
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
      logCacheDir = new File(".pandoraCacheDir").getAbsolutePath();
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