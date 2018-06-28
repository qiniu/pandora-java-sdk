package com.qiniu.pandora.pipeline.sender;


import com.qiniu.pandora.common.*;
import com.qiniu.pandora.http.HttpCommon;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.FilePointsGenerator;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.StringMap;
import com.qiniu.pandora.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Pipeline 打点数据发送器
 */
public class DataSender implements Sender {
    private static final int LINE_CAPACITY = 4;
    private PandoraClient pandoraClient;
    private String url;

    public DataSender(String repoName, Auth auth) {
        url = url(repoName);
        this.pandoraClient = new PandoraClientImpl(auth);
    }

    public DataSender(String repoName, PandoraClient pandoraClient) {
        url = url(repoName);
        this.pandoraClient = pandoraClient;
    }

    /**
     * 发送Batch数据
     */
    public Response send(byte[] postBody) throws QiniuException {
        if (postBody.length <= 0) {
            return null;
        }
        StringMap headers = new StringMap();
        return pandoraClient.post(url, postBody, headers, HttpCommon.TEXT_PLAIN);

    }


    /**
     * 发送Batch数据
     *
     * @param points Batch数据点
     * @return null 没有发送数据，Response 发送结果
     * @throws QiniuException 发送失败
     */
    @Override
    public Response send(Batch points) throws QiniuException {
        return this.send(points.toString().getBytes(Constants.UTF_8));
    }


    /**
     * 传入数据点，自动分批发送
     * 注意，该接口不能保证原子性
     *
     * @param points 点迭代器
     * @return SendPointError 所有失败数据点和错误异常信息
     */
    @Override
    public SendPointError send(Iterable<Point> points) {
        SendPointError se = new SendPointError();
        Batch b = new Batch();
        for (Point p : points) {
            if (p.isTooLarge()) {
                se.addFail(p, new QiniuRuntimeException("Point too large"));
                continue;
            }
            if (!b.canAdd(p) && b.getSize() > 0) {
                try {
                    send(b);
                } catch (QiniuException e) {
                    se.addFails(b.getPoints(), e);
                }
                b.clear();
            }
            b.add(p);
        }
        try {
            send(b);
        } catch (QiniuException e) {
            se.addFails(b.getPoints(), e);
        }
        return se;
    }

    @Override
    public void close() {
    }

    @Override
    public SendPointError sendFromString(String points) throws QiniuException {
        Iterable<Point> ps = Point.fromPointsString(points);
        return send(ps);
    }

    @Override
    public SendPointError sendFromFile(String filePath) throws QiniuException {
        Iterable<Point> points = readFromFile(filePath);
        return send(points);
    }

    @Override
    public SendPointError sendFromBytes(byte[] points) throws QiniuException {
        String ps = new String(points, Constants.UTF_8);
        return sendFromString(ps);
    }


    protected Iterable<Point> readFromFile(String filePath) throws QiniuException {
        if (StringUtils.isBlank(filePath)) {
            throw new QiniuRuntimeException("file name is empty");
        }
        File f = new File(filePath);
        final Scanner scanner;
        try {
            scanner = new Scanner(f, "utf-8");
        } catch (FileNotFoundException e) {
            throw new QiniuRuntimeException(e);
        }
        return new Iterable<Point>() {
            @Override
            public Iterator<Point> iterator() {
                return new FilePointsGenerator(scanner);
            }
        };
    }

    protected String url(String repoName) {
        return Constants.PIPELINE_HOST + "/v2/repos/" + repoName + "/data";
    }

}
