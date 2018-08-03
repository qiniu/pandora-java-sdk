package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.Point;

public class MockSender implements Sender {

  @Override
  public Response send(Batch points) throws QiniuException {
    return null;
  }

  @Override
  public SendPointError send(Iterable<Point> points) {
    return null;
  }

  @Override
  public SendPointError sendFromFile(String filePath) throws QiniuException {
    return null;
  }

  @Override
  public SendPointError sendFromString(String points) throws QiniuException {
    return null;
  }

  @Override
  public SendPointError sendFromBytes(byte[] points) throws QiniuException {
    return null;
  }

  @Override
  public void close() {

  }
}
