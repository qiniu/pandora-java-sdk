package com.qiniu.pandora.collect.runner.sinks;

import com.qiniu.pandora.DefaultPandoraClient;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.service.upload.PostDataResponse;
import com.qiniu.pandora.service.upload.PostDataService;
import java.io.IOException;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;

public class PandoraSink extends AbstractSink implements Configurable {

  private static final Logger logger = LogManager.getLogger(PandoraSink.class);

  public static final String HOSTNAME = "hostname";
  public static final String REPO = "repo";
  public static final String SOURCE_TYPE = "source_type";
  public static final String PANDORA_HOST = "pandora_host";
  public static final String TOKEN = "token";

  private String hostname;
  private String repo;
  private String sourceType;
  private String token;

  private PostDataService postDataService;

  @Override
  public final void configure(final Context context) {
    String hostname = context.getString(HOSTNAME, "");
    if (ObjectUtils.isEmpty(hostname)) {
      hostname = "localhost";
    }

    String repo = context.getString(REPO);
    if (ObjectUtils.isEmpty(repo)) {
      throw new IllegalArgumentException("repo cannot be empty");
    }

    String sourceType = context.getString(SOURCE_TYPE);
    if (ObjectUtils.isEmpty(sourceType)) {
      throw new IllegalArgumentException("source_type cannot be empty");
    }

    String pandoraHost = context.getString(PANDORA_HOST);
    if (ObjectUtils.isEmpty(pandoraHost)) {
      throw new IllegalArgumentException("pandora_host cannot be empty");
    }

    String token = context.getString(TOKEN);
    if (ObjectUtils.isEmpty(pandoraHost)) {
      throw new IllegalArgumentException("pandora_host cannot be empty");
    }

    this.hostname = hostname;
    this.repo = repo;
    this.sourceType = sourceType;
    this.token = token;
    postDataService = new PostDataService(new DefaultPandoraClient(pandoraHost));
  }

  @Override
  public final void start() {
    logger.info("Starting HttpSink");
  }

  @Override
  public final void stop() {
    logger.info("Stopping HttpSink");
  }

  @Override
  public final Status process() throws EventDeliveryException {
    Status status = Status.READY;

    Channel ch = getChannel();
    Transaction txn = ch.getTransaction();
    txn.begin();

    try {
      Event event = ch.take();

      byte[] eventBody = null;
      if (event != null) {
        eventBody = event.getBody();
      }

      if (eventBody != null && eventBody.length > 0) {
        logger.debug("Sending request : " + new String(event.getBody()));

        // todo check success / fail
        PostDataResponse response;
        try {
          response =
              postDataService.upload(
                  eventBody,
                  hostname,
                  "",
                  repo,
                  sourceType,
                  token,
                  Constants.CONTENT_TYPE_APPLICATION_JSON);
        } catch (IOException e) {
          txn.rollback();
          status = Status.BACKOFF;
          logger.error("upload data failed", e);
        }
      } else {
        txn.commit();
        status = Status.BACKOFF;

        logger.warn("Processed empty event");
      }

    } catch (Throwable t) {
      txn.rollback();
      status = Status.BACKOFF;

      logger.error("Error sending HTTP request, retrying", t);

      // re-throw all Errors
      if (t instanceof Error) {
        throw (Error) t;
      }

    } finally {
      txn.close();
    }
    return status;
  }
}
