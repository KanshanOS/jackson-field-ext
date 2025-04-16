package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【固定电话】 前四位，后两位
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskFixedPhoneFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return DesensitizedUtil.fixedPhone(o);
    }
}
