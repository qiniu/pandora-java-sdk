package com.qiniu.pandora.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocUtilTest {

  Map<String, Object> document = new HashMap();
  List<Object> list = new ArrayList<>();

  @Before
  public void setup() {
    document.put("A1", 1111);

    Map<String, Object> map = new HashMap();
    map.put("B1", 22222);
    map.put("B2", "BBBBB");
    Map<String, Object> mapB = new HashMap();
    mapB.put("C1", 33333);
    mapB.put("C2", "CCCCCC");
    map.put("B3", mapB);
    document.put("A2", map);

    Map<String, Object> m1 = new HashMap();
    m1.put("L1", "111");
    m1.put("L2", 111);
    m1.put("L3", "aaa");
    m1.put("L4", "AAA");
    Map<String, Object> m2 = new HashMap();
    m2.put("L1", "222");
    m2.put("L2", 222);
    m2.put("L3", "bbb");
    m2.put("L4", "BBB");
    Map<String, Object> m3 = new HashMap();
    m3.put("L1", "333");
    m3.put("L2", 333);
    m3.put("L3", "ccc");
    m3.put("L4", "CCC");
    Map<String, Object> m4 = new HashMap();
    m4.put("L1", "444");
    m4.put("L2", 444);
    m4.put("L3", "ddd");
    m4.put("L4", "DDD");
    list.add(m1);
    list.add(m2);
    list.add(m3);
    list.add(m4);

    document.put("A3", list);

    Map<String, Object> mapD = new HashMap();
    mapD.put("D1", 4444);
    mapD.put("D2", "DDDD");
    List<Object> l2 = new ArrayList<>();
    l2.addAll(list);
    mapD.put("D3", l2);
    document.put("A4", mapD);

  }

  @Test
  public void findArray() {
    Object value = DocUtil.findArray(document, "A1");
    Assert.assertNull(value);
    value = DocUtil.findArray(document, "A2");
    Assert.assertNull(value);

    List ret = DocUtil.findArray(document, "A3");
    Assert.assertEquals(ret.size(), 4);
    Assert.assertArrayEquals(ret.toArray(), list.toArray());

    ret = DocUtil.findArray(document, "A4.D1");
    Assert.assertNull(ret);
    ret = DocUtil.findArray(document, "A4.D3");
    Assert.assertEquals(ret.size(), 4);
    Assert.assertArrayEquals(ret.toArray(), list.toArray());
  }

  @Test
  public void compareObject() {
    boolean ret = DocUtil.compareObject(document, "A1", 1111);
    Assert.assertTrue(ret);

    ret = DocUtil.compareObject(document, "A2.B1", 1111);
    Assert.assertFalse(ret);

    ret = DocUtil.compareObject(document, "A2.B1", 22222);
    Assert.assertTrue(ret);

    DocUtil.compareObject(document, "A3", list);
    Assert.assertTrue(ret);
  }

  @Test
  public void changeField() {
    DocUtil.changeField(document, "A1", 11111);
    boolean ret = DocUtil.compareObject(document, "A1", 11111);
    Assert.assertTrue(ret);

    DocUtil.changeField(document, "A2.B1", 222);
    ret = DocUtil.compareObject(document, "A2.B1", 222);
    Assert.assertTrue(ret);

    DocUtil.changeField(document, "A3.L1", "lllll");
    List retList = DocUtil.findArray(document, "A3");
    List except = new ArrayList();
    Map<String, Object> m1 = new HashMap();
    m1.put("L1", "lllll");
    m1.put("L2", 111);
    m1.put("L3", "aaa");
    m1.put("L4", "AAA");
    Map<String, Object> m2 = new HashMap();
    m2.put("L1", "lllll");
    m2.put("L2", 222);
    m2.put("L3", "bbb");
    m2.put("L4", "BBB");
    Map<String, Object> m3 = new HashMap();
    m3.put("L1", "lllll");
    m3.put("L2", 333);
    m3.put("L3", "ccc");
    m3.put("L4", "CCC");
    Map<String, Object> m4 = new HashMap();
    m4.put("L1", "lllll");
    m4.put("L2", 444);
    m4.put("L3", "ddd");
    m4.put("L4", "DDD");
    except.add(m1);
    except.add(m2);
    except.add(m3);
    except.add(m4);
    Assert.assertArrayEquals(retList.toArray(), except.toArray());
  }
}
