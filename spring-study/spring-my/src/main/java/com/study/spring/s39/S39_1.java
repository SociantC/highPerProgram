package com.study.spring.s39;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S39_1 {
    public static void main(String[] args) {
        SpringApplication.run(S39_1.class, args);
    }

    static class Bean2{}

    @Bean
    public Bean2 bean2() {
        return new Bean2();
    }
}
