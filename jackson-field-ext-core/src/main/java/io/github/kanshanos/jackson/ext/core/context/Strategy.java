package io.github.kanshanos.jackson.ext.core.context;

import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 组装策略
 *
 * @author Neo
 * @since 2025/4/16 15:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Strategy {

    /**
     * 是否忽略，默认：DEFAULT
     */
    private TrueFalse ignore;

    /**
     * 使用覆盖当前字段，默认为 DEFAULT
     */
    private TrueFalse override;

    /**
     * 异常处理策略，默认为 DEFAULT
     */
    private ExceptionStrategy exception;

}
