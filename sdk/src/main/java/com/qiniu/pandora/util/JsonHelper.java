package com.qiniu.pandora.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonHelper {

  public static final ObjectMapper mapper = new ObjectMapper();
  private static final Logger logger = LogManager.getLogger(JsonHelper.class);

  static {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static ObjectMapper getMapperWithMixIn(Class<?> target, Class<?> mixinSource) {
    return new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .addMixIn(target, mixinSource);
  }

  public static <T> List<T> readList(String json, Class<T> tClass) throws IOException {
    List<T> result = null;
    try {
      JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, tClass);
      result = mapper.readValue(json, javaType);
    } catch (JsonMappingException | JsonParseException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    return result;
  }

  public static String writeValueAsString(Object value) {
    try {
      return JsonHelper.mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      logger.warn(e);
    }
    return null;
  }

  public static String writeValueAsString(Object value, Class<?> mixinSource) {
    try {
      return JsonHelper.getMapperWithMixIn(Object.class, mixinSource).writeValueAsString(value);
    } catch (JsonProcessingException e) {
      logger.warn(e);
    }
    return null;
  }

  public static byte[] writeValueAsBytes(Object value) {
    try {
      return JsonHelper.mapper.writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      logger.warn(e);
    }
    return new byte[0];
  }

  public static byte[] writeValueAsBytes(Object value, Class<?> mixinSource) {
    try {
      return JsonHelper.getMapperWithMixIn(Object.class, mixinSource).writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      logger.warn(e);
    }
    return new byte[0];
  }

  public static Map<String, Object> readValueAsMap(byte[] bytes) {
    try {
      return mapper.readValue(bytes, Map.class);
    } catch (IOException e) {
      logger.warn(e);
    }
    return null;
  }

  public static <T> T readValue(Class<T> tClass, String json) {
    try {
      return mapper.readValue(json, tClass);
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e);
    }
    return null;
  }

  public static <T> T readValue(Class<T> tClass, byte[] json) {
    try {
      return mapper.readValue(json, tClass);
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e);
    }
    return null;
  }

  public static <T> T readValueWithException(Class<T> tClass, String json) {
    try {
      return mapper.readValue(json, tClass);
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e);
      throw new IllegalArgumentException("JSON parse failed, may due to invalid format");
    }
  }

  public static <T> T readValue(TypeReference<T> typeReference, String json) {
    try {
      return mapper.readValue(json, typeReference);
    } catch (IOException e) {
      logger.warn(e);
    }
    return null;
  }

  public static <T> T readValue(TypeReference<T> typeReference, byte[] json) {
    try {
      return mapper.readValue(json, typeReference);
    } catch (IOException e) {
      logger.warn(e);
    }
    return null;
  }
}
