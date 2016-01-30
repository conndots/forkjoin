package io.conndots.forkjoin.wordcount;

import com.google.common.collect.ImmutableList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xqlee on 16/1/30.
 */
public class DefaultTextSpliter implements TextSpliter {
    private static final Pattern WORDS_PATTERN = Pattern.compile("\\w+");
    @Override
    public ImmutableList<String> split(final String pageText) {
        ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();
        Matcher matcher = WORDS_PATTERN.matcher(pageText);
        while (matcher.find()) {
            builder.add(matcher.group());
        }
        return builder.build();
    }
}
