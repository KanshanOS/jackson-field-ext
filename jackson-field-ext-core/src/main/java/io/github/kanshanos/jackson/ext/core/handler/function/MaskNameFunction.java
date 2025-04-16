package io.github.kanshanos.jackson.ext.core.handler.function;

import io.github.kanshanos.jackson.ext.core.util.NameDesensitizedUtils;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【中文姓名】只显示姓氏，兼容复姓，其他隐藏为星号，比如：李**
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskNameFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return NameDesensitizedUtils.process(o);
    }
}
