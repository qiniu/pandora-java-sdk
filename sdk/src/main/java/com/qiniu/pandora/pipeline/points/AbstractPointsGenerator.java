package com.qiniu.pandora.pipeline.points;

import java.util.Iterator;

/**
 * 抽象的数据点迭代器.
 */
public abstract class AbstractPointsGenerator implements Iterator<Point> {

    @Override
    public void remove() {
        // do nothing
    }
}
