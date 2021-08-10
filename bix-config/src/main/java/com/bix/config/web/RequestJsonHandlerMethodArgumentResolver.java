package com.bix.config.web;

import com.alibaba.fastjson.JSONObject;
import com.bix.config.filter.TraceIdFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * <p> web层Controller支持使用RequestJson注解获取json里的指定属性 </p>
 *
 * <pre> Created: 2019/5/20 13:18 </pre>
 *
 * @author  lx
 * @version 1.0
 * @since JDK 1.7
 */
@Component
public class RequestJsonHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 请求头线程变量
     */
    private final ThreadLocal<JSONObject> requestJsonThreadData = new ThreadLocal<>();
    private final ThreadLocal<String> traceIdThreadData = new ThreadLocal<>();

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestJson.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        RequestJson requestJson = parameter.getParameterAnnotation(RequestJson.class);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        synchronized (request) {
            JSONObject jsonObject;
            final JSONObject requestJsonObject = requestJsonThreadData.get();
            final String traceId = MDC.get(TraceIdFilter.TRACE_ID);
            if(!StringUtils.equals(traceIdThreadData.get(),traceId) || StringUtils.isEmpty(traceId)) {
                traceIdThreadData.set(traceId);
                BufferedReader reader = request.getReader();
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[1024];
                int rd;
                while ((rd = reader.read(buf)) != -1) {
                    sb.append(buf, 0, rd);
                }
                jsonObject = JSONObject.parseObject(sb.toString());
                requestJsonThreadData.set(jsonObject);
                reader.close();
            }else{
                jsonObject = requestJsonObject;
            }
            String value = requestJson.value();
            if (StringUtils.isEmpty(value)) {
                value = parameter.getParameterName();
            }

            Object fieldValue = jsonObject.get(value);
            if(requestJson.isRequired()&&(fieldValue==null||"".equals(fieldValue))){
                throw new IllegalArgumentException(value+" is required!");
            }
            return fieldValue;
        }
    }
}
