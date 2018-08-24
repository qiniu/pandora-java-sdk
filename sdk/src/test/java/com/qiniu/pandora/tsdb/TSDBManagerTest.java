package com.qiniu.pandora.tsdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.tsdb.models.*;
import com.qiniu.pandora.util.Auth;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TSDBManagerTest {
    private static final String accessKey = "";
    private static final String secretKey = "";
    private static final String repoName = "";
    private static final String seriesName = "";

    public TSDBManager manager;

    @Before
    public void setUp() {
        Auth auth = Auth.create(accessKey, secretKey);
        PandoraClient client = new PandoraClientImpl(auth);
        manager = new TSDBManager(client, "http://tsdb-pdex.qiniu.io");
    }

    @Test
    public void test001CreateRepo() throws Exception {
        boolean isOk = manager.CreateRepo(new CreateRepoInput(repoName));

        assertTrue(isOk);
    }

    @Test
    public void test002GetRepo() throws Exception {
        GetRepoOuput output = manager.GetRepo(repoName);

        assertNotNull(output);

        System.out.println(output.getRepoName());
        System.out.println(output.getRegion());
        System.out.println(output.getCreateTime());
        System.out.println(output.getDeleting());
        System.out.println(output.getMetaData());
    }

    @Test
    public void test003ListRepos() throws Exception {
        List<RepoDesc> output = manager.ListRepo();

        assertNotNull(output);

        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i).getRepoName());
            System.out.println(output.get(i).getRegion());
            System.out.println(output.get(i).getCreateTime());
            System.out.println(output.get(i).getDeleting());
            System.out.println("---------------------------");
        }
    }

    @Test
    public void test004UpdateRepoMetadata() throws Exception {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "liangmengmeng");
        metadata.put("info", "java sdk test metadata");

        boolean isOk = manager.UpdateRepoMetadata(repoName, metadata);

        assertTrue(isOk);
    }

    @Test
    public void test005CreateSeries() throws Exception {
        CreateSeriesInput input = new CreateSeriesInput(repoName, seriesName);

        boolean isOk = manager.CreateSeries(input);

        assertTrue(isOk);
    }

    @Test
    public void test006ListSeries() throws Exception {
        List<SeriesDesc> output = manager.ListSeries(repoName);

        assertNotNull(output);

        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i).getSeriesName());
            System.out.println(output.get(i).getCreateTime());
            System.out.println(output.get(i).getRetention());
            System.out.println(output.get(i).getType());
            System.out.println(output.get(i).getDeleting());
            System.out.println("------------------------");
        }
    }

    @Test
    public void test007UpdateSeriesMetadata() throws Exception {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "liangmengmeng");
        metadata.put("desc", "this is test java sdk to update series metadata");

        boolean isOk = manager.UpdateSeriesMetadata(repoName, seriesName, metadata);

        assertTrue(isOk);
    }

    @Test
    public void test008QueryData() throws Exception {
        String sql = "select * from " + seriesName;
        QueryDataInput input = new QueryDataInput(repoName, sql);

        QueryDataOutput output = manager.QueryPoint(input);

        assertNotNull(output);
    }

    @Test
    public void test009DeleteSeriesMetadata() throws Exception {
        boolean isOk = manager.DeleteSeriesMetadata(repoName, seriesName);

        assertTrue(isOk);
    }

    @Test
    public void test010DeleteSeries() throws Exception {
        boolean isOk = manager.DeleteSeries(repoName, seriesName);

        assertTrue(isOk);
    }

    @Test
    public void test011DeleteRepoMetadata() throws Exception {
        boolean isOk = manager.DeleteRepoMetadata(repoName);

        assertTrue(isOk);
    }

    @Test
    public void test012DeleteRepo() throws Exception {
        boolean isOk = manager.DeleteRepo(repoName);

        assertTrue(isOk);
    }

}