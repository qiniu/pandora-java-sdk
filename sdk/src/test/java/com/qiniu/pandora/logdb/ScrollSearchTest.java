package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.logdb.search.ScrollSearchService;
import com.qiniu.pandora.logdb.search.SearchService;
import com.qiniu.pandora.util.Auth;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScrollSearchTest extends LogDBTest {

    private SearchService searchService;
    private ScrollSearchService scrollSearchService;
    private String repoName;

    @Before
    public void setUp() {
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        LogDBClient logDBClient = new LogDBClient(client);
        this.searchService = logDBClient.NewSearchService();
        this.scrollSearchService = logDBClient.NewScrollSearchService();
        this.repoName = TestConfig.LOGDB_REPO;
    }

    @Test
    public void testSearchWithScroll() {
        SearchService.SearchRequest searchRequest = new SearchService.SearchRequest();
        int maxSize = 7;
        String scroll = "3m";

        searchRequest.query = "level: WARN";
        searchRequest.size = maxSize;
        searchRequest.scroll = scroll; //hold for 3 minutes
        try {
            SearchService.SearchResult searchResult = this.searchService.search(repoName, searchRequest);
            Assert.assertTrue(searchResult.total > 0);
            Assert.assertTrue(searchResult.data.size() == maxSize);
            Assert.assertNotNull(searchResult.requestId);
            Assert.assertNotNull(searchResult.scrollID);
            System.out.println("total:" + searchResult.total + ", current: " + searchResult.data.size());
            System.out.println("scroll ID:" + searchResult.scrollID);

            //use scrollID to query the remained data
            String scrollID = searchResult.scrollID;
            searchResult = scrollSearchService.scroll(repoName, scroll, scrollID);
            Assert.assertNotNull(searchResult.requestId);
            System.out.println("scroll next:" + searchRequest.size);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
