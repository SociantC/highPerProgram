package com.study.spring.s31;

import org.apache.ibatis.util.MapUtil;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebConfig {
//    @ControllerAdvice
//    static class MyControllerAdvice{
//        @ExceptionHandler
//        @ResponseBody
//        public Map<String, Object> handle(Exception e) {
//            Map<String, Object> result = new HashMap<>();
//            result.put("error", e.getMessage());
//            return result;
//        }
//    }

    @Bean
    public TomcatServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }


    @Bean
    public DispatcherServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean   //默认RequestMappingHandlerAdapter不带jackson 调用控制前
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        return requestMappingHandlerAdapter;
    }

    @Bean   // ⬇️修改了tomcat默认的错误地址
    public ErrorPageRegistrar errorPageRegistrar() {    // 使用请求转发的方式进行跳转
        return servletWebServerFactory -> servletWebServerFactory.addErrorPages(new ErrorPage("/error"));
    }

    /**
     * servletWebServerFactory 初始化之前执行 errorPageRegistrarBeanPostProcessor
     * 在容器中找到所有 errorPageRegistrar
     * 回调并创建 errorPage
     * @return
     */
    @Bean
    public ErrorPageRegistrarBeanPostProcessor errorPageRegistrarBeanPostProcessor() {
        return new ErrorPageRegistrarBeanPostProcessor();
    }

    @Controller
    static class Controller1{
        @RequestMapping("test")
        public ModelAndView test() {
            int i = 1 / 0;
            return null;


        }


//        @RequestMapping("error")
//        @ResponseBody
//        public Map<String, Object> error(HttpServletRequest request) {
//            Map<String, Object> result = new HashMap<>();
//            Throwable error = ((Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
//            result.put("error", error.getMessage());
//
//            return result;
//        }
    }

    @Bean
    public BasicErrorController basicErrorController() {
        ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setIncludeException(true );
        return new BasicErrorController(new DefaultErrorAttributes(), errorProperties);
    }


    @Bean
    public View error(){
        return new View() {
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                // ⬅️
                System.out.println(model);
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().print("<h3>服务器内部错误</h3>");
            }
        };
    }


    @Bean
    public ViewResolver viewResolver() {
        return new BeanNameViewResolver();
    }
}
