package com.qiniu.pandora.pipeline.points;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by wenzhengcui on 2017/4/10.
 */
public class PointTest {
    @Test
    public void fromPointString() throws Exception {
        String str = "a=1\tb=2\tc=4=4\td=1\n";
        Point point = Point.fromPointString(str);
        Assert.assertEquals(str.getBytes().length, point.getSize());
        Assert.assertEquals(str, point.toString());

        str = "a=1\tb=2\tc=4=4\td=1";
        point = Point.fromPointString(str);
        Assert.assertEquals(str.getBytes().length + 1, point.getSize());
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

}