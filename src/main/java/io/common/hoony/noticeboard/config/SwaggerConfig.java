package io.common.hoony.noticeboard.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

//    private final BuildProperties buildProperties;

    private String version;
    private String title;

    @Bean
    public Docket apiV1() {
        version = getFullVersion();
        title = "Noticeboard - API";
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.common.hoony.noticeboard"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo(title,version))
                .globalResponseMessage(RequestMethod.GET,getResponseMessage());
    }
    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "Noticeboard - API",
                version,
                "dkargo.io",
                new Contact("jeon","jeon.io","help@jeon.io"),
                "Licenses",
                "dkargo.io",
                new ArrayList<>()
        );
    }
    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        consumes.add("application/octet-stream;charset=UTF-8");
        consumes.add("multipart/form-data");
        return consumes;
    }
    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        produces.add("application/octet-stream;charset=UTF-8");
        produces.add("multipart/form-data");
        return produces;
    }
    private List<ResponseMessage> getResponseMessage() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(
                new ResponseMessageBuilder()
                        .code(200)
                        .message("OK!")
                        .build()
        );
        responseMessages.add(
                new ResponseMessageBuilder()
                        .code(400)
                        .message("Bad Request!")
                        .build()
        );
        responseMessages.add(
                new ResponseMessageBuilder()
                        .code(404)
                        .message("Not Found!")
                        .build()
        );
        responseMessages.add(
                new ResponseMessageBuilder()
                        .code(500)
                        .message("Internal Server Error!")
                        .build()
        );
        return responseMessages;
    }
    private String getFullVersion() {
//        return buildProperties.get("fullVersion");
        return null;
    }
}
