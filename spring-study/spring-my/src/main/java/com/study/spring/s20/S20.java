package com.study.spring.s20;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Created on 2022/5/2
 *
 * @author LSWXHS-CGH
 */
public class S20 {
    private static final Logger log = LoggerFactory.getLogger(S20.class);


    public static void main(String[] args) throws Exception {
        // AnnotationConfig：java 配置类构建
        // ServletWebServer: 支持内嵌容器
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        // 解析@RequestMapping 注解和派生注解，生成路径与控制器方法之间的映射关系，在bean初始化时候就生成
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        // 获取映射结果 HandlerMethod 控制器方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            System.out.println(entry.getKey()+"="+entry.getValue());
        }

        // 返回处理器执行链对象
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test4");
        request.setParameter("name","张三");
        request.addHeader("token","1令牌");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecutionChain chain = handlerMapping
                .getHandler(request);
        System.out.println(">>>>>>>>>>>>>");
        MyRequestMappingHandlerAdapter adapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        adapter.invokeHandlerMethod(request, response, (HandlerMethod) chain.getHandler());

        // 检查响应
        byte[] content = response.getContentAsByteArray();
        System.out.println(new String(content, StandardCharsets.UTF_8));

        /*System.out.println(">>>>>>>>>>>>>");
        List<HandlerMethodArgumentResolver> resolvers = adapter.getArgumentResolvers();
        for (HandlerMethodArgumentResolver resolver : resolvers) {
            System.out.println(resolver);
        }

        System.out.println(">>>>>>>>>>>>>返回值解析器");
        for (HandlerMethodReturnValueHandler returnValueHandler : adapter.getReturnValueHandlers()) {
            System.out.println(returnValueHandler);

        }*/


    }
}
