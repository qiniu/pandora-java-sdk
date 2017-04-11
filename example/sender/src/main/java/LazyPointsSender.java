import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.AbstractPointsGenerator;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.sender.ParallelDataSender;
import com.qiniu.pandora.pipeline.sender.Sender;
import com.qiniu.pandora.util.Auth;
import java.util.Date;
import java.util.Iterator;

/**
 * 迭代器进行打点的方式。
 *
 * 注意：
 * 1. 使用点迭代器进行数据发送，避免大量数据点存储在内存当中
 */
public class LazyPointsSender {

  /**
   * 生成数据点迭代器。避免把所有数据点全部放在内存中
   */
  public static class PointsGenerator extends AbstractPointsGenerator {

    int i = 0;
    int max;

    public PointsGenerator(int max) {
      this.max = max;
    }

    public boolean hasNext() {
      return i < max;
    }

    public Point next() {
      Point p = new Point();
      p.append("tag1", i % 3);
      p.append("tag2", String.format("tag%s", i % 3));
      p.append("l1", i);
      p.append("f1", 4.5 + i);
      p.append("t", new Date());
      i++;
      return p;
    }
  }

  public static void main(String[] args) {
    Auth auth = Auth.create(Config.ACCESS_KEY, Config.SECRET_KEY);
    String repoName = "testjavasdk";
    // 并发发送，指定并发线程数为4
    Sender sender = new ParallelDataSender(repoName, auth, 4);

    // 构造点迭代器传给sender 进行并发发送
    SendPointError err = sender.send(new Iterable<Point>() {
      public Iterator<Point> iterator() {
        return new PointsGenerator(1000);
      }
    });
    System.out.println("err.getExceptions() = " + err.getExceptions());
    sender.close();
  }

}
