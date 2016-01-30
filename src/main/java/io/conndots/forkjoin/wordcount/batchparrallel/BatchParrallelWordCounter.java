package io.conndots.forkjoin.wordcount.batchparrallel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.conndots.forkjoin.ForkJoinRunner;
import io.conndots.forkjoin.wordcount.ResourceFetcher;
import io.conndots.forkjoin.wordcount.TextSpliter;
import io.conndots.forkjoin.wordcount.WordsCounter;

/**
 * Created by xqlee on 16/1/30.
 */
public class BatchParrallelWordCounter implements WordsCounter {
    private static final int BATCH_SIZE = 1000;
    @Override
    public ImmutableMap<String, Long> countWords(final ImmutableList<String> pageUris, ResourceFetcher
            resourceFetcher, TextSpliter spliter) {
        return ForkJoinRunner.runTask(new BatchParrallelWordCountTask(pageUris, resourceFetcher, spliter, BATCH_SIZE)
                , Runtime.getRuntime().availableProcessors(), true);
    }
}
