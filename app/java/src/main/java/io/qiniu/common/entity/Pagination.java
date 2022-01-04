package io.qiniu.common.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pagination {

  private int pageNo;
  private int pageSize;
  private String sort;
  private Order order = Order.DESC;
  private Map<String, Object> filter = new HashMap<>(); // filter可放一些deleted标志
  private String prefix;

  public Pagination(int pageNo, int pageSize) {
    this.pageNo = pageNo;
    this.pageSize = pageSize;

    this.validate();
  }

  public Pagination(int pageNo, int pageSize, String sort, String order) {
    this.sort = sort == null ? "id" : sort.trim();
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.order = order != null && order.equals(Order.ASC.value()) ? Order.ASC : Order.DESC;

    this.validate();
  }

  public Pagination(
      int pageNo,
      int pageSize,
      String sort,
      String order,
      Map<String, Object> filter,
      String prefix) {
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.sort = sort;
    this.order = order != null && order.equals(Order.ASC.value()) ? Order.ASC : Order.DESC;
    this.filter = new HashMap<>(filter);
    this.prefix = prefix;

    this.validate();
  }

  public Pagination(Pagination other) {
    this.pageNo = other.pageNo;
    this.pageSize = other.pageSize;
    this.sort = other.sort;
    this.order = other.order;
    this.filter = new HashMap<>(other.filter);
    this.prefix = other.prefix;
  }

  public Pagination clone() {
    return new Pagination(this);
  }

  public Pagination() {}

  public void addFilterEntry(String key, Object value) {
    filter.put(key, value);
  }

  public <T> List<T> pagination(List<T> list) {
    int fromIndex = Math.min(list.size(), (getPageNo() - 1) * getPageSize());
    int endIndex = Math.min(list.size(), fromIndex + getPageSize());
    return list.subList(fromIndex, endIndex);
  }

  public void validate() {
    if (pageNo < 1 || pageSize < 0) {
      throw new IllegalArgumentException(
          "illegal page parameter pageNo=" + pageNo + " pageSize=" + pageSize);
    }
  }

  public int getPageNo() {
    return pageNo;
  }

  public int getPageSize() {
    return pageSize;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order != null && order.equals(Order.ASC.value()) ? Order.ASC : Order.DESC;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public Map<String, Object> getFilter() {
    return filter;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
