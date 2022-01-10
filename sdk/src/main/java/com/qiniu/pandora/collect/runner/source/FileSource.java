package com.qiniu.pandora.collect.runner.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;

public class FileSource extends AbstractSource implements PollableSource, Configurable {

  private static final Logger logger = LogManager.getLogger(FileSource.class);

  public static final String INPUT_FILE = "input.file";

  private String inputFile;

  @Override
  public void configure(Context context) {
    inputFile = context.getString(INPUT_FILE);
    if (ObjectUtils.isEmpty(inputFile)) {
      throw new IllegalArgumentException("input.file cannot be empty");
    }
  }

  @Override
  public long getBackOffSleepIncrement() {
    return 0;
  }

  @Override
  public long getMaxBackOffSleepInterval() {
    return 0;
  }

  @Override
  public Status process() throws EventDeliveryException {
    Status status = null;

    try {
      Event event = new SimpleEvent();

      Path file = Paths.get(inputFile);
      try (InputStream in = Files.newInputStream(file);
          BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        String line;
        while ((line = reader.readLine()) != null) {
          processLine(line.getBytes());
        }
      } catch (IOException e) {
        logger.error("ERROR: ", e);
      }

      // Store the Event into this Source's associated Channel(s)
      getChannelProcessor().processEvent(event);

      status = Status.READY;
    } catch (Throwable t) {
      // Log exception, handle individual exceptions as needed
      logger.error("ERROR: ", t);
      status = Status.BACKOFF;
    }
    return status;
  }

  private void readLine(byte[] line) {}

  private void processLine(byte[] line) {
    byte[] message = line;
    Event event = new SimpleEvent();
    Map<String, String> headers = new HashMap<>();
    headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
    event.setBody(message);
    event.setHeaders(headers);
    try {
      getChannelProcessor().processEvent(event);
    } catch (ChannelException e) {
      logger.error("ERROR: ", e);
    }
  }
}
