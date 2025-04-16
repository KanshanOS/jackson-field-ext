package io.github.kanshanos.jackson.ext.core.context;

import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;

/**
 * 组装上下文
 *
 * @author Neo
 * @since 2025/4/16 13:22
 */
public class AssembleContext {
    private static final ThreadLocal<Strategy> ASSEMBLE_STRATEGY_HOLDER = new ThreadLocal<>();

    public static void strategy(TrueFalse ignore, TrueFalse override, ExceptionStrategy exception) {
        ASSEMBLE_STRATEGY_HOLDER.set(new Strategy(ignore, override, exception));
    }

    public static TrueFalse ignore() {
        Strategy strategy = ASSEMBLE_STRATEGY_HOLDER.get();
        if (strategy != null) {
            return strategy.getIgnore();
        }
        return TrueFalse.DEFAULT;
    }

    public static TrueFalse override() {
        Strategy strategy = ASSEMBLE_STRATEGY_HOLDER.get();
        if (strategy != null) {
            return strategy.getOverride();
        }
        return TrueFalse.DEFAULT;
    }

    public static ExceptionStrategy exception() {
        Strategy strategy = ASSEMBLE_STRATEGY_HOLDER.get();
        if (strategy != null) {
            return strategy.getException();
        }
        return ExceptionStrategy.DEFAULT;
    }

    public static void clear() {
        ASSEMBLE_STRATEGY_HOLDER.remove();
    }
}
