package io.github.kanshanos.jackson.ext.core.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.github.kanshanos.jackson.ext.core.annotation.AssembleSpEL;
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
        Object extFieldValue = evaluateSpEL(value, annotation.expression(), annotation.clazz());
        serializeWithOverrideCheck(value, extFieldName, extFieldValue, gen, serializers, annotation.override());
    }

    private Object evaluateSpEL(Object value, String expression, Class<?> targetType) {
        Expression exp = parser.parseExpression(expression);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("value", value);
        return exp.getValue(context, targetType);
    }
}
