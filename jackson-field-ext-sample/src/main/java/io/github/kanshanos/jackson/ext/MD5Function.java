package io.github.kanshanos.jackson.ext;

import cn.hutool.crypto.digest.MD5;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * MD5加密
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MD5Function implements Function<String, String> {
    @Override
    public String apply(String o) {
        return MD5.create().digestHex(o);
    }
}
