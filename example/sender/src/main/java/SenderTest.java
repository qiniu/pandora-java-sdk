import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.sender.FaultTolerantDataSender;
import com.qiniu.pandora.pipeline.sender.OptionsBuilder;
import com.qiniu.pandora.pipeline.sender.Sender;
import com.qiniu.pandora.util.Auth;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SenderTest {

  private static String ak = Config.ACCESS_KEY;
  private static String sk = Config.SECRET_KEY;
  private static String repoName = "testjavasdk2";
  private static String path = ".cacheDir";
  private static FileWriter writer;

  public static void main(String[] atgs) throws Exception {
    Auth auth = Auth.create(ak, sk);

    OptionsBuilder optionsBuilder = OptionsBuilder.newOpts()
        .setLogCacheDir(
            ".pandoraCacheDir")               // 默认为当前目录 .pandoraCacheDir 目录下缓存打点失败的数据，需要有读写权限，或者设置为有读写权限的目录
        .setRepoName(repoName)                            // logCacheData 目录里的文件，会根据该字段过滤，
        .setLogMaxCacheSize(1024 * 1024)                  // 日志最大缓存大小，超过该值后的打点失败的日志则不保存
        .setLogRotateFileSize(1024 * 1024 * 10)            //  切割重试日志的文件大小
        .setLogRotateIntervalInSec(60)                    //  切割重试日志的时间间隔
        .setLogRetryIntervalInSec(10);                    //  当前保存的重试文件发送完后，间隔多少时间继续

    FaultTolerantDataSender sender = new FaultTolerantDataSender(repoName, auth);
    // 初始化缓存工具
    sender.initDataSenderCache(optionsBuilder);

    for (int i = 0; i < 10; i++) {
      SendPointError err = sender.send(defalutData());
    }
    Thread.sleep(1000);
    replace();
    sender.close();
  }

  private static Point makePoint(int i) {
    Point p = new Point();
    if (i % 2 == 0) {
      p.append("pandorajavasdktest", "123456");
    } else {
      p.append("pandorajavasdktest", "XXXXXX");
    }
    return p;
  }

  private static List<Point> defalutData() {
    List<Point> points = new ArrayList<Point>();
    for (int i = 0; i < 5; i++) {
      points.add(makePoint(i));
    }
    return points;
  }

  private static void replace() throws IOException {
    File f = new File(path);
    File[] fs = f.listFiles();
    for (File file : fs) {
      File read = new File(file.getAbsolutePath() + ".old");
      file.renameTo(read);
      BufferedReader br = new BufferedReader(new FileReader(read));
      String ss = "";
      writer = new FileWriter(file);

      try {
        while ((ss = br.readLine()) != null) {
          String content = ss.replace("123456", "test");
          writer.write(content + "\n");
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        writer.flush();
        writer.close();
        read.delete();
      }
    }
  }
}