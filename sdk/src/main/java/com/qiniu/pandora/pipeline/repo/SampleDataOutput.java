package com.qiniu.pandora.pipeline.repo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Map;

public class SampleDataOutput {

    @SerializedName("records")
    public Map<String,Object>[] values;

    @Override
    public String toString() {
        return "SampleDataOutput{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
