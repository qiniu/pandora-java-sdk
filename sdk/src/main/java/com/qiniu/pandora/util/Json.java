package com.qiniu.pandora.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;


public final class Json {
    private static final Gson gson = new Gson();

    private Json() {
    }

    public static String encode(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T decode(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static StringMap decode(String json) {
        // CHECKSTYLE:OFF
        Type t = new TypeToken<Map<String, Object>>() {
        }.getType();
        // CHECKSTYLE:ON
        Map<String, Object> x = gson.fromJson(json, t);
        return new StringMap(x);
    }

    public static <T> T encodeMap(Map<String, Object> map, Class<T> classOfT) {
        String json = gson.toJson(map);
        return gson.fromJson(json, classOfT);
    }
}
