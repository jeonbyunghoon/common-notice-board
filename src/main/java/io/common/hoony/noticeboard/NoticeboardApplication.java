package io.common.hoony.noticeboard;

import io.common.hoony.noticeboard.config.FileUploadConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//아래의 어노테이션을 통해 스프링부트 어플리케이션이 실행됨과 동시에 설정한 Properties 값들도 설정된다.
@EnableConfigurationProperties({FileUploadConfig.class})
@SpringBootApplication
public class NoticeboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoticeboardApplication.class, args);
    }

}