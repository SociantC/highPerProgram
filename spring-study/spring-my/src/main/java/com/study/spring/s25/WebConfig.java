package com.study.spring.s25;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class WebConfig {
    @Controller
    static class Controller1 {
        @ResponseStatus(HttpStatus.OK)  // 测试使用，可以暂时不考虑ReturnValueHandlerComposite
        public ModelAndView foo(User user){ //modelAttribute封装
            System.out.println("foo");
            return null;
        }
    }


    static class User{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
