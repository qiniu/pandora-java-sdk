package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class JobScheduler {
    @SerializedName("type")
    public String type;
    @SerializedName("spec")
    public JobSchedulerSpec spec;
}