package io.qiniu.common.entity;

public enum Order {
  DESC("desc"),
  ASC("asc");

  private static final String DESC_VALUE = "desc";
  private static final String ASC_VALUE = "asc";

  Order(String order) {}

  public String value() {
    switch (this) {
      case ASC:
        return ASC_VALUE;
      case DESC:
        return DESC_VALUE;
    }
    return null;
  }

  public static Order from(String s) {
    if (s == null || s.isEmpty()) {
      return Order.DESC;
    }

    if (s.equals(ASC_VALUE)) {
      return Order.ASC;
    }
    return Order.DESC;
  }
}
