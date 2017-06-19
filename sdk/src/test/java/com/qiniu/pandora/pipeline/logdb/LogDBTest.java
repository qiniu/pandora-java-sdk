package com.qiniu.pandora.pipeline.logdb;

import com.qiniu.pandora.logdb.LogDBClient;
import org.junit.Before;

public class LogDBTest {
    protected LogDBClient logDBClient;
    protected String repo0 = "repo0";
    protected String repo1 = "repo1";
    @Before
    public void setUp() throws Exception {
        String ak = "testak";
        String sk = "testsk";
        logDBClient = LogDBClient.NewLogDBClient(ak,sk);
    }
}