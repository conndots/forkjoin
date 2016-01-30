package io.conndots.forkjoin.wordcount.batchparrallel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.conndots.forkjoin.wordcount.TextSpliter;

import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.function.Consumer;

/**
 * Created by xqlee on 16/1/30.
 */
public class PageWordsCountWorker extends RecursiveTask<ImmutableMap<String, Long>> {
    private final ImmutableList<String> pages;
    private final TextSpliter textSpliter;
    protected PageWordsCountWorker(ImmutableList<String> pages, TextSpliter textSpliter) {
        this.pages = pages;
        this.textSpliter = textSpliter;
    }
    @Override
    protected ImmutableMap<String, Long> compute() {
        final Map<String, Long> countMap = Maps.newHashMap();
        pages.forEach(new Consumer<String>() {
            @Override
            public void accept(final String pageText) {
                textSpliter.split(pageText).forEach(new Consumer<String>() {
                    @Override
                    public void accept(final String token) {
                        if (countMap.containsKey(token)) {
                            countMap.put(token, countMap.get(token) + 1l);
                        }
                        else {
                            countMap.put(token, 1l);
                        }
                    }
                });
            }
        });

        return new ImmutableMap.Builder<String, Long>().putAll(countMap).build();
    }
}
