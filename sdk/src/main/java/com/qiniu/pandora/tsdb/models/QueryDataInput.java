package com.qiniu.pandora.tsdb.models;

public class QueryDataInput {
    private String repoName;
    private String sql;

    public QueryDataInput(String repoName, String sql) {
        this.repoName = repoName;
        this.sql = sql;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
