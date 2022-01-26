package com.qiniu.pandora.collect.runner.sinks;

import com.qiniu.pandora.collect.CollectorContext;
import com.qiniu.pandora.service.upload.PostDataResponse;
import com.qiniu.pandora.service.upload.PostDataService;
import com.qiniu.pandora.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.sink.AbstractSink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ObjectUtils;

public class PandoraSink extends AbstractSink implements ContextConfigurable {

  private static final Logger logger = LogManager.getLogger(PandoraSink.class);

  // request
  private static final String RAW = "raw";

  // config name
  public static final String HOSTNAME = "hostname";
  public static final String REPO = "repo";
  public static final String SOURCE_TYPE = "source_type";
  public static final String BATCH_SIZE = "batch_size";

  private String hostname;
  private String repo;
  private String sourceType;
  private int batchSize;

  private PostDataService postDataService;

  @Override
  public void configContext(CollectorContext context) {
    this.postDataService = context.getPostDataService();
  }

  @Override
  public final void configure(final Context context) {
    String hostname = context.getString(HOSTNAME, "");

    String repo = context.getString(REPO);
    if (ObjectUtils.isEmpty(repo)) {
      throw new IllegalArgumentException("repo cannot be empty");
    }

    String sourceType = context.getString(SOURCE_TYPE);
    if (ObjectUtils.isEmpty(sourceType)) {
      throw new IllegalArgumentException("source_type cannot be empty");
    }

    int batchSize = context.getInteger(BATCH_SIZE, 2000);

    this.hostname = hostname;
    this.repo = repo;
    this.sourceType = sourceType;
    this.batchSize = batchSize;

    if (postDataService == null) {
      throw new IllegalArgumentException("post data service must be set");
    }
  }

  @Override
  public final void start() {
    super.start();
    logger.info("Runner [{}] starting http sink", getName());
  }

  @Override
  public final void stop() {
    super.stop();
    logger.info("Runner [{}] stopping http sink", getName());
    // todo send channel all data
  }

  @Override
  public final Status process() throws EventDeliveryException {
    Status status;

    Channel channel = getChannel();
    Transaction txn = channel.getTransaction();
    txn.begin();

    try {
      List<Map<String, Object>> data = new ArrayList<>(batchSize);
      List<Event> events = new ArrayList<>(batchSize);
      while (data.size() < batchSize) {
        Event event = channel.take();
        if (event == null) {
          break;
        }
        byte[] eventBody = event.getBody();
        if (eventBody != null && eventBody.length > 0) {
          data.add(combinePandoraBody(event));
          events.add(event);
        }
      }

      if (!ObjectUtils.isEmpty(data)) {
        PostDataResponse response;
        try {
          response = postDataService.upload(data, hostname, "", repo, sourceType);
        } catch (IOException e) {
          txn.rollback();
          status = Status.BACKOFF;
          logger.error("Runner [{}] upload data failed", getName(), e);
          return status;
        }
        handleResponse(events, response, channel);
        txn.commit();
        status = Status.READY;
      } else {
        txn.commit();
        status = Status.READY;

        logger.warn("Runner [{}] processed empty event", getName());
      }

    } catch (Throwable t) {
      txn.rollback();
      status = Status.BACKOFF;

      logger.error("Runner [{}] failed to send pandora sink request, retrying", getName(), t);

      // re-throw all Errors
      if (t instanceof Error) {
        throw (Error) t;
      }

    } finally {
      txn.close();
    }
    return status;
  }

  private void handleResponse(List<Event> events, PostDataResponse response, Channel channel) {
    if (events.size() != response.getTotal()) {
      logger.error(
          "Runner [{}] pandora sink expect send {} data, but got total response {}",
          getName(),
          events.size(),
          response.getTotal());
    }
    if (ObjectUtils.isEmpty(response.getDetails())) {
      return;
    }
    logger.error(
        "Runner [{}] pandora sink expect send {} data, but success data count is {}, failed data count is {}, will retry to send failed data",
        getName(),
        response.getTotal(),
        response.getSuccess(),
        response.getFailure());
    Set<String> errorMsg = new HashSet<>(response.getDetails().size());
    response
        .getDetails()
        .forEach(
            detail -> {
              if (detail.getStatus() / 200 == 1) {
                return;
              }
              for (int i = detail.getStartPos(); i < detail.getEndPos(); i++) {
                channel.put(events.get(i));
              }
              if (!ObjectUtils.isEmpty(detail.getMessage())) {
                errorMsg.add(detail.getMessage());
              }
            });
    if (!ObjectUtils.isEmpty(errorMsg)) {
      logger.error(
          "Runner [{}] pandora sink failed message is [{}]", getName(), String.join(",", errorMsg));
    }
  }

  private static Map<String, Object> combinePandoraBody(Event event) {
    Map<String, Object> data;
    if (ObjectUtils.isEmpty(event.getHeaders())) {
      data = new HashMap<>();
    } else {
      data = new HashMap<>(event.getHeaders().size() + 1);
      data.putAll(event.getHeaders());
    }
    data.put(RAW, StringUtils.utf8String(event.getBody()));
    return data;
  }
}
