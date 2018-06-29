package com.qiniu.pandora.tsdb;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.tsdb.query.QueryDataInput;
import com.qiniu.pandora.tsdb.query.QueryDataOutput;
import com.qiniu.pandora.tsdb.repo.*;
import com.qiniu.pandora.util.Auth;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TSDBClientTest {
    private static final String accessKey = TestConfig.ACCESS_KEY;
    private static final String secretKey = TestConfig.SECRET_KEY;
    private static final String repoName = TestConfig.TSDB_REPO;
    private static final String seriesName = TestConfig.TSDB_SERIES;

    public TSDBClient manager;

    @Before
    public void setUp() {
        Auth auth = Auth.create(accessKey, secretKey);
        PandoraClient client = new PandoraClientImpl(auth);
        manager = new TSDBClient(client);
    }

    @Test
    public void test001CreateRepo() throws Exception {
        CreateRepoInput input = new CreateRepoInput();
        input.region = "nb";
        manager.createRepo(repoName, input);
    }

    @Test
    public void test002GetRepo() throws Exception {
        GetRepoOuput output = manager.getRepo(repoName);

        assertNotNull(output);

        System.out.println(output.repoName);
        System.out.println(output.region);
        System.out.println(output.createTime);
        System.out.println(output.deleting);
        System.out.println(output.metaData);
    }

    @Test
    public void test003ListRepos() throws Exception {
        RepoDesc[] output = manager.listRepo();

        assertNotNull(output);

        for (int i = 0; i < output.length; i++) {
            System.out.println(output[i].repoName);
            System.out.println(output[i].region);
            System.out.println(output[i].createTime);
            System.out.println(output[i].deleting);
            System.out.println("---------------------------");
        }
    }

    @Test
    public void test004UpdateRepoMetadata() throws Exception {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "liangmengmeng");
        metadata.put("info", "java sdk test metadata");

        manager.updateRepoMetadata(repoName, metadata);
    }

    @Test
    public void test005CreateSeries() throws Exception {
        CreateSeriesInput input = new CreateSeriesInput();
        input.retention = "oneDay";

        manager.createSeries(repoName, seriesName, input);
    }

    @Test
    public void test006ListSeries() throws Exception {
        SeriesDesc[] output = manager.listSeries(repoName);

        assertNotNull(output);

        for (int i = 0; i < output.length; i++) {
            System.out.println(output[i].seriesName);
            System.out.println(output[i].createTime);
            System.out.println(output[i].retention);
            System.out.println(output[i].type);
            System.out.println(output[i].deleting);
            System.out.println("------------------------");
        }
    }

    @Test
    public void test007UpdateSeriesMetadata() throws Exception {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "liangmengmeng");
        metadata.put("desc", "this is test java sdk to update series metadata");
        manager.updateSeriesMetadata(repoName, seriesName, metadata);
    }

    @Test
    public void test008QueryData() throws Exception {
        String sql = "select * from " + seriesName;
        QueryDataInput input = new QueryDataInput();
        input.sql = sql;

        QueryDataOutput output = manager.queryPoint(repoName, input);

        assertNotNull(output);
    }

    @Test
    public void test009DeleteSeriesMetadata() throws Exception {
        manager.deleteSeriesMetadata(repoName, seriesName);
    }

    @Test
    public void test010DeleteSeries() throws Exception {
        manager.deleteSeries(repoName, seriesName);
    }

    @Test
    public void test011DeleteRepoMetadata() throws Exception {
        manager.deleteRepoMetadata(repoName);
    }

    @Test
    public void test012DeleteRepo() throws Exception {
        manager.deleteRepo(repoName);
    }
}