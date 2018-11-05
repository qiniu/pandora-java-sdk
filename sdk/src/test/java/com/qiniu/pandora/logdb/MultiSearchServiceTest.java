package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.logdb.search.MultiSearchService;
import com.qiniu.pandora.util.Auth;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiSearchServiceTest {
    private MultiSearchService multiSearchService;

    private String repoName;

    @Before
    public void setUp() {
        String ak = TestConfig.ACCESS_KEY;
        String sk = TestConfig.SECRET_KEY;
        if (Strings.isNullOrEmpty(ak)){
            ak = System.getenv("QINIU_ACCESS_KEY");
            sk = System.getenv("QINIU_SECRETY_KEY");
        }
        Auth auth = Auth.create(ak,sk);
        PandoraClient client = new PandoraClientImpl(auth);
        LogDBClient logDBClient = new LogDBClient(client);
        this.multiSearchService = logDBClient.NewMultiSearchService();
        this.repoName = System.getenv("QINIU_REPO");
        if (Strings.isNullOrEmpty(repoName)){
            this.repoName = TestConfig.LOGDB_REPO;
        }
    }

    @Test
    public void testSearchWithScroll() {
        List<MultiSearchService.SearchRequest> requestList = new ArrayList<MultiSearchService.SearchRequest>();
        MultiSearchService.SearchRequest searchRequest = new MultiSearchService.SearchRequest();
        searchRequest.repo = this.repoName;
        searchRequest.source =
                "{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\"}}],\"query\":{\"query_string\":{\"query\":\"*\"}}}";

        requestList.add(searchRequest);
        try {
            MultiSearchService.SearchResult searchResult = this.multiSearchService.search(requestList);
            Assert.assertNotNull(searchResult.requestId);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void multiSearch() throws Exception {
        // start build MultiSearchRequest
        MultiSearchRequest request = new MultiSearchRequest();
        
        // how to build single searchRequest? 
        // Doc: https://www.elastic.co/guide/en/elasticsearch/client/java-rest/5.6/java-rest-high-search.html
        // start to build first searchRequest
        SearchRequest firstSearchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        
        BoolQueryBuilder b = new BoolQueryBuilder();
        b.must(QueryBuilders.termQuery("hostname", "elastic.es.com"));
        searchSourceBuilder.query(b);
        
        firstSearchRequest.source(searchSourceBuilder);
        // using repoName as indices() input
        firstSearchRequest.indices(repoName);
        // add first request to mutlSearchRequest
        request.add(firstSearchRequest);

        // start to build second request
        SearchRequest secondSearchRequest = new SearchRequest();
        searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "nginx"));
        secondSearchRequest.source(searchSourceBuilder);
        secondSearchRequest.indices(repoName);
        // add second request to mutlSearchRequest
        request.add(secondSearchRequest);

        // do the request
        MultiSearchResponse searchResult = multiSearchService.multiSearch(request);
        // do something with searchResult

    }
}
