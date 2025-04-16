package io.github.kanshanos.jackson.ext.core.enums;

/**
 * 异常策略枚举
 *
 * @author Neo
 * @since 2025/4/15 16:44
 */
public enum ExceptionStrategy {
    DEFAULT,            // 使用全局默认策略
    THROWS,             // 抛出异常
    THROWS_AND_LOG,     // 打日志并抛出异常
    ORIGIN_VALUE,       // 返回原值
    ORIGIN_AND_LOG,     // 返回原值并打日志
    HIDDEN_VALUE,       // 隐藏值
    HIDDEN_AND_LOG,     // 打日志并跳过
}
