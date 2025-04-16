package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【电子邮箱】邮箱前缀仅显示头尾2个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：123456@gmail.com 变为：12**56@gmail.com
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
