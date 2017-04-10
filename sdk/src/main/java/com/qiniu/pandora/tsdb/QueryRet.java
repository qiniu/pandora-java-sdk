package com.qiniu.pandora.tsdb;

import com.google.gson.Gson;
import com.qiniu.pandora.util.Json;

import java.util.*;

/**
 * TSDB 的查询返回结果
 */
public class QueryRet {
    private List<Result> results = Collections.emptyList();

    public List<Result> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return Json.encode(this);
    }

    public <T> List<T> toList(Class<T> classOfT) {
        List<T> rs = new ArrayList<>();
        for (Result r : results) {
            rs.addAll(r.toList(classOfT));
        }
        return rs;
    }

    /**
     * 每条SQL对应一个查询结果
     */
    public static class Result {
        private List<Serie> series = Collections.emptyList();

        public List<Serie> getSeries() {
            return series;
        }

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }

        public <T> List<T> toList(Class<T> classOfT) {
            List<T> results = new ArrayList<>();
            for (Serie s : series) {
                results.addAll(s.toList(classOfT));
            }
            return results;
        }

    }

    /**
     * 每个tag group对应一个Serie
     */
    public static class Serie {
        /**
         * 序列名字
         */
        private String name;
        /**
         * 当sql中不含有group by 条件的时候该字段为空，反之则包含所有tag字段
         */
        private Map<String, String> tags = Collections.emptyMap();
        /**
         * 当sql中不含有group by 条件的时候，包含所有列
         * 如果含有group by 条件， 则包含除了tag 的所有列
         */
        private List<String> columns = Collections.emptyList();
        /**
         * 所有数据值，和columns字段中一一对应
         */
        private List<Row> values = Collections.emptyList();

        public String getName() {
            return name;
        }

        public Map<String, String> getTags() {
            return tags;
        }

        public List<String> getColumns() {
            return columns;
        }

        public List<Row> getValues() {
            return values;
        }

        public String getTag(String tagKey) {
            if (tags.containsKey(tagKey)) {
                return tags.get(tagKey);
            }
            return null;
        }

        @Override
        public String toString() {
            return Json.encode(this);
        }

        public <T> List<T> toList(Class<T> classOfT) {
            List<T> ret = new ArrayList<>();
            Map<String, Object> valMap = new HashMap<>();
            for (Map.Entry<String, String> tag : tags.entrySet()) {
                valMap.put(tag.getKey(), tag.getValue());
            }
            int colSize = getColumns().size();
            for (Row r : getValues()) {
                for (int i = 0; i < colSize; i++) {
                    valMap.put(getColumns().get(i), r.get(i));
                }
                T obj = Json.encodeMap(valMap, classOfT);
                ret.add(obj);
            }
            return ret;
        }

    }

    public static class Row extends ArrayList<Object> {
        @Override
        public String toString() {
            return Json.encode(this);
        }
    }

}
