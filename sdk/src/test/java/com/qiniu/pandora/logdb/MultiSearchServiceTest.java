package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.logdb.search.MultiSearchService;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.util.Auth;
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
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        LogDBClient logDBClient = new LogDBClient(client);
        this.multiSearchService = logDBClient.NewMultiSearchService();
        this.repoName = TestConfig.LOGDB_REPO;
    }

    @Test
    public void testSearchWithScroll() {
        List<MultiSearchService.MultiSearchRequest> requestList = new ArrayList<MultiSearchService.MultiSearchRequest>();
        MultiSearchService.MultiSearchRequest searchRequest = new MultiSearchService.MultiSearchRequest();
        searchRequest.repo = this.repoName;
        searchRequest.source =
                "{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\"}}],\"query\":{\"query_string\":{\"query\":\"*\"}}}";

        requestList.add(searchRequest);
        try {
            MultiSearchService.MultiSearchResult searchResult = this.multiSearchService.search(requestList);
            Assert.assertNotNull(searchResult.requestId);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
