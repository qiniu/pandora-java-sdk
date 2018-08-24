package com.qiniu.pandora.pipeline.logdb;

import com.qiniu.pandora.logdb.SearchService;
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Method;

/**
 * Created by tuo on 2017/6/19.
 */
public class SearchServiceTest extends LogDBTest{
    @Test
    public void source() throws Exception{
        SearchService searchService = logDBClient.NewSearchService();
        searchService = searchService.setFrom(0).setSize(1).
                setQuerystring("*").setRepo(super.repo0);
        Method sourceMethod = searchService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        Object source = sourceMethod.invoke(searchService);
        String expectedSource = "{\"query\":\"*\",\"from\":0,\"size\":1}";
        Assert.assertEquals(source,expectedSource);

        searchService.reset();

        searchService = searchService.setFrom(1).setSize(2).
                setQuerystring("field:val1").setRepo(super.repo1);

        sourceMethod = searchService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        source = sourceMethod.invoke(searchService);
        expectedSource = "{\"query\":\"field:val1\",\"from\":1,\"size\":2}";
        Assert.assertEquals(source,expectedSource);
    }
    @Test
    public void url() throws Exception{
        SearchService searchService = super.logDBClient.NewSearchService();

        Method urlMethod = searchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        Object url = urlMethod.invoke(searchService);
        String expectedUrl = "https://logdb.qiniu.com/v5/repos/null/search";
        Assert.assertEquals(url,expectedUrl);

        searchService.setRepo(super.repo0);
        urlMethod = searchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        url = urlMethod.invoke(searchService);
        expectedUrl = "https://logdb.qiniu.com/v5/repos/repo0/search";
        Assert.assertEquals(url,expectedUrl);

        logDBClient.setHost("https://test.com");
        searchService = logDBClient.NewSearchService();
        urlMethod = searchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        url = urlMethod.invoke(searchService);
        expectedUrl = "https://test.com/v5/repos/null/search";
        Assert.assertEquals(url,expectedUrl);

        logDBClient.setHost("https://logdb.qiniu.com");
    }
}
