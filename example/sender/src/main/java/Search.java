import com.google.gson.annotations.SerializedName;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.logdb.search.SearchService.SearchRequest;
import com.qiniu.pandora.logdb.search.SearchService.SearchResult;
import com.qiniu.pandora.util.Auth;

public class Search {

  static class ESearchRequest extends SearchRequest {

    @SerializedName("start_time")
    public long startTime;

    @SerializedName("end_time")
    public long endTime;
  }

  static String repoName = "testrepo";
  static String AK = Config.ACCESS_KEY;
  static String SK = Config.SECRET_KEY;


  // 不想升级sdk也想使用 startTime  和 endTime 提高查询速度
  public static void oldSearch() throws QiniuException {
    LogDBClient client = LogDBClient.NewLogDBClient(AK, SK);

    ESearchRequest searchRequest = new ESearchRequest();
    // 需要修改的数据的查询语句
    searchRequest.query = "*";
    searchRequest.startTime = 100000l;
    searchRequest.endTime = 200000l;

    long start = System.currentTimeMillis();
    System.out.println(start);
    SearchResult result = client.NewSearchService().search(repoName, searchRequest);
    long end = System.currentTimeMillis();
    System.out.println(end + "  " + (end - start));
    System.out.println(result.data);
  }


  public static void newSearch() throws QiniuException {
    LogDBClient client = LogDBClient.NewLogDBClient(AK, SK);

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.query = "*";
    searchRequest.startTime = 100000l;
    searchRequest.startTime = 200000l;

    long start = System.currentTimeMillis();
    System.out.println(start);
    SearchResult result = client.NewSearchService().search(repoName, searchRequest);
    long end = System.currentTimeMillis();
    System.out.println(end + "  " + (end - start));
    System.out.println(result.data);
  }

  public static void main(String[] args) throws QiniuException {

  }
}
