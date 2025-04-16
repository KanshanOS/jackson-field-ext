package io.github.kanshanos.jackson.ext.core.interceptor;

import io.github.kanshanos.jackson.ext.core.annotation.AssembleStrategy;
import io.github.kanshanos.jackson.ext.core.context.AssembleContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 组装拦截器
 *
 * @author Neo
 * @since 2025/4/16 13:49
 */
public class AssembleInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (handler instanceof HandlerMethod handlerMethod) {

                // 优先获取方法上的注解，其次类上的注解
                AssembleStrategy annotation = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), AssembleStrategy.class);
                if (annotation == null) {
                    annotation = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), AssembleStrategy.class);
                }

                if (annotation != null) {
                    AssembleContext.strategy(annotation.ignore(), annotation.override(), annotation.exception());
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AssembleContext.clear();
    }
}
