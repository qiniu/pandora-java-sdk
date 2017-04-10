package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.pipeline.error.SendPointError;

/**
 * 数据发送接口.
 */
public interface Sender {
    /**
     * 发送Batch数据
     *
     * @param points
     * @return
     * @throws QiniuException
     */
    Response send(Batch points) throws QiniuException;

    /**
     * 自动切割数据点进行发送
     *
     * @param points
     * @return
     */
    SendPointError send(Iterable<Point> points);

    /**
     * 从文件读取数据进行发送
     *
     * @param filePath
     * @return
     * @throws QiniuException
     */
    SendPointError sendFromFile(String filePath) throws QiniuException;

    /**
     * 发送bytes数据点
     *
     * @param points
     * @return
     * @throws QiniuException
     */
    SendPointError sendFromString(String points) throws QiniuException;

    /**
     * 发送bytes数据点
     *
     * @param points
     * @return
     * @throws QiniuException
     */
    SendPointError sendFromBytes(byte[] points) throws QiniuException;

    /**
     * 关闭Sender
     */
    void close();
}
