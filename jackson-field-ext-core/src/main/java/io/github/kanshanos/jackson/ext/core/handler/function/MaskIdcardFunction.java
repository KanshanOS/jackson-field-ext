package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【身份证号码】 110101199001011234 -> 110101********1234
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskIdcardFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return DesensitizedUtil.idCardNum(o, 6, 4);
    }

}
