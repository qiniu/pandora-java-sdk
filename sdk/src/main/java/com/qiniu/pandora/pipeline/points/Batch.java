package com.qiniu.pandora.pipeline.points;

import java.util.ArrayList;
import java.util.List;

/**
 * Batch数据点，批量发送
 */
public class Batch {
    public static final int MAX_BATCH_SIZE = 2 * 1024 * 1024;

    private List<Point> points = new ArrayList<Point>();
    private int maxSize = MAX_BATCH_SIZE;
    private int size;

    /**
     * 获得Batch中所有数据点
     *
     * @return 所有数据点
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * 增加数据点
     *
     * @param p 数据点
     */
    public void add(Point p) {
        points.add(p);
        size += p.getSize();
    }

    /**
     * 获得当前batch的byte 大小
     *
     * @return 所有数据点大小总和
     */
    public int getSize() {
        return size;
    }

    /**
     * 获得最大的Batch大小
     *
     * @param maxSize 最大Batch大小
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Batch是否已经满了
     *
     * @return Batch是否已经填满
     */
    public boolean isFull() {
        return size >= maxSize;
    }

    /**
     * 判断加入当前数据点之后，Batch是否会超出限制
     *
     * @param p 数据点
     * @return 加入数据点则超出限制
     */
    public boolean canAdd(Point p) {
        return size + p.getSize() <= maxSize;
    }

    /**
     * 清空batch
     */
    public void clear() {
        points.clear();
        size = 0;
    }

    /**
     * 生成数据点
     *
     * @return 数据点
     */
    public String toString() {
        StringBuilder buff = new StringBuilder();
        for (Point point : points) {
            buff.append(point);
        }
        return buff.toString();
    }

}
