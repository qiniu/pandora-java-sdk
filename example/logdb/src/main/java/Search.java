
import com.google.gson.Gson;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuRequestException;
import com.qiniu.pandora.logdb.Highlight;
import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.logdb.MultiSearchService;
import com.qiniu.pandora.logdb.SearchService;
import com.qiniu.pandora.util.Json;

import java.util.ArrayList;
import java.util.HashMap;

public class Search {
  public static void main(String[] args) {
    String repo = "repo";
    LogDBClient logDBClient = LogDBClient.NewLogDBClient(Config.ACCESS_KEY,Config.SECRET_KEY);
    // logdb search

    ArrayList<String> pre_tags = new ArrayList<String>();
    pre_tags.add( "<em>");
    ArrayList<String> post_tags = new ArrayList<String>();
    post_tags.add( "</em>");
    HashMap<String,HashMap<String,String>> fields = new HashMap<String,HashMap<String,String>>();
    fields.put("a",new HashMap<String, String>());
    Highlight hl = new Highlight(pre_tags,post_tags, fields, false, 100);
    try {
      SearchService searchService = logDBClient.NewSearchService();
      SearchService.SearchRet searchRet = searchService.setFrom(0).setSize(1).
              setQuerystring("*").setRepo(repo).setHighlight(hl)
              .action();
      System.out.println(searchRet);
    } catch (QiniuException e) {
      e.printStackTrace();
    }
//    // logdb post msearch
    try {
      MultiSearchService multiSearchService = logDBClient.NewMultiSearchService();
      MultiSearchService.SearchRequest searchRequest1 = new MultiSearchService.SearchRequest("{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647},\"aggs\":{\"2\":{\"date_histogram\":{\"field\":\"timestamp\",\"interval\":\"60s\",\"time_zone\":\"Asia/Shanghai\",\"min_doc_count\":0,\"extended_bounds\":{\"min\":1496644491486,\"max\":1496644791486}}}},\"fields\":[\"*\",\"_source\"],\"script_fields\":{},\"fielddata_fields\":[\"timestamp\"]}"
              , repo);
      MultiSearchService.SearchRequest searchRequest2 = new MultiSearchService.SearchRequest("{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647}}"
              , repo);
      MultiSearchService.MultiSearchResult searchResult = multiSearchService
              .add(searchRequest1)
              .add(searchRequest2).action();

      System.out.println(Json.encode(searchResult.getResponses()));

      multiSearchService.reset();
      searchResult = multiSearchService.add(searchRequest1).action();

      System.out.println(Json.encode(searchResult.getResponses()));
    } catch (QiniuException e) {
      e.printStackTrace();
    }
    // logdb scroll search
    try {
      SearchService searchService = logDBClient.NewSearchService();
      SearchService.SearchRet searchRet = searchService.setFrom(0).setSize(10).
              setQuerystring("*").setRepo(repo).setScroll("1m")
              .action();
      System.out.println(searchRet);
      int count = 1;
      while (searchRet.getData().size()>0&& searchRet.getScroll_id()!=""){
        System.out.println("count"+count+++"----------------------------------");
        searchRet = logDBClient.NewScrollService().setScroll_id(searchRet.getScroll_id()).action();
        System.out.println(searchRet);
      }
    } catch (QiniuException e) {
      e.printStackTrace();
    }
  }
}
