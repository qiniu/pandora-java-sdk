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
 * 并发测试
 */
public class ParDataSenderTest extends SenderTest {
    @Test
    public void testParSend() throws Exception {
        long start = System.currentTimeMillis();

        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new ParallelDataSender("testrepo", client, 4);
        ds.send(points);
        Assert.assertEquals(client.getPointSize(), maxCnt);
        System.out.println("System.currentTimeMillis() - start = " + (System.currentTimeMillis() - start));
        ds.close();
    }

    @Test
    public void testParPointsGenerator() throws Exception {
        Iterable<Point> piter = new Iterable<Point>() {
            @Override
            public Iterator<Point> iterator() {
                return new PointsGenerator();
            }
        };
        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new ParallelDataSender("testrepo", client, 4);
        ds.send(piter);
        Assert.assertEquals(client.getPointSize(), maxCnt);
        ds.close();

    }


    @Test
    public void testPointsParGeneratorException() throws Exception {
        Iterable<Point> piter = new Iterable<Point>() {
            @Override
            public Iterator<Point> iterator() {
                return new PointsGenerator();
            }
        };
        PandoraClient client = new MockExceptionPandoraClient();
        Sender ds = new ParallelDataSender("testrepo", client, 4);
        SendPointError se = ds.send(piter);
        Assert.assertEquals(se.getFailures().size(), maxCnt);
        ds.close();

    }


    @Test
    public void testParPointsFileSender() throws Exception {
        Path p = Paths.get("testfile");
        List<String> lines = new ArrayList<String>();
        for (Point point : points) {
            lines.add(point.toString().trim());
        }
        Files.write(p, lines, Charset.defaultCharset());

        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new ParallelDataSender("testrepo", client, 4);
        SendPointError se = ds.sendFromFile("testfile");
        Assert.assertEquals(client.getPointSize(), maxCnt);
        Files.delete(p);
        ds.close();

    }

    @Test
    public void testPointsStringSender() throws Exception {
        MockPandoraClient client = new MockPandoraClient();
        Sender ds = new ParallelDataSender("testrepo", client, 4);

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
        ds.close();
    }

}
