package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【手机号码】前三位，后4位，其他隐藏，比如135****2210
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskMobilePhoneFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return DesensitizedUtil.mobilePhone(o);
    }
}
