package io.conndots.forkjoin.wordcount.batchparrallel;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.conndots.forkjoin.util.MapUtil;
import io.conndots.forkjoin.wordcount.DefaultTextSpliter;
import io.conndots.forkjoin.wordcount.ResourceFetcher;
import io.conndots.forkjoin.wordcount.TextSpliter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by xqlee on 16/1/30.
 */
public class BatchParrallelWordCountTask extends RecursiveTask<ImmutableMap<String, Long>> {
    protected final ImmutableList<String> resourcePaths;
    protected final TextSpliter textSpliter;
    protected final ResourceFetcher resourceFetcher;
    protected final int batchSize;

    protected BatchParrallelWordCountTask(ImmutableList<String> resourcePaths, ResourceFetcher resourceFetcher,
                                          TextSpliter textSpliter, int batchSize) {
        Preconditions.checkNotNull(resourcePaths);
        Preconditions.checkNotNull(resourceFetcher);

        this.resourceFetcher = resourceFetcher;
        this.resourcePaths = resourcePaths;
        this.textSpliter = textSpliter == null ? new DefaultTextSpliter() : textSpliter;
        this.batchSize = batchSize > 0 ? batchSize : 1000;
    }

    protected BatchParrallelWordCountTask(ImmutableList<String> resourcePaths, ResourceFetcher resourceFetcher,
                                          TextSpliter textSpliter) {
        Preconditions.checkNotNull(resourcePaths);
        Preconditions.checkNotNull(resourceFetcher);

        this.resourceFetcher = resourceFetcher;
        this.resourcePaths = resourcePaths;
        this.textSpliter = textSpliter == null ? new DefaultTextSpliter() : textSpliter;
        this.batchSize = 1000;
    }

    private ImmutableList<ForkJoinTask<ImmutableMap<String, Long>>> forkPagesWordCountTasks() {
        ImmutableList.Builder<ForkJoinTask<ImmutableMap<String, Long>>> tasksListBuilder = new Builder<>();

        ImmutableList.Builder<String> urisListBuilderForNextTask = null;
        for (int i = 0; i < resourcePaths.size(); i++) {
            if (i % batchSize == 0 || i == resourcePaths.size() - 1) {
                if (urisListBuilderForNextTask != null) {
                    final ImmutableList<String> resoureUris = urisListBuilderForNextTask.build();
                    if (resoureUris.size() > 0) {
                        tasksListBuilder.add(new RecursiveTask<ImmutableMap<String, Long>>() {
                            @Override
                            protected ImmutableMap<String, Long> compute() {
                                ImmutableList.Builder<String> pagesListBuilder = new Builder<>();
                                List<ForkJoinTask<String>> fetcherTasks = Lists.newArrayListWithExpectedSize(resoureUris
                                        .size());
                                resoureUris.forEach(uri -> {
                                    fetcherTasks.add(new RecursiveTask<String>() {
                                        @Override
                                        protected String compute() {
                                            try {
                                                return resourceFetcher.fetch(uri);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                return "";
                                            }
                                        }
                                    }.fork());
                                });

                                fetcherTasks.forEach(fetcherTask -> {
                                    pagesListBuilder.add(fetcherTask.join());
                                });

                                new PageWordsCountWorker(pagesListBuilder.build(),
                                        textSpliter).fork();
                                return null;
                            }
                        }.fork());
                    }
                }
                urisListBuilderForNextTask = new Builder<>();
            }
            urisListBuilderForNextTask.add(resourcePaths.get(i));
        }
        return tasksListBuilder.build();
    }

    @Override
    protected ImmutableMap<String, Long> compute() {
        ImmutableList<ForkJoinTask<ImmutableMap<String, Long>>> wordCountTasks = forkPagesWordCountTasks();

        ImmutableMap<String, Long> result = null;
        for (ForkJoinTask<ImmutableMap<String, Long>> task : wordCountTasks) {
            result = MapUtil.mergeWith((l0, l1) -> l0 + l1, new ImmutableMap.Builder<String, Long>().build(), task.join
                    ());
        }

        return result;

    }
}
