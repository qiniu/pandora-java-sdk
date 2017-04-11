import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.sender.ParallelDataSender;
import com.qiniu.pandora.pipeline.sender.Sender;
import com.qiniu.pandora.util.Auth;
import java.net.URL;

/**
 * 从文件中读取数据点进行发送
 */
public class FileSender {


  public static void main(String[] args) {
    URL file = FileSender.class.getResource("points.txt");
    //设置需要操作的账号的AK和SK
    Auth auth = Auth.create(Config.ACCESS_KEY, Config.SECRET_KEY);
    String repoName = "testjavasdk";
    // 并发发送，指定并发线程数为4
    Sender sender = new ParallelDataSender(repoName, auth, 4);
    // 或者也可以选择顺序发送，视数据多少和资源情况
    // Sender sender = new DataSender(repoName, auth);
    SendPointError error = null;
    try {
      error = sender.sendFromFile(file.getPath());
    } catch (QiniuException e) {
      e.printStackTrace();
    }
    if (error != null) {
      System.out.println("error.getExceptions() = " + error.getExceptions());
    }
    sender.close();
  }
}
