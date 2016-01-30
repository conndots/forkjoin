package io.conndots.forkjoin.wordcount.fetcher;

import com.google.common.base.Preconditions;
import io.conndots.forkjoin.wordcount.ResourceFetcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by xqlee on 16/1/30.
 */
public class FSResourceFetcher implements ResourceFetcher {
    @Override
    public String fetch(final String uri) throws IOException {
        Preconditions.checkNotNull(uri);

        File file = new File(uri);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("It cannot be a directory");
        }

        StringBuilder pageStr = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Stream<String> stream = reader.lines();;
            stream.forEach(new Consumer<String>() {
                @Override
                public void accept(final String s) {
                    pageStr.append(s);
                }
            });
        }

        return pageStr.toString();
    }
}
