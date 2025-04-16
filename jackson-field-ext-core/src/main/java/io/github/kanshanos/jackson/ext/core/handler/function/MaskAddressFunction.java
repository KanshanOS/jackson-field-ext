package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【地址】只显示到地区，不显示详细地址，比如：上海市浦东新区世纪大道1号 -> 上海市浦东新区******
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskAddressFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return DesensitizedUtil.address(o, 6);
    }
}
