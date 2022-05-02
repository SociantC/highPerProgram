package com.study.spring.s20;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2022/5/2
 *
 * @author LSWXHS-CGH
 */
public class YmlReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Yml annotation = returnType.getMethodAnnotation(Yml.class);
        return annotation != null;
    }

    /**
     *
     * @param returnValue 返回值
     * @param returnType
     * @param mavContainer
     * @param webRequest
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object returnValue
            , MethodParameter returnType
            , ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        // 转换返回结果为yml
        String str = new Yaml().dump(returnValue);
        // 写入响应
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("text/plain;charset=utf-8");
        response.getWriter().print(str);



    }
}
