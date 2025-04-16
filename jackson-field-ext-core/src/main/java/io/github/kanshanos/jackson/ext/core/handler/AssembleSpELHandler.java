package io.github.kanshanos.jackson.ext.core.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.github.kanshanos.jackson.ext.core.annotation.AssembleSpEL;
import io.github.kanshanos.jackson.ext.core.enums.ExceptionStrategy;
import io.github.kanshanos.jackson.ext.core.enums.TrueFalse;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;

/**
 * 处理 AssembleSpEL 注解
 *
 * @author Neo
 * @since 2025/3/26 16:45
 */
public class AssembleSpELHandler extends AbstractAssembleHandler<AssembleSpEL> {

    private final ExpressionParser parser = new SpelExpressionParser();


    @Override
    protected AssembleSpEL getAnnotation(BeanProperty property) {
        return property.getAnnotation(AssembleSpEL.class);
    }

    @Override
    protected void doSerialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String extFieldName = resolveExtFieldName(annotation.ext());
        Object extFieldValue = evaluateSpEL(value, annotation.expression());
        serializeWithOverrideStrategy(value, extFieldName, extFieldValue, gen, serializers);
    }

    @Override
    protected TrueFalse getAnnotationOverrideStrategy() {
        return annotation.override();
    }

    @Override
    protected ExceptionStrategy getAnnotationExceptionStrategy() {
        return annotation.exception();
    }

    private Object evaluateSpEL(Object value, String expression) {
        Expression exp = parser.parseExpression(expression);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("value", value);
        return exp.getValue(context);
    }
}
