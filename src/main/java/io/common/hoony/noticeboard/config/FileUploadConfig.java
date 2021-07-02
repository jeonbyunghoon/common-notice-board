package io.common.hoony.noticeboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//application.properties, yml 에 설정된 file.upload 로 시작하는 설정값을  가져온다.
//location 변수에 설정한 값이 지정된다.
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
