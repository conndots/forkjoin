package io.conndots.forkjoin.wordcount;

import com.google.common.collect.ImmutableList;

/**
 * Created by xqlee on 16/1/30.
 */
public interface TextSpliter {
    ImmutableList<String> split(String pageText);
}
