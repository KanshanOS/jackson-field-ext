package io.github.kanshanos.jackson.ext.core.log;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Neo
 * @since 2025/4/15 17:38
 */
@Slf4j
public class Slf4jLog implements ILog {

    @Override
    public void error(String msg, Throwable t) {
        log.error(msg, t);
    }
}
