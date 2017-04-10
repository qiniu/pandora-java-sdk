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
     * @return
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * 增加数据点
     *
     * @param p
     */
    public void add(Point p) {
        points.add(p);
        size += p.getSize();
    }

    /**
     * 获得当前batch的byte 大小
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * 获得最大的Batch大小
     *
     * @param maxSize
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Batch是否已经满了
     *
     * @return
     */
    public boolean isFull() {
        return size >= maxSize;
    }

    /**
     * 判断加入当前数据点之后，Batch是否会超出限制
     *
     * @param p
     * @return
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
     * @return
     */
    public String toString() {
        StringBuilder buff = new StringBuilder();
        for (Point point : points) {
            buff.append(point);
        }
        return buff.toString();
    }

}
