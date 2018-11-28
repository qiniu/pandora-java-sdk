package update;

import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.util.Json;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectPoint extends Point {

  private List<Field> fields = new ArrayList<Field>();
  private int size;

  public static ObjectPoint fromPointMap(Map<String, Object> map) {
    ObjectPoint p = new ObjectPoint();
    for (String key : map.keySet()) {
      if (map.get(key) instanceof List) {
        List l = new ArrayList();
        l.addAll((List) map.get(key));
        p.append(key, l);
      } else if (map.get(key) instanceof Map) {
        Map m = new HashMap();
        m.putAll((Map) map.get(key));
        p.append(key, m);
      } else {
        p.append(new Field(key, map.get(key)));
      }
    }
    return p;
  }

  public <K, V> void append(String key, Map<K, V> value) {
    Field field = new Field(key, Json.encode(value));
    append(field);
  }

  public <V> void append(String key, List<V> value) {
    Field field = new Field(key, Json.encode(value));
    append(field);
  }

  public String toString() {
    StringBuilder buff = new StringBuilder();
    for (int i = 0; i < fields.size(); i++) {
      buff.append(String.valueOf(fields.get(i)));
      if (i == fields.size() - 1) {
        buff.append("\n");
      } else {
        buff.append("\t");
      }
    }
    return buff.toString();
  }

  private void append(Field f) {
    fields.add(f);
    size += f.getSize() + 1; // 包含字段本身和后缀的分隔符 \t \n
  }

  public int getSize() {
    return size;
  }
}
