package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.logdb.search.PartialSearchService;
import com.qiniu.pandora.util.Auth;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PartialSearchTest {

    private PartialSearchService partialSearchService;
    private String repoName;

    @Before
    public void setUp() {
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        LogDBClient logDBClient = new LogDBClient(client);
        this.partialSearchService = logDBClient.NewPartialSearchService();
        this.repoName = TestConfig.LOGDB_REPO;
    }

    @Test
    public void testSearchWithPartial() {
        int maxSize = 10;
        PartialSearchService.SearchRequest request = new PartialSearchService.SearchRequest();
        request.queryString = "level: WARN";
        request.size = maxSize;
        request.sort = "timestamp";
        request.endTime = 1529653974002L;
        request.startTime = 1529653968692L;
        try {
            PartialSearchService.SearchResult result = this.partialSearchService.search(repoName, request);
            Assert.assertNotNull(result.requestId);
            System.out.println(result.requestId);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
