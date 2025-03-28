package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.StrUtil;

import java.util.function.Function;


/**
 * 隐藏邮箱函数
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
public class MaskEmailFunction implements Function<Object, Object> {
    @Override
    public Object apply(Object o) {
        String value = String.valueOf(o);
        return StrUtil.hide(value, 2, StrUtil.indexOf(value, '@') - 2);
    }
}
