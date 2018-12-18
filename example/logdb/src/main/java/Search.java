import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.logdb.*;
import com.qiniu.pandora.logdb.search.MultiSearchService;
import com.qiniu.pandora.logdb.search.PartialSearchService;
import com.qiniu.pandora.logdb.search.ScrollSearchService;
import com.qiniu.pandora.logdb.search.SearchService;
import com.qiniu.pandora.logdb.search.SearchService.SearchRequest;
import com.qiniu.pandora.logdb.search.SearchService.SearchRequest.Highlight;
import com.qiniu.pandora.util.Json;

import java.util.*;

public class Search {

  public static void main(String[] args) {
    String repo = "YOUR_REPO_NAME";
    LogDBClient logDBClient = LogDBClient.NewLogDBClient(Config.ACCESS_KEY, Config.SECRET_KEY);

    // logdb search
    ArrayList<String> pre_tags = new ArrayList<String>();
    pre_tags.add("<em>");
    ArrayList<String> post_tags = new ArrayList<String>();
    post_tags.add("</em>");
    HashMap<String, HashMap<String, String>> fields = new HashMap<String, HashMap<String, String>>();
    fields.put("a", new HashMap<String, String>());
    Highlight hl = new SearchService.SearchRequest().new Highlight(pre_tags, post_tags, fields,
        false, 100);
    try {
      SearchService searchService = logDBClient.NewSearchService();
      SearchService.SearchRequest search = new SearchRequest();
      search.from = 0;
      search.size = 1;
      search.query = "*";
      search.highlight = hl;
      SearchService.SearchResult searchRet = searchService.search(repo, search);
      System.out.println(searchRet);
    } catch (QiniuException e) {
      e.printStackTrace();
    }
    // logdb post msearch
    try {
      MultiSearchService multiSearchService = logDBClient.NewMultiSearchService();

      MultiSearchService.SearchRequest searchRequest1 = new MultiSearchService.SearchRequest();
      MultiSearchService.SearchRequest searchRequest2 = new MultiSearchService.SearchRequest();
      searchRequest1.source = "{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647},\"aggs\":{\"2\":{\"date_histogram\":{\"field\":\"timestamp\",\"interval\":\"60s\",\"time_zone\":\"Asia/Shanghai\",\"min_doc_count\":0,\"extended_bounds\":{\"min\":1496644491486,\"max\":1496644791486}}}},\"fields\":[\"*\",\"_source\"],\"script_fields\":{},\"fielddata_fields\":[\"timestamp\"]}";
      searchRequest1.repo = repo;
      searchRequest2.source = "{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647}}";
      searchRequest2.repo = repo;

      List<MultiSearchService.SearchRequest> searchRequestList = new ArrayList<MultiSearchService.SearchRequest>();
      searchRequestList.add(searchRequest1);
      searchRequestList.add(searchRequest2);
      MultiSearchService.SearchResult searchResult = multiSearchService.search(searchRequestList);
      System.out.println(Json.encode(searchResult.responses));
    } catch (QiniuException e) {
      e.printStackTrace();
    }
    // logdb scroll search
    try {
      SearchService searchService = logDBClient.NewSearchService();
      SearchService.SearchRequest search = new SearchRequest();
      search.from = 0;
      search.size = 10;
      search.query = "*";
      search.scroll = "1m";
      SearchService.SearchResult searchResult = searchService.search(repo, search);
      System.out.println(searchResult);
      int count = 1;
      while (searchResult.data.size() > 0 && !searchResult.scrollID.equals("")) {
        if (count > 100) {
          break;
        }
        System.out.println("count" + count++ + "----------------------------------");
        ScrollSearchService scrollsearchService = logDBClient.NewScrollSearchService();
        searchResult = scrollsearchService.scroll(repo, "1m", searchResult.scrollID);
        System.out.println(searchResult);
      }
    } catch (QiniuException e) {
      e.printStackTrace();
    }
//    // partial search
    /*
    如果数据特别大的时候，logdb会对查询进行优化，用户可以持续调用该接口用来获得更加完整的结果。
    SearchRet.process 来了解查询进度.
    SearchRet.partialSuccess 来了解查询是否结束。
     */
    PartialSearchService partialSearchService = logDBClient.NewPartialSearchService();
    try {
      Calendar instance = Calendar.getInstance();
      long endTime = instance.getTimeInMillis();
      instance.add(Calendar.HOUR, -24 * 30);
      long startTime = instance.getTimeInMillis();

      PartialSearchService.SearchRequest searchRequest = new PartialSearchService.SearchRequest();
      searchRequest.size = 1;
      searchRequest.queryString = "*";
      searchRequest.sort = "timestamp";
      searchRequest.startTime = startTime;
      searchRequest.endTime = endTime;

      PartialSearchService.SearchResult searchRet = partialSearchService
          .search(repo, searchRequest);

      for (PartialSearchService.SearchResult.Row r : searchRet.hits) {
        Object highlight = r.get("highlight");
        if (highlight != null) {
          Map<String, List<String>> highlightMap = (Map<String, List<String>>) highlight;
          for (Map.Entry<String, List<String>> entry : highlightMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
          }
        }
        break;
      }

      System.out.println(searchRet);
      while (searchRet.partialSuccess) {
        searchRet = partialSearchService.search(repo, searchRequest);
        Object h = searchRet.hits.get(0).get("highlight");
        if (h != null) {
          Map<String, List<String>> highlightMap = (Map<String, List<String>>) h;
          for (Map.Entry<String, List<String>> entry : highlightMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
          }
        }
        System.out.println(searchRet);
        Thread.sleep(5000);
      }

    } catch (QiniuException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}