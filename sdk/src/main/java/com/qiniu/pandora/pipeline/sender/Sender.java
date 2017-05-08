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
     * @param points Batch数据点
     * @return 请求响应体
     * @throws QiniuException 请求异常
     */
    Response send(Batch points) throws QiniuException;

    /**
     * 自动切割数据点进行发送
     *
     * @param points 所有数据点
     * @return 所有发送失败的数据点和异常信息
     */
    SendPointError send(Iterable<Point> points);

    /**
     * 从文件读取数据进行发送
     *
     * @param filePath 文件路径
     * @return 所有发送失败的数据点和异常信息
     * @throws QiniuException 运行时异常
     */
    SendPointError sendFromFile(String filePath) throws QiniuException;

    /**
     * 发送bytes数据点
     *
     * @param points 所有数据点
     * @return 所有发送失败的数据点和异常信息
     * @throws QiniuException 运行时异常
     */
    SendPointError sendFromString(String points) throws QiniuException;

    /**
     * 发送bytes数据点
     *
     * @param points 所有数据点
     * @return 所有发送失败的数据点和异常信息
     * @throws QiniuException 运行时异常
     */
    SendPointError sendFromBytes(byte[] points) throws QiniuException;

    /**
     * 关闭Sender
     */
    void close();
}
