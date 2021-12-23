package com.qiniu.pandora.common;

/** 定义 Pipeline Repo 中的数据类型 */
public interface ValueType {
  String TypeString = "string";
  String TypeLong = "long";
  String TypeFloat = "float";
  String TypeBoolean = "boolean";
  String TypeDate = "date";
  String TypeGeoPoint = "geo_point";
  String TypeIP = "ip";
  String TypeObject = "object";
}
