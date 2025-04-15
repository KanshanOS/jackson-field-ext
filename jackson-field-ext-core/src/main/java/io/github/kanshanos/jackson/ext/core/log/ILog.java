package io.github.kanshanos.jackson.ext.core.log;

/**
 * 日志接口
 *
 * @author Neo
 * @since 2025/4/15 17:36
 */
public interface ILog {

    void error(String msg, Throwable t);
}
