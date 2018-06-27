package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

public class CreateJobInput {
    @SerializedName("-")
    public String jobname;
    @SerializedName("srcs")
    public JobSrc[] srcs;
    @SerializedName("Computation")
    public Computation computation;
    @SerializedName("container")
    public Container container;
    @SerializedName("scheduler")
    public JobScheduler scheduler;
    @SerializedName("params")
    public Param[] params;

}
