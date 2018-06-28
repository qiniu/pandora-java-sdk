package com.qiniu.pandora.pipeline.points;

import com.qiniu.pandora.common.Configuration;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.util.Json;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenzhengcui on 2017/4/10.
 */
public class PointTest {
    @Test
    public void fromPointString() throws Exception {
        String str = "a=1\tb=2\tc=4=4\td=1\n";
        Point point = Point.fromPointString(str);
        Assert.assertEquals(str.getBytes(Constants.UTF_8).length, point.getSize());
        Assert.assertEquals(str, point.toString());

        str = "a=1\tb=2\tc=4=4\td=1";
        point = Point.fromPointString(str);
        Assert.assertEquals(str.getBytes(Constants.UTF_8).length + 1, point.getSize());
        Assert.assertEquals(str + "\n", point.toString());
    }

    @Test
    public void fromPointsString() throws Exception {
        String str = "a=1\tb=2\tc=4=4\td=1\na=2\tb=3\tc=5=5=5\td=2\n";
        List<Point> point = Point.fromPointsString(str);
        Assert.assertEquals(point.size(), 2);
        Assert.assertEquals(18, point.get(0).getSize());
        Assert.assertEquals(20, point.get(1).getSize());
    }


    @Test
    public void fromPointsStringError() throws Exception {
        // 第一条的 1234 被忽略
        // 第二条的空字段被忽略
        // 第三行空行被忽略
        String str = "a=1\tb=2\t1234\tc=4=4\td=1\na=2\t\tb=3\tc=5=5=5\td=2\n\n";
        List<Point> point = Point.fromPointsString(str);
        Assert.assertEquals(point.size(), 2);
        Assert.assertEquals(18, point.get(0).getSize());
        Assert.assertEquals(20, point.get(1).getSize());
    }

    @Test
    public void fromPointsStringEmpty() throws Exception {
        String str = "\n\n\n";
        List<Point> point = Point.fromPointsString(str);
        Assert.assertEquals(point.size(), 0);
    }

    @Test
    public void complexDataType() throws Exception {
        Map<String, Integer> mapData = new HashMap<>();
        mapData.put("t1", 1);
        mapData.put("t2", 2);
        List<Integer> arrayData = new ArrayList<>(2);
        arrayData.add(1);
        arrayData.add(2);
        Point point = new Point();
        point.append("integerKey", 2);
        point.append("boolKey", Boolean.TRUE);
        point.append("mapKey", mapData);
        point.append("arrayKey", arrayData);
        String expected = "integerKey=2\tboolKey=true\tmapKey=" + Json.encode(mapData) + "\tarrayKey=" + Json.encode(arrayData) + "\n";
        Assert.assertEquals(point.toString(), expected);
    }
}