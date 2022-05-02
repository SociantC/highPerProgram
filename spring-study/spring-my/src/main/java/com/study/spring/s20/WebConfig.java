package com.study.spring.s20;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;

/**
 * Created on 2022/5/2
 *
 * @author LSWXHS-CGH
 */
@Configuration
@ComponentScan  //默认扫描配置类所在的包和子包
@PropertySource("classpath:application.properties")
// webMvcProperties(spring.mvc)
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})    // 绑定配置文件键值
public class WebConfig {
    /*
     * 1.内嵌 web容器工厂
     * 2.创建DispatcherServlet
     * 3.注册DispatcherServlet SpringMVC的入口
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties) {
        return new TomcatServletWebServerFactory(serverProperties.getPort());
    }
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
    // 如果使用DispatcherServlet 初始化时候添加的组件，不会作为bean
    // 1. 加入RequestMappingHandlerMapping
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public MyRequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        TokenArgumentResolver tokenArgumentResolver = new TokenArgumentResolver();
        MyRequestMappingHandlerAdapter handlerAdapter = new MyRequestMappingHandlerAdapter();
        YmlReturnValueHandler ymlReturnValueHandler = new YmlReturnValueHandler();

        ArrayList<HandlerMethodArgumentResolver> list = new ArrayList<>();
        list.add(tokenArgumentResolver);
        handlerAdapter.setCustomArgumentResolvers(list);
        ArrayList<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
        returnValueHandlers.add(ymlReturnValueHandler);
        handlerAdapter.setCustomReturnValueHandlers(returnValueHandlers);
        return handlerAdapter;
    }




    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet,WebMvcProperties webMvcProperties) {
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        registrationBean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());   //如果有多个servlet,数字小优先级顺序高 默认-1,代表启动时不需要初始化
        return registrationBean;   // 如果一个请求过来，没有其他servlet路径，所有进行匹配
    }

}
