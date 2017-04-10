package com.qiniu.pandora.pipeline.sender;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuRuntimeException;
import com.qiniu.pandora.pipeline.error.SendPointError;
import com.qiniu.pandora.pipeline.points.Batch;
import com.qiniu.pandora.pipeline.points.Point;
import com.qiniu.pandora.util.Auth;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 并发的数据发送器
 * 注意：在使用完成后需要执行close() 函数，保证后台线程全部结束
 */
public class ParallelDataSender extends DataSender {
    final ExecutorService service;
    final int parrallel;

    public ParallelDataSender(String repoName, Auth auth) {
        this(repoName, auth, 1);
    }


    public ParallelDataSender(String repoName, Auth auth, int par) {
        super(repoName, auth);
        this.parrallel = par;
        this.service = Executors.newFixedThreadPool(par);
    }

    public ParallelDataSender(String repoName, PandoraClient pandoraClient, int par) {
        super(repoName, pandoraClient);
        this.parrallel = par;
        this.service = Executors.newFixedThreadPool(par);
    }

    /**
     * 传入数据点，自动并发分批发送
     * 注意，该接口不能保证原子性
     *
     * @param points 数据点迭代器
     * @return SendPointError 所有失败数据点和错误异常信息（不保证顺序）
     * @Experimental
     */
    @Override
    public SendPointError send(final Iterable<Point> points) {
        final SendPointError se = new SendPointError();
        final ConcurrentLinkedQueue<Batch> batches = new ConcurrentLinkedQueue<>();
        final AtomicBoolean finishParse = new AtomicBoolean(false);
        // 切分合适的数据batch
        service.submit(new Runnable() {
            @Override
            public void run() {
                Batch b = new Batch();
                for (Point p : points) {
                    if (p.isTooLarge()) {
                        se.addFail(p, new QiniuRuntimeException("Point too large"));
                        continue;
                    }
                    if (!b.canAdd(p) && b.getSize() > 0) {
                        batches.add(b);
                        b = new Batch();
                    }
                    b.add(p);
                }
                batches.add(b);
                finishParse.set(true);
            }
        });

        // 并发从queue中读取batch，并发发送
        final CountDownLatch latch = new CountDownLatch(this.parrallel);
        for (int i = 0; i < this.parrallel; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        final Batch b = batches.poll();
                        if (finishParse.get() && b == null) {
                            break;
                        }
                        try {
                            if (b != null) {
                                send(b);
                            }
                        } catch (QiniuException e) {
                            se.addFails(b.getPoints(), e);
                        } catch (Exception e) {
                            se.addFails(b.getPoints(), new QiniuRuntimeException(e));
                        }
                    }
                    latch.countDown();
                }
            });
        }

        // 等待发送完毕
        try {
            latch.await();
        } catch (InterruptedException e) {
            // 被中断
        }
        return se;
    }

    @Override
    public void close() {
        super.close();
        service.shutdown();
    }
}
