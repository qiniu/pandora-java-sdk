package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class JobSchedulerSpec {
    @SerializedName("crontab")
    public String crontab;
    @SerializedName("loop")
    public String loop;
}
