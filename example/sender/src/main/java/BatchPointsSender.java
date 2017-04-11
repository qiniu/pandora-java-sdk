import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.sender.DataSender;
import com.qiniu.pandora.pipeline.sender.Sender;
import com.qiniu.pandora.util.Auth;
import java.util.Date;

/**
 * 自己控制数据点的发送情况
 *
 * 1. 每个Batch的发送是原子的，可以选择重试，不用担心数据重复发送。
 * 适合于精确控制自己发送的底层API
 */
public class BatchPointsSender {

  public static void main(String[] args) {
    //设置需要操作的账号的AK和SK
    Auth auth = Auth.create(Config.ACCESS_KEY, Config.SECRET_KEY);
    String repoName = "testjavasdk";
    // 顺序发送
    Sender sender = new DataSender(repoName, auth);
    Batch batch = new Batch();
    for (int i = 0; i < 1000; i++) {
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
    sender.close();
  }

}
