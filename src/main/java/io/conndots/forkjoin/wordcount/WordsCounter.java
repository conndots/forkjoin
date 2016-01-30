package io.conndots.forkjoin.wordcount;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by xqlee on 16/1/30.
 */
public interface WordsCounter {
    ImmutableMap<String, Long> countWords(ImmutableList<String> pageUris, ResourceFetcher resourceFetcher,
                                          TextSpliter spliter);
}
