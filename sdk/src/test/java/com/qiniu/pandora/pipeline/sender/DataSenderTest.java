package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Point;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 顺序打点测试.
 */
public class DataSenderTest extends SenderTest {

    @Test
    public void testSend() throws Exception {
        long start = System.currentTimeMillis();
        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new DataSender("testrepo", client);
        ds.send(points);
        Assert.assertEquals(client.getPointSize(), maxCnt);
        System.out.println("System.currentTimeMillis() - start = " + (System.currentTimeMillis() - start));
    }


    @Test
    public void testPointsGenerator() throws Exception {
        Iterable<Point> piter = new Iterable<Point>() {
            @Override
            public Iterator<Point> iterator() {
                return new PointsGenerator();
            }
        };
        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new DataSender("testrepo", client);
        ds.send(piter);
        Assert.assertEquals(client.getPointSize(), maxCnt);
    }


    @Test
    public void testPointsGeneratorException() throws Exception {
        Iterable<Point> piter = new Iterable<Point>() {
            @Override
            public Iterator<Point> iterator() {
                return new PointsGenerator();
            }
        };
        PandoraClient client = new MockExceptionPandoraClient();
        Sender ds = new DataSender("testrepo", client);
        SendPointError se = ds.send(piter);
        Assert.assertEquals(se.getFailures().size(), maxCnt);
    }


    @Test
    public void testPointsFileSender() throws Exception {
        Path p = Paths.get("testfile");
        List<String> lines = new ArrayList<String>();
        for (Point point : points) {
            lines.add(point.toString().trim());
        }
        Files.write(p, lines, Charset.defaultCharset());

        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new DataSender("testrepo", client);
        SendPointError se = ds.sendFromFile("testfile");
        Assert.assertEquals(client.getPointSize(), maxCnt);
        Files.delete(p);
    }

    @Test
    public void testPointsStringSender() throws Exception {
        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new DataSender("testrepo", client);

        Point p = new Point();
        p.append("key1", 1);
        p.append("key2", "2");
        p.append("key3", 4.5);
        p.append("key3", new Date());
        String aPoint = p.toString();

        StringBuilder sb = new StringBuilder(aPoint.length() * maxCnt);
        for (int i = 0; i < maxCnt; i++) {
            sb.append(aPoint);
        }
        SendPointError se = ds.sendFromString(sb.toString().trim());
        Assert.assertEquals(client.getPointSize(), maxCnt);

        se = ds.sendFromBytes(sb.toString().trim().getBytes(Constants.UTF_8));
        Assert.assertEquals(client.getPointSize(), maxCnt * 2);

    }


}