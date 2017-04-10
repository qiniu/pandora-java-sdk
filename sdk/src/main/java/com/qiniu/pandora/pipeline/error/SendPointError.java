package com.qiniu.pandora.pipeline.error;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.pipeline.points.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 发送失败的错误，包含需要重试哪些数据，并且返回错误异常
 * 注意：依赖具体实现方式，并不能保证错误出现顺序
 */
public class SendPointError {
    private List<Point> failures = new ArrayList<>();
    private List<QiniuException> exceptions = new ArrayList<>();

    public void addFail(Point p, QiniuException e) {
        failures.add(p);
        exceptions.add(e);
    }

    public void addFails(Collection<Point> ps, QiniuException e) {
        failures.addAll(ps);
        exceptions.add(e);
    }


    public List<Point> getFailures() {
        return failures;
    }

    public List<QiniuException> getExceptions() {
        return exceptions;
    }
}
