import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.logdb.MultiSearchService;
import com.qiniu.pandora.logdb.MultiSearchService.MultiSearchResult;
import com.qiniu.pandora.logdb.MultiSearchService.SearchResponse;
import java.util.ArrayList;
import java.util.List;

public class MSearch {

  static String AK = "<AK>";
  static String SK = "<SK>";


  public static void main(String[] args) throws Exception {
    LogDBClient client = LogDBClient.NewLogDBClient(AK, SK);
    String statement = "<查询语句>";
    List<String> list = new ArrayList();
    list.add("<repo1>");
    list.add("<repo2>");
    // case 1
    long start = 0;
    long end = 0;
    MultiSearchService multiSearchService = new MultiSearchService(client);
    multiSearchService.setEndTimeInMillis(end);
    multiSearchService.setStartTimeInMillis(start);

    String repos = String.join("\",\"", list);
    MultiSearchService.SearchRequest searchRequest1 = new MultiSearchService.SearchRequest(
        statement, repos);
    MultiSearchResult result = multiSearchService.add(searchRequest1).action();
    System.out.println(result.getResponses().size());

    // case 2
    multiSearchService = new MultiSearchService(client);
    multiSearchService.setEndTimeInMillis(end);
    multiSearchService.setStartTimeInMillis(start);
    for (String key : list) {
      searchRequest1 = new MultiSearchService.SearchRequest(
          statement, key);
      multiSearchService.add(searchRequest1);
    }
    result = multiSearchService.action();
    System.out.println(result.getResponses().size());
    for (SearchResponse response : result.getResponses()) {
      System.out.println(response);
    }
  }
}