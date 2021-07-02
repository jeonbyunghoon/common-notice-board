//package io.common.hoony.noticeboard.config;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import log.munzi.interceptor.LoggingInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@RequiredArgsConstructor
//@Configuration
//public class LoggingConfig {
//
//    private final ObjectMapper objectMapper;
//
//    @Bean
//    public LoggingInterceptor loggingInterceptor() {
//        return new LoggingInterceptor(objectMapper);
//    }
//}

package io.common.hoony.noticeboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import log.munzi.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class LoggingConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor(objectMapper);
    }
}