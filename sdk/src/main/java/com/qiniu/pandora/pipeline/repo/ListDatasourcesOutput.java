package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;


public class ListDatasourcesOutput {
    @SerializedName("datasources")
    public DatasourceDesc[] datasources;
}
