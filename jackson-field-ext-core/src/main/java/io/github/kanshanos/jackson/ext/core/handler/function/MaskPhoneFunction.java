package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 隐藏手机号函数
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskPhoneFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return StrUtil.hide(o, 3, 7);
    }
}
