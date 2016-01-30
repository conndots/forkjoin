package io.conndots.forkjoin.wordcount;

import java.io.IOException;

/**
 * Created by xqlee on 16/1/30.
 */
public interface ResourceFetcher {
    String fetch(String uri) throws IOException;
}
