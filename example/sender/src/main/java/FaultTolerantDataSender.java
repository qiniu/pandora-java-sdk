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
        .setRepoName(repoName)                            // logCacheData 目录里的文件，会根据该字段过滤，
        //.setLogCacheDir(".cacheDir")                     // 保存打点失败的日志目录，需要有读写权限，
        .setLogRotateFileSize(1024 * 1024 * 1024)              //  切割重试日志的文件大小
        .setLogRotateIntervalInSec(60)                 //  切割重试日志的时间间隔
        .setLogRetryIntervalInSec(5);                   //  当前保存的重试文件发送完后，间隔多少时间继续
    try {
      sender.initDataSenderCache(optionsBuilder);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    Batch batch = new Batch();
    for (int i = 0; i < 100000; i++) {
      Point p = new Point();
      p.append("pandorajavasdktest", "1234561");
      if(i % 10000 == 1){
        p.append("pandorajavasdktest", "123456");
      }
//      p.append("tag1", i % 3);
//      p.append("tag2", String.format("tag%s", i % 3));
//      p.append("l1", i);
//      p.append("f1", 4.5 + i);
//      p.append("t", new Date());
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
    sender.close();
  }
}
