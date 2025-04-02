package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 隐藏邮箱函数
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskEmailFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return StrUtil.hide(o, 2, StrUtil.indexOf(o, '@') - 2);
    }
}
