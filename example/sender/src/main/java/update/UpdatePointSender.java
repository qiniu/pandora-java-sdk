package update;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.sender.DataSender;
import com.qiniu.pandora.pipeline.sender.Sender;
import com.qiniu.pandora.util.Auth;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import update.SearchService.SearchRequest;
import update.SearchService.SearchResult;
import update.SearchService.SearchResult.Row;


public class UpdatePointSender {

  /*
   *
   * { "title": "title-000000000",	"date": "2018-11-28T13:55:42.004+08:00", 	"key": "15433845420141",	"cids": [{"level":1,"cid":0},{"level":1,"cid":1}]}
   * 其中 date 和 key 为联合主键
   * 需要修改 key 为：XXXX， cids数组中 cid == 1 的对象的 level 为 2
   *
   * */

  static String repoName = "testupdate";
  static String AK = "AK";
  static String SK = "SK";

  public static void main(String[] args) {
//    upload();
    searchAndUpdate();

//    upload();
  }

  public static void searchAndUpdate() {
    Auth auth = Auth.create(AK, SK);
    Sender sender = new DataSender(repoName, auth, "http://cs20:9999");

    CrudLogdbClient client = CrudLogdbClient.NewCrudLogdbClient(AK, SK, "http://cs20:9997");

    SearchRequest searchRequest = new SearchRequest();
    // 需要修改的数据的查询语句
    searchRequest.query = "key:15433872147751";
    try {

      SearchResult result = client.newSearchService().search(repoName, searchRequest);
      for (Row row : result.data) {
        System.out.println("Old: " + row);
        List cids = DocUtil.findArray(row, "cids");
        for (Object cid : cids) {
          // 比较 cids.cid 是否是自己想要的值，
          if (DocUtil.compareObject(cid, "cid", 92700)) {
            DocUtil.changeField(cid, "level", 1222221);
          }
        }
        row.remove("_time");
        System.out.println("New: " + row);

        // update
        String pk = row.get("key") + "";
        String datePK = row.get("date") + "";
        client.putDocument(repoName, datePK, pk, row);

        client.deleteDocument(repoName, datePK, pk);

//        // Use pipeline update
//        // 声明含有相同 联合主键的为 update，反之为 insert
//        Point point = ObjectPoint.fromPointMap(row);
//        System.out.println("Point: " + point);
//        if (point.getSize() > 0) {
//          ((DataSender) sender).send(point.toString().getBytes(Constants.UTF_8));
//        }

      }
    } catch (QiniuException e) {
      e.printStackTrace();
    }

    sender.close();
  }

  // 上传数据
  public static void upload() {
    Auth auth = Auth.create(AK, SK);
    Sender sender = new DataSender(repoName, auth);

    Batch batch = new Batch();
    for (int i = 0; i < 98; i++) {
      Point p = new Point();
      p.append("title", String.format("title-%09d", i));
      p.append("date", new Date());
      p.append("key", System.currentTimeMillis() * 10 + 1 + "");

      List<Object> cids = new ArrayList<Object>();
      int len = 2 + new Random().nextInt(5);
      for (int j = 0; j < len; j++) {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("cid", i * 100 + j);
        objectMap.put("level", 1);
        cids.add(objectMap);
      }
      p.append("cids", cids);

      System.out.println(p);

      if (!batch.canAdd(p) && batch.getSize() > 0) {
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
