package com.qiniu.pandora.service.storage;

import java.util.List;

public class StorageDTO<T> {
  private String total;

  private List<T> data;

  public StorageDTO() {}

  public StorageDTO(String total, List<T> data) {
    this.total = total;
    this.data = data;
  }

  public String getTotal() {
    return total;
  }

  public List<T> getData() {
    return data;
  }
}
