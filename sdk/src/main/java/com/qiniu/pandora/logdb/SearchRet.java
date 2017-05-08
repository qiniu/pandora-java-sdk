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
     * @return 是否部分命中
     */
    public boolean isPartialSuccess() {
        return partialSuccess;
    }

    /**
     * 返回命中总数
     *
     * @return 命中总数
     */
    public long getTotal() {
        return total;
    }

    /**
     * 返回所有数据行
     * @return 所有数据行
     */
    public List<Row> getData() {
        return data;
    }

    public static class Row extends LinkedHashMap<String, Object> {
    }

    /**
     * 根据指定Class 类型将行转为具体对象
     *
     * @param cls 对象Class类型
     * @param <T> Class具体类型
     * @return 转为指定类型的数组
     */
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
