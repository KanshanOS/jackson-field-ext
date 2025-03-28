package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.StrUtil;

import java.util.function.Function;


/**
 * 隐藏手机号函数
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
public class MaskPhoneFunction implements Function<Object, Object> {
    @Override
    public Object apply(Object o) {
        return StrUtil.hide(String.valueOf(o), 3, 7);
    }
}
