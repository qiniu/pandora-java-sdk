package com.qiniu.pandora.logdb;

import com.qiniu.pandora.util.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 查询logd的原始结果.
 */
public class SearchRet {

    private long total;
    private boolean partialSuccess;
    private List<Row> data;

    /**
     * 如果该字段为true，代表这次查询提前结束，只返回了部分命中结果
     *
     * @return
     */
    public boolean isPartialSuccess() {
        return partialSuccess;
    }

    public long getTotal() {
        return total;
    }

    public List<Row> getData() {
        return data;
    }

    public static class Row extends LinkedHashMap<String, Object> {
    }

    public <T> List<T> toList(Class<T> cls) {
        List<T> ret = new ArrayList<>();
        for (Row r : data) {
            ret.add(Json.encodeMap(r, cls));
        }
        return ret;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SearchRet{");
        sb.append("total=").append(total);
        sb.append(", partialSuccess=").append(partialSuccess);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
