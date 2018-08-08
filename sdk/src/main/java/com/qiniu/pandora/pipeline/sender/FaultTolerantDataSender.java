package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuRuntimeException;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.util.Auth;
import java.io.File;
import java.io.FileNotFoundException;

public class FaultTolerantDataSender extends DataSender {

  DataSenderCache cache = null;
  String repo = "unknow";

  public FaultTolerantDataSender(String repoName, Auth auth) {
    super(repoName, auth);
    this.repo = repoName;
  }

  public FaultTolerantDataSender(String repoName, PandoraClient pandoraClient) {
    super(repoName, pandoraClient);
    this.repo = repoName;
  }

  public FaultTolerantDataSender(String repoName, Auth auth, String pipelineHost) {
    super(repoName, auth, pipelineHost);
    this.repo = repoName;
  }

  public FaultTolerantDataSender(String repoName, PandoraClient pandoraClient,
      String pipelineHost) {
    super(repoName, pandoraClient, pipelineHost);
    this.repo = repoName;
  }

  public void initDataSenderCache(OptionsBuilder builder) throws Exception {
    builder.setSender(this);
    if ("unKnow".equals(builder.getRepoName())) {
      builder.setRepoName(repo);
    }
    cache = DataSenderCache.getInstance(builder);
  }

  @Override
  public Response send(Batch points) throws QiniuException {
    byte[] bs = points.toString().getBytes(Constants.UTF_8);
    try {
      super.send(bs);
    } catch (QiniuException ex) {
      if (ex.response != null && ex.response.statusCode / 100 == 4) {
        throw ex;
      } else {
        cache.write(bs);
      }
    }
    return null;
  }

  @Override
  public SendPointError send(Iterable<Point> points) {
    SendPointError se = new SendPointError();
    Batch b = new Batch();
    for (Point p : points) {
      if (p.isTooLarge()) {
        se.addFail(p, new QiniuRuntimeException("Point too large"));
        // cache.write(points.toString().getBytes(Constants.UTF_8));
        continue;
      }
      if (!b.canAdd(p) && b.getSize() > 0) {
        try {
          send(b);
        } catch (QiniuException e) {
          se.addFails(b.getPoints(), e);
        }
        b.clear();
      }
      b.add(p);
    }
    try {
      send(b);
    } catch (QiniuException e) {
      se.addFails(b.getPoints(), e);
    }
    return se;
  }

  @Override
  public SendPointError sendFromFile(String filePath) throws QiniuException {
    Iterable<Point> points = readFromFile(filePath);
    return send(points);
  }

  @Override
  public SendPointError sendFromString(String points) throws QiniuException {
    Iterable<Point> ps = Point.fromPointsString(points);
    return send(ps);
  }

  @Override
  public SendPointError sendFromBytes(byte[] points) throws QiniuException {
    String ps = new String(points, Constants.UTF_8);
    return sendFromString(ps);
  }

  @Override
  public void close() {
    if (cache != null) {
      cache.close();
    }
  }
}
