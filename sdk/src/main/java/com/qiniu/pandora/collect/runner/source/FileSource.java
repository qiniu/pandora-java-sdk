package com.qiniu.pandora.collect.runner.source;

import com.qiniu.pandora.collect.runner.config.EmbeddedRunnerConfiguration;
import com.qiniu.pandora.service.upload.PostDataService;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ObjectUtils;

public class FileSource extends AbstractSource
    implements EventDrivenSource, Configurable, MetaSource {

  private static final Logger logger = LogManager.getLogger(FileSource.class);

  public static final String INPUT_FILE = "input.file";

  private String inputFile;
  private String metaPath;

  private AtomicLong offset;

  public FileSource() {
    super();
    offset = new AtomicLong();
  }

  @Override
  public void configure(Context context) {
    inputFile = context.getString(INPUT_FILE);
    if (ObjectUtils.isEmpty(inputFile)) {
      throw new IllegalArgumentException("input.file cannot be empty");
    }
    metaPath = context.getString(EmbeddedRunnerConfiguration.META_PATH);
    String meta = context.getString(EmbeddedRunnerConfiguration.METADATA);
    if (ObjectUtils.isEmpty(meta)) {
      String metadata = MetadataProcessor.read(getName(), metaPath);
      offset.set(ObjectUtils.isEmpty(metadata) ? 0 : Long.parseLong(metadata));
    } else {
      offset.set(Long.parseLong(meta));
    }
  }

  @Override
  public synchronized void stop() {
    super.stop();
  }

  @Override
  public void start() {
    super.start();
    try (RandomAccessFile raf = new RandomAccessFile(inputFile, "r")) {
      while (raf.length() > offset.get()) {
        raf.seek(offset.get());
        String line = raf.readLine();
        if (ObjectUtils.isEmpty(line)) {
          continue;
        }
        processLine(line.getBytes());
        offset.addAndGet(line.length());
        MetadataProcessor.save(String.valueOf(offset), getName(), metaPath);
      }
    } catch (Exception e) {
      // Log exception, handle individual exceptions as needed
      logger.error("Runner [{}] file source read failed", getName(), e);
    }
  }

  private void processLine(byte[] line) {
    byte[] message = line;
    Event event = new SimpleEvent();
    Map<String, String> headers = new HashMap<>();
    headers.put(PostDataService.ORIGIN, inputFile);
    headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
    event.setBody(message);
    event.setHeaders(headers);
    try {
      // Store the Event into this Source's associated Channel(s)
      getChannelProcessor().processEvent(event);
    } catch (ChannelException e) {
      logger.error("Runner [{}] FileSource process new line failed", getName(), e);
    }
  }

  @Override
  public String getMeta() {
    return String.valueOf(offset);
  }

  @Override
  public void deleteMeta() {
    MetadataProcessor.delete(getName(), metaPath);
  }
}
