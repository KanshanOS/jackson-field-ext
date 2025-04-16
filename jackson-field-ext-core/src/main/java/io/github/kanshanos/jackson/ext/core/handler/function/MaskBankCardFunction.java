package io.github.kanshanos.jackson.ext.core.handler.function;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/**
 * 【银行卡号脱敏】由于银行卡号长度不定，所以只展示前4位，后面的位数根据卡号决定展示1-4位
 * 例如：
 * <pre>{@code
 *      1. "1234 2222 3333 4444 6789 9"    ->   "1234 **** **** **** **** 9"
 *      2. "1234 2222 3333 4444 6789 91"   ->   "1234 **** **** **** **** 91"
 *      3. "1234 2222 3333 4444 678"       ->    "1234 **** **** **** 678"
 *      4. "1234 2222 3333 4444 6789"      ->    "1234 **** **** **** 6789"
 *  }</pre>
 *
 * @author Neo
 * @since 2025/3/27 08:47
 */
@Component
public class MaskBankCardFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        return DesensitizedUtil.bankCard(o);
    }
}
