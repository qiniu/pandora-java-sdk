package com.qiniu.pandora.pipeline.logdb;
import com.qiniu.pandora.logdb.PartialSearchService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class PartialSearchTest extends LogDBTest{
    @Test
    public void source() throws Exception{
        PartialSearchService partialSearchService = logDBClient.NewPartialSearchService();
        partialSearchService = partialSearchService.setSize(1)
                .setQueryString("*")
                .setRepo(super.repo0)
                .setSort("timestamp")
                .setStartTime(Long.valueOf("1495261354595"))
                .setEndTime(Long.valueOf("1497853354595"))
                .setPre_tag("@hello@")
                .setPost_tag("@hello/@");
        Method sourceMethod = partialSearchService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        Object source = sourceMethod.invoke(partialSearchService);
        String expectedSource = "{\"query_String\":\"*\",\"sort\":\"timestamp\",\"size\":1,\"startTime\":1495261354595,\"endTime\":1497853354595,\"searchType\":1,\"highlight\":{\"pre_tag\":\"@hello@\",\"post_tag\":\"@hello/@\"}}";
        Assert.assertEquals(source,expectedSource);

        partialSearchService.reset();

        partialSearchService = partialSearchService.setSize(1)
                .setQueryString("*")
                .setRepo(super.repo0)
                .setSort("dd")
                .setStartTime(Long.valueOf("1495261354595"))
                .setEndTime(Long.valueOf("1497853354595"))
                .setPre_tag("@aa@")
                .setPost_tag("@aa/@");
        sourceMethod = partialSearchService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        source = sourceMethod.invoke(partialSearchService);
        expectedSource = "{\"query_String\":\"*\",\"sort\":\"dd\",\"size\":1,\"startTime\":1495261354595,\"endTime\":1497853354595,\"searchType\":1,\"highlight\":{\"pre_tag\":\"@aa@\",\"post_tag\":\"@aa/@\"}}";
        Assert.assertEquals(source,expectedSource);
    }
    @Test
    public void url() throws Exception{
        PartialSearchService partialSearchService = logDBClient.NewPartialSearchService();

        Method urlMethod = partialSearchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        Object url = urlMethod.invoke(partialSearchService);
        String expectedUrl = "https://logdb.qiniu.com/v5/repos/null/s";
        Assert.assertEquals(url,expectedUrl);

        partialSearchService.setRepo(super.repo0);
        urlMethod = partialSearchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        url = urlMethod.invoke(partialSearchService);
        expectedUrl = "https://logdb.qiniu.com/v5/repos/repo0/s";
        Assert.assertEquals(url,expectedUrl);


        logDBClient.setHost("https://test.com");
        partialSearchService = logDBClient.NewPartialSearchService();
        urlMethod = partialSearchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        url = urlMethod.invoke(partialSearchService);
        expectedUrl = "https://test.com/v5/repos/null/s";
        Assert.assertEquals(url,expectedUrl);

        logDBClient.setHost("https://logdb.qiniu.com");
    }
}
