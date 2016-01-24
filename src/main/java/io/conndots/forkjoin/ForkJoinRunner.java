package io.conndots.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by xqlee on 16/1/24.
 */
public class ForkJoinRunner {
    public static <T> T runTask(ForkJoinTask<T> task) {
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        return pool.invoke(task);
    }

    public static <T> T runTask(ForkJoinTask<T> task, int threadNum) {
        ForkJoinPool pool = new ForkJoinPool(threadNum);
        return pool.invoke(task);
    }

    public static <T> T runTask(ForkJoinTask<T> task, int threadNum, boolean async) {
        ForkJoinPool pool = new ForkJoinPool(threadNum, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, async);
        return pool.invoke(task);
    }
}
