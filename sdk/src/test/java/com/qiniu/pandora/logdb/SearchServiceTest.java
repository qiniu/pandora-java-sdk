package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.logdb.search.SearchService;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.util.Auth;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by tuo on 2017/6/19.
 */
public class SearchServiceTest {

    private SearchService searchService;
    private String repoName;

    @Before
    public void setUp() {
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        LogDBClient logDBClient = new LogDBClient(client);
        this.searchService = logDBClient.NewSearchService();
        this.repoName = TestConfig.LOGDB_REPO;
    }

    @Test
    public void testSearchWithQuery() {
        SearchService.SearchRequest searchRequest = new SearchService.SearchRequest();
        searchRequest.query = "level: WARN";
        try {
            SearchService.SearchResult searchResult = this.searchService.search(repoName, searchRequest);
            Assert.assertTrue(searchResult.total > 0);
            Assert.assertNotNull(searchResult.requestId);
            System.out.println("total:" + searchResult.total + ", current: " + searchResult.data.size());
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testSearchWithSize() {
        SearchService.SearchRequest searchRequest = new SearchService.SearchRequest();
        int maxSize = 7;
        searchRequest.query = "level: WARN";
        searchRequest.size = maxSize;
        try {
            SearchService.SearchResult searchResult = this.searchService.search(repoName, searchRequest);
            Assert.assertTrue(searchResult.total > 0);
            Assert.assertTrue(searchResult.data.size() == maxSize);
            Assert.assertNotNull(searchResult.requestId);
            System.out.println("total:" + searchResult.total + ", current: " + searchResult.data.size());
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


}
