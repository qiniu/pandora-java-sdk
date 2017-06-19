package com.qiniu.pandora.pipeline.logdb;
import com.qiniu.pandora.logdb.ScrollService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class ScrollTest extends LogDBTest{
    @Test
    public void source() throws Exception{
        ScrollService scrollService = logDBClient.NewScrollService();
        scrollService = scrollService.setScroll("1m").setScroll_id("d");
        Method sourceMethod = scrollService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        Object source = sourceMethod.invoke(scrollService);
        String expectedSource = "{\"scroll\":\"1m\",\"scroll_id\":\"d\"}";
        Assert.assertEquals(source,expectedSource);

        scrollService.reset();

        scrollService = scrollService.setScroll("2m").setScroll_id("f");

        sourceMethod = scrollService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        source = sourceMethod.invoke(scrollService);
        expectedSource = "{\"scroll\":\"2m\",\"scroll_id\":\"f\"}";
        Assert.assertEquals(source,expectedSource);
    }
    @Test
    public void url() throws Exception{
        ScrollService scrollService = logDBClient.NewScrollService();

        Method urlMethod = scrollService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        Object url = urlMethod.invoke(scrollService);
        String expectedUrl = "https://logdb.qiniu.com/v5/scroll";
        Assert.assertEquals(url,expectedUrl);

        logDBClient.setHost("https://test.com");
        scrollService = logDBClient.NewScrollService();
        urlMethod = scrollService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        url = urlMethod.invoke(scrollService);
        expectedUrl = "https://test.com/v5/scroll";
        Assert.assertEquals(url,expectedUrl);

        logDBClient.setHost("https://logdb.qiniu.com");
    }
}
