package io.qiniu.common.entity;

import java.util.List;
import java.util.Objects;

public class ResourceList<T> {

  private long pageNo;
  private long pageSize;
  private long totalCount;
  private List<T> resourceList;
  private String listKeyName;

  public ResourceList(
      long totalCount, List<T> resourceList, String listKeyName, long pageNo, long pageSize) {
    this.totalCount = totalCount;
    this.resourceList = resourceList;
    this.listKeyName = listKeyName;
    this.pageNo = pageNo;
    this.pageSize = pageSize;
  }

  public long getCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public void setResourceList(List<T> resourceList) {
    this.resourceList = resourceList;
  }

  public void setPageNo(long pageNo) {
    this.pageNo = pageNo;
  }

  public void setPageSize(long pageSize) {
    this.pageSize = pageSize;
  }

  public List<T> getResourceList() {
    return resourceList;
  }

  @Override
  public String toString() {
    return "ResourceList{"
        + "pageNo="
        + pageNo
        + ", pageSize="
        + pageSize
        + ", totalCount="
        + totalCount
        + ", resourceList="
        + resourceList
        + ", listKeyName='"
        + listKeyName
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResourceList<?> that = (ResourceList<?>) o;
    return pageNo == that.pageNo
        && pageSize == that.pageSize
        && totalCount == that.totalCount
        && Objects.equals(resourceList, that.resourceList)
        && Objects.equals(listKeyName, that.listKeyName);
  }

  @Override
  public int hashCode() {

    return Objects.hash(pageNo, pageSize, totalCount, resourceList, listKeyName);
  }

  public long getPageNo() {
    return pageNo;
  }

  public long getPageSize() {
    return pageSize;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public String getListKeyName() {
    return listKeyName;
  }

  public void setListKeyName(String listKeyName) {
    this.listKeyName = listKeyName;
  }
}
