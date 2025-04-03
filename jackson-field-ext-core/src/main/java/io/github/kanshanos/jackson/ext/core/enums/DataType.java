package io.github.kanshanos.jackson.ext.core.enums;

/**
 * 数据类型
 *
 * @author Neo
 * @since 2025/3/24 15:40
 */
public enum DataType {

    /**
     * 字符串数组，如：1,2,3
     */
    STRING_ARRAY,
    /**
     * JSON数组，如：[1,2,3]
     */
    JSON_ARRAY,
    /**
     * LIST 集合，如：[1,2,3]
     */
    LIST,
    /**
     * MAP 集合，如：{0:'FEMALE',1:'MALE'}
     * 仅适用于 extType 结果类型标注
     */
    MAP,
    ;


}
