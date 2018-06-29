package com.qiniu.pandora.logdb;

import com.qiniu.pandora.common.*;
import com.qiniu.pandora.logdb.repo.*;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.util.Auth;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LogDBTest {
    protected LogDBClient logDBClient;

    @Before
    public void setUp() throws Exception {
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        this.logDBClient = new LogDBClient(client);
    }

    /**
     * create -> get -> update -> get -> delete
     */
    @Test
    public void testManageRepo() {
        CreateRepoInput createRepoInput = new CreateRepoInput();
        createRepoInput.region = Constants.region;
        createRepoInput.retention = "30d";
        createRepoInput.description = "this is a sdk test repo";
        createRepoInput.schema = new RepoSchemaEntry[]{
                new RepoSchemaEntry("timestamp", ValueType.TypeDate),
                new RepoSchemaEntry("sensor", ValueType.TypeString, Analyzer.StandardAnalyzer),
                new RepoSchemaEntry("value", ValueType.TypeLong)
        };

        String randomRepoName = String.format("sdkrepo%s", (int) (Math.random() * 100));
        //create
        try {
            this.logDBClient.createRepo(randomRepoName, createRepoInput);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }

        //get
        try {
            GetRepoOutput repoOutput = this.logDBClient.getRepo(randomRepoName);
            Assert.assertEquals(createRepoInput.description, repoOutput.description);

            System.out.println(repoOutput.toString());
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }

        //update
        UpdateRepoInput updateRepoInput = new UpdateRepoInput();
        updateRepoInput.retention = "10d";
        updateRepoInput.description = "this is a sdk test repo(update)";
        updateRepoInput.schema = new RepoSchemaEntry[]{
                new RepoSchemaEntry("timestamp", ValueType.TypeDate),
                new RepoSchemaEntry("sensor", ValueType.TypeString, Analyzer.StandardAnalyzer),
                new RepoSchemaEntry("value", ValueType.TypeLong),
                new RepoSchemaEntry("alias", ValueType.TypeString, Analyzer.StandardAnalyzer)
        };
        try {
            this.logDBClient.updateRepo(randomRepoName, updateRepoInput);
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        //get
        try {
            GetRepoOutput repoOutput = this.logDBClient.getRepo(randomRepoName);
            Assert.assertEquals(updateRepoInput.description, repoOutput.description);

            System.out.println(repoOutput.toString());
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }

        //delete
        try {
            this.logDBClient.deleteRepo(randomRepoName);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testListRepo() {
        try {
            ListRepoOutput output = this.logDBClient.listRepos();
            Assert.assertTrue(output.repos.length > 0);
            for (RepoDesc desc : output.repos) {
                System.out.println(desc.toString());
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}