import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.sender.OptionsBuilder;
import com.qiniu.pandora.util.Auth;
import java.io.FileNotFoundException;
import java.util.Date;

public class FaultTolerantDataSender {

  public static void main(String[] args) {
    //设置需要操作的账号的AK和SK
    String ak = Config.ACCESS_KEY;
    String sk = Config.SECRET_KEY;
    Auth auth = Auth.create(ak, sk);
    String repoName = "testjavasdk";
    // 顺序发送
    com.qiniu.pandora.pipeline.sender.FaultTolerantDataSender sender = new com.qiniu.pandora.pipeline.sender.FaultTolerantDataSender(
        repoName, auth);

    // 注： FaultTolerantDataSender 会将 LogCacheDir 目录里的文件根据 repoName 进行过滤，进行重试发送
    OptionsBuilder optionsBuilder = OptionsBuilder.newOpts()
        .setLogCacheDir(
            ".pandoraCacheDir")               // 默认为当前目录 .pandoraCacheDir 目录下缓存打点失败的数据，需要有读写权限，或者设置为有读写权限的目录
        .setRepoName(repoName)                            // logCacheData 目录里的文件，会根据该字段过滤，
        .setLogMaxCacheSize(1024 * 1024 * 100)                  // 日志最大缓存大小，超过该值后的打点失败的日志则不保存
        .setLogRotateFileSize(1024 * 1024 * 10)            //  切割重试日志的文件大小
        .setLogRotateIntervalInSec(60 * 5)                    //  切割重试日志的时间间隔
        .setLogRetryIntervalInSec(10);                    //  当前保存的重试文件发送完后，间隔多少时间继续

    try {
      // 初始化缓存
      sender.initDataSenderCache(optionsBuilder);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    Batch batch = new Batch();
    for (int i = 0; i < 10000; i++) {
      Point p = new Point();
      p.append("tag1", i % 3);
      p.append("tag2", String.format("tag%s", i % 3));
      p.append("l1", i);
      p.append("f1", 4.5 + i);
      p.append("t", new Date());
      if (!batch.canAdd(p) && batch.getSize() > 0) {
        // 判断Batch还可以增加新的数据
        try {
          sender.send(batch);
        } catch (QiniuException e) {
          e.printStackTrace();
        }
        batch.clear();
      }
      batch.add(p);
    }
    // 剩余没有发送的数据
    if (batch.getSize() > 0) {
      try {
        sender.send(batch);
      } catch (QiniuException e) {
        e.printStackTrace();
      }
      batch.clear();
    }

    // 关闭sender，否则后台缓存线程不退出
    sender.close();
  }
}
