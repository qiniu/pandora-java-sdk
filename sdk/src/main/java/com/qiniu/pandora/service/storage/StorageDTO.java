package com.qiniu.pandora.service.storage;

import java.util.List;

public class StorageDTO<T> {
  private long total;

  private List<T> data;

  public StorageDTO() {}

  public StorageDTO(long total, List<T> data) {
    this.total = total;
    this.data = data;
  }

  public long getTotal() {
    return total;
  }

  public List<T> getData() {
    return data;
  }
}
