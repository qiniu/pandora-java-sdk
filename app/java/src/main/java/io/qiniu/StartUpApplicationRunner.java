package io.qiniu;

import com.qiniu.pandora.common.QiniuException;
import io.qiniu.configuration.PandoraProperties;
import io.qiniu.service.PandoraService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PreDestroy;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StartUpApplicationRunner implements ApplicationRunner {

  private static final Logger logger = LogManager.getLogger(StartUpApplicationRunner.class);

  private static final String ID_PATH = ".id";
  private static final String PID_PATH = ".pid";

  private final PandoraService pandoraService;
  private final PandoraProperties properties;

  private RandomAccessFile randomAccessFile;
  private FileChannel channel;
  private FileLock pidLock;

  @Autowired
  public StartUpApplicationRunner(PandoraService pandoraService, PandoraProperties properties) {
    this.pandoraService = pandoraService;
    this.properties = properties;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (!acquirePid()) {
      throw new QiniuException("not allow to start multiple instances");
    }

    setId();
  }

  private boolean acquirePid() throws QiniuException {
    try {
      randomAccessFile = new RandomAccessFile(PID_PATH, "rw");
      channel = randomAccessFile.getChannel();
      pidLock = channel.tryLock();
      if (pidLock == null) {
        logger.error("another instance is running, pid file is locked");
        return false;
      }
    } catch (Exception e) {
      throw new QiniuException("check pid failed");
    }

    return true;
  }

  @PreDestroy
  public void releasePid() {
    try {
      if (pidLock != null) {
        pidLock.release();
      }
    } catch (Exception e) {
      logger.error("release pid lock failed", e);
    } finally {
      try {
        if (null != randomAccessFile) {
          randomAccessFile.close();
        }
        if (null != channel) {
          channel.close();
        }
      } catch (Exception e) {
        logger.error("close pid file failed", e);
      }
    }
  }

  private void setId() throws IOException {
    Path idPath = Paths.get(ID_PATH);

    String id = properties.getId();
    BufferedReader reader = Files.newBufferedReader(idPath);
    String content = reader.readLine();

    if (id != null) {
      content = id;
    }

    // generate a new id and save it to file
    // or use content as id to register
    id =
        pandoraService
            .getCustomService()
            .register(
                properties.getAppName(),
                properties.getServiceName(),
                properties.getServerAddress(),
                properties.getServerPort(),
                properties.getPandoraToken(),
                content);

    if (id == null) {
      throw new QiniuException("start failed, worker id is null");
    }

    // save worker id to file
    BufferedWriter writer = Files.newBufferedWriter(idPath);
    writer.write(id);

    properties.setId(id);

    logger.info(String.format("worker id is %s", properties.getId()));
  }
}
