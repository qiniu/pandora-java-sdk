package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.util.StringMap;

import java.util.List;

/**
 * Created by wenzhengcui on 2017/4/10.
 */
public class MockPandoraClient implements PandoraClient {

    volatile int pointSize = 0;

    public int getPointSize() {
        return pointSize;
    }

    @Override
    public Response post(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException {
        List<Point> ps = Point.fromPointsString(new String(content, Constants.UTF_8));
        synchronized (this) {
            pointSize += ps.size();
        }
        try {
            // 模拟io行为
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response get(String url, StringMap headers) throws QiniuException {
        return null;
    }

    @Override
    public Response put(String url, byte[] content, StringMap headers, String bodyType) throws QiniuException {
        return null;
    }

    @Override
    public Response delete(String url, StringMap headers) throws QiniuException {
        return null;
    }
}
