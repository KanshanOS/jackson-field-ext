package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【中国车牌】车牌中间用*代替
 * eg1：null       -》 ""
 * eg1：""         -》 ""
 * eg3：苏D40000   -》 苏D4***0
 * eg4：陕A12345D  -》 陕A1****D
 * eg5：京A123     -》 京A123     如果是错误的车牌，不处理
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskCarLicenseFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return DesensitizedUtil.carLicense(o);
    }
}
