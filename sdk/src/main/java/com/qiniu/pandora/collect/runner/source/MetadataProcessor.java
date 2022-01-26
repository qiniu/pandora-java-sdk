package com.qiniu.pandora.collect.runner.source;

import com.qiniu.pandora.util.StringUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.flume.FlumeException;

public class MetadataProcessor {

  public static void save(String metadata, String runner, String metaDir) {
    Path metaPath = Paths.get(metaDir);
    try {
      Files.createDirectories(metaPath);
    } catch (IOException e) {
      throw new FlumeException("Error creating positionFile parent directories", e);
    }

    File file = new File(pathname(runner, metaDir));
    try (FileWriter writer = new FileWriter(file, false)) {
      writer.write(metadata);
    } catch (IOException e) {
      throw new FlumeException("save metadata into local file failed", e);
    }
  }

  public static String read(String runner, String metaDir) throws FlumeException {
    File file = new File(pathname(runner, metaDir));
    if (!file.exists()) {
      return null;
    }
    try {
      return StringUtils.utf8String(FileUtils.readFileToByteArray(file));
    } catch (IOException e) {
      throw new FlumeException("read metadata from local file failed", e);
    }
  }

  public static void delete(String runner, String metaDir) {
    File file = new File(pathname(runner, metaDir));
    try {
      if (file.exists()) {
        file.delete();
      }
    } catch (Exception e) {
      throw new FlumeException("delete local file failed", e);
    }
  }

  private static String pathname(String runner, String metaDir) {
    return Paths.get(metaDir, String.format("%s.meta", runner)).toString();
  }
}
