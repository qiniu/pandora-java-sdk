package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.pipeline.points.AbstractPointsGenerator;
import com.qiniu.pandora.pipeline.points.Point;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wenzhengcui on 2017/4/10.
 */
public class SenderTest {
    List<Point> points = new ArrayList<Point>();
    int maxCnt;

    @Before
    public void setUp() throws Exception {
        long start = System.currentTimeMillis();
        Point p = new Point();
        p.append("key1", 1);
        p.append("key2", "2");
        p.append("key3", 4.5);
        p.append("key3", new Date());
        int size = p.getSize();
        maxCnt = 10 * 1024 / size * 1024;
        for (long i = 0; i < maxCnt; i++) {
            points.add(p);
        }
        System.out.println("Setup System.currentTimeMillis() - start = " + (System.currentTimeMillis() - start));

    }

    public class PointsGenerator extends AbstractPointsGenerator {

        int i = 0;

        @Override
        public boolean hasNext() {
            return i < maxCnt;
        }

        @Override
        public Point next() {
            i++;
            Point p = new Point();
            p.append("key1", 1);
            p.append("key2", "2");
            p.append("key3", 4.5);
            p.append("key3", new Date());
            return p;
        }
    }

}
