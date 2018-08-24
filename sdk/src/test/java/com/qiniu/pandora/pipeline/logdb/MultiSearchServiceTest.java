package com.qiniu.pandora.pipeline.logdb;

import com.qiniu.pandora.logdb.MultiSearchService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class MultiSearchServiceTest extends LogDBTest{
    @Test
    public void Source() throws Exception{
        MultiSearchService multiSearchService = logDBClient.NewMultiSearchService();

        MultiSearchService.SearchRequest searchRequest1 = new MultiSearchService.SearchRequest("{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647},\"aggs\":{\"2\":{\"date_histogram\":{\"field\":\"timestamp\",\"interval\":\"60s\",\"time_zone\":\"Asia/Shanghai\",\"min_doc_count\":0,\"extended_bounds\":{\"min\":1496644491486,\"max\":1496644791486}}}},\"fields\":[\"*\",\"_source\"],\"script_fields\":{},\"fielddata_fields\":[\"timestamp\"]}"
                , super.repo0);
        MultiSearchService.SearchRequest searchRequest2 = new MultiSearchService.SearchRequest("{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647}}"
                , super.repo1);
        multiSearchService = multiSearchService
                .add(searchRequest1)
                .add(searchRequest2);

        Method sourceMethod = multiSearchService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        Object source = sourceMethod.invoke(multiSearchService);
        String expectedSource = "{\"index\":[\"repo0\"]}\n" +
                "{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647},\"aggs\":{\"2\":{\"date_histogram\":{\"field\":\"timestamp\",\"interval\":\"60s\",\"time_zone\":\"Asia/Shanghai\",\"min_doc_count\":0,\"extended_bounds\":{\"min\":1496644491486,\"max\":1496644791486}}}},\"fields\":[\"*\",\"_source\"],\"script_fields\":{},\"fielddata_fields\":[\"timestamp\"]}\n" +
                "{\"index\":[\"repo1\"]}\n" +
                "{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647}}\n";
        Assert.assertEquals(source,expectedSource);

        Method urlMethod = multiSearchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        Object url = urlMethod.invoke(multiSearchService);
        String expectedUrl = "https://logdb.qiniu.com/v5/logdbkibana/msearch";
        Assert.assertEquals(url,expectedUrl);

        multiSearchService.reset();
        multiSearchService = multiSearchService
                .add(searchRequest1);
        sourceMethod = multiSearchService.getClass().getDeclaredMethod("source");
        sourceMethod.setAccessible(true);
        source = sourceMethod.invoke(multiSearchService);
        expectedSource = "{\"index\":[\"repo0\"]}\n" +
                "{\"size\":1,\"sort\":[{\"timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"Appid: 1380665431\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp\":{\"gte\":1496644491486,\"lte\":1496644791486,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647},\"aggs\":{\"2\":{\"date_histogram\":{\"field\":\"timestamp\",\"interval\":\"60s\",\"time_zone\":\"Asia/Shanghai\",\"min_doc_count\":0,\"extended_bounds\":{\"min\":1496644491486,\"max\":1496644791486}}}},\"fields\":[\"*\",\"_source\"],\"script_fields\":{},\"fielddata_fields\":[\"timestamp\"]}\n";
        Assert.assertEquals(source,expectedSource);

        urlMethod = multiSearchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        url = urlMethod.invoke(multiSearchService);
        expectedUrl = "https://logdb.qiniu.com/v5/logdbkibana/msearch";
        Assert.assertEquals(url,expectedUrl);

    }
    @Test
    public void url() throws Exception{
        MultiSearchService multiSearchService = logDBClient.NewMultiSearchService();

        Method urlMethod = multiSearchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
        Object url = urlMethod.invoke(multiSearchService);
        String expectedUrl = "https://logdb.qiniu.com/v5/logdbkibana/msearch";
        Assert.assertEquals(url,expectedUrl);

        logDBClient.setHost("https://test.com");
        multiSearchService = logDBClient.NewMultiSearchService();
        urlMethod = multiSearchService.getClass().getDeclaredMethod("url");
        urlMethod.setAccessible(true);
         url = urlMethod.invoke(multiSearchService);
        expectedUrl = "https://test.com/v5/logdbkibana/msearch";
        Assert.assertEquals(url,expectedUrl);

        logDBClient.setHost("https://logdb.qiniu.com");
    }
}
