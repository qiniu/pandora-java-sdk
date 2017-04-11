import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.sender.ParallelDataSender;
import com.qiniu.pandora.pipeline.sender.Sender;
import com.qiniu.pandora.util.Auth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 并行发送数据点.
 *
 * 注意：
 * 1. 当数据点量巨大的时候适合使用并发打点的方式
 * 2. 数据量小的时候并发多线程创建会更加耗时，建议使用顺次发送
 */
public class ParallelSender {

  public static void main(String[] args) {
    //设置需要操作的账号的AK和SK
    Auth auth = Auth.create(Config.ACCESS_KEY, Config.SECRET_KEY);
    String repoName = "testjavasdk";
    // 并发发送，指定并发线程数为4
    Sender sender = new ParallelDataSender(repoName, auth, 4);
    List<Point> points = new ArrayList<Point>();
    for (int i = 0; i < 1000; i++) {
      Point p = new Point();
      p.append("tag1", i % 3);
      p.append("tag2", String.format("tag%s", i % 3));
      p.append("l1", i);
      p.append("f1", 4.5 + i);
      p.append("t", new Date());
      points.add(p);
    }
    SendPointError err = sender.send(points);
    System.out.println("err.getExceptions() = " + err.getExceptions());
    sender.close();
  }

}
