package com.qiniu.pandora.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DocUtil {

  // 改变 document 根据 key 指定的元素的值，例如 { "A1": {"B1": "Value1", "B2": "Value2"...},  } ，
  // key 为 A1.B1 value 为 "CCCC"，则 document 变为 { "A1": {"B1": "Value1", "B2": "Value2"...},  }
  // 如果key指定的是数组，且数组中的每个元素仍然是Map类型，则会将该数组所有的元素都进行修改
  public static void changeField(Map<String, Object> document, String key, Object value) {
    if (StringUtils.isNullOrEmpty(key)) {
      return;
    }

    String[] keys = key.split("\\.");
    changeField(document, keys, value);
  }

  public static void changeField(Map<String, Object> document, String[] keys, Object value)
      throws RuntimeException {
    if (keys == null || keys.length == 0) {
      return;
    }

    if (keys.length == 1) {
      document.put(keys[0], value);
    }
    Object temp = document.get(keys[0]);
    if (temp instanceof Map) {
      changeField((Map) temp, Arrays.copyOfRange(keys, 1, keys.length), value);
    }

    if (temp instanceof List) {
      for (Object obj : (List) temp) {
        if (obj instanceof Map) {
          changeField((Map) obj, Arrays.copyOfRange(keys, 1, keys.length), value);
        }
      }
    }
  }

  // 例如 object 为 { "A1": {"B1": "Value1", "B2": "Value2"...},  } key 为 A1.B1，value 为Value1 则相等，
  // 根据key 找到 object 对应的 value，进行比较
  public static boolean compareObject(Object object, String key, Object value) {
    if (StringUtils.isNullOrEmpty(key)) {
      return false;
    }
    return compareObject(object, key.split("\\."), value);
  }

  // 比较 Map 的object，内部按照keys去递归寻找的值得，是不是和value相等
  public static boolean compareObject(Object object, String[] keys, Object value) {
    // 这种情况比较无意义，返回false
    if (object == null || value == null || keys == null) {
      return false;
    }

    if (keys.length == 0) {
      return object.equals(value);
    }

    if (object instanceof Map) {
      Map temp = (Map) object;
      if (temp.containsKey(keys[0])) {
        Object v = temp.get(keys[0]);
        if (keys.length == 1) {
          return value.equals(v);
        }
        return compareObject(v, Arrays.copyOfRange(keys, 1, keys.length), value);
      }
    }

    return false;
  }

  // 在 document 中找到 key 指定的数组，例如 { "A1": {"B1": "Value1", "B2": "Value2", "B3" :[{"C1":"CCC",...}...]...},  }
  // 则 A1.B3 返回 [{"C1":"CCC",...}...]，如果 key 在 document 中不能递归直达，或者找到的元素不是数组则返回空
  public static List<Object> findArray(Map<String, Object> document, String key) {
    if (StringUtils.isNullOrEmpty(key)) {
      return null;
    }
    return findArray(document, key.split("\\."));
  }

  public static List<Object> findArray(Map<String, Object> document, String[] keys) {
    if (keys == null || keys.length == 0) {
      return null;
    }

    if (!document.containsKey(keys[0])) {
      return null;
    }

    Object temp = document.get(keys[0]);

    if (keys.length == 1) {
      if (temp instanceof List) {
        return (List) temp;
      }
      return null;
    }

    if (temp instanceof Map) {
      return findArray((Map) temp, Arrays.copyOfRange(keys, 1, keys.length));
    }
    return null;
  }
}
