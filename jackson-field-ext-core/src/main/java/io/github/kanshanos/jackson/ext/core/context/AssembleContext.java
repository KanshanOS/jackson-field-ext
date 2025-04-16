package io.github.kanshanos.jackson.ext.core.context;

import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;
import lombok.experimental.UtilityClass;

/**
 * 组装上下文
 *
 * @author Neo
 * @since 2025/4/16 13:22
 */
@UtilityClass
public class AssembleContext {

    private static final ThreadLocal<Strategy> ASSEMBLE_STRATEGY_HOLDER = ThreadLocal.withInitial(Strategy::new);

    public static void strategy(TrueFalse ignore, TrueFalse override, ExceptionStrategy exception) {
        ASSEMBLE_STRATEGY_HOLDER.set(new Strategy(ignore, override, exception));
    }

    public static TrueFalse ignore() {
        return ASSEMBLE_STRATEGY_HOLDER.get().getIgnore();
    }

    public static TrueFalse override() {
        return ASSEMBLE_STRATEGY_HOLDER.get().getOverride();
    }

    public static ExceptionStrategy exception() {
        return ASSEMBLE_STRATEGY_HOLDER.get().getException();
    }

    public static void clear() {
        ASSEMBLE_STRATEGY_HOLDER.remove();
    }
}
