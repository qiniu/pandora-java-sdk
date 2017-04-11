import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.sender.DataSender;
import com.qiniu.pandora.pipeline.sender.Sender;
import com.qiniu.pandora.util.Auth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 顺次发送数据点.
 *
 * 注意：
 * 1. 当数据点量巨大的时候适合使用并发打点的方式
 * 2. 数据量小的时候并发多线程创建会更加耗时，建议使用顺次发送
 */
public class SequenceSender {

  public static void main(String[] args) {
    //设置需要操作的账号的AK和SK
    Auth auth = Auth.create(Config.ACCESS_KEY, Config.SECRET_KEY);
    String repoName = "testjavasdk";
    // 顺序发送
    Sender sender = new DataSender(repoName, auth);
    List<Point> points = new ArrayList<Point>();
    for (int i = 0; i < 1000; i++) {
      Point p = new Point();
      p.append("tag1", i % 3);
      p.append("tag2", String.format("tag%s", i % 3));
      p.append("l1", i);
      p.append("f1", 4.5 + i);
      p.append("t", new Date());
      points.add(p);
      System.out.println(p.toString().trim());
    }
    SendPointError error = sender.send(points);
    if (error.getExceptions().size() > 0) {
      System.out.println("error.getExceptions() = " + error.getExceptions());
    }
    sender.close();
  }
}
