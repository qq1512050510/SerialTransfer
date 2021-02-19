package com.lingyue.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("spring-test-interface")
                .apiInfo(apiInfo())
                .globalOperationParameters(globalParamBuilder())
                .globalResponseMessage(RequestMethod.GET,
                        responseBuilder())
                .globalResponseMessage(RequestMethod.POST,
                        responseBuilder())
                .globalResponseMessage(RequestMethod.PUT,
                        responseBuilder())
                .globalResponseMessage(RequestMethod.DELETE,
                        responseBuilder())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lingyue"))
                .paths(PathSelectors.any()).build()
                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("jyp", "", "jyp.beijing@qq.com");
        return new ApiInfoBuilder().title("星芒灯光测试版本").description("dev by lingyue").contact(contact)
                .version("1.0.0")
                .build();
    }

    private List<ApiKey> security() {
        List list = new ArrayList();
        list.add(new ApiKey("token", "token", "header"));
        return list;
    }

    private List<Parameter> globalParamBuilder() {
        List pars = new ArrayList();
        pars.add(parameterBuilder("token", "令牌", "string", "header", false).build());
        return pars;
    }

    private ParameterBuilder parameterBuilder(String name, String desc, String type, String parameterType, boolean required) {
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name(name).description(desc).modelRef(new ModelRef(type)).parameterType(parameterType).required(required).build();
        return tokenPar;
    }

    private List<ResponseMessage> responseBuilder() {
        List responseMessageList = new ArrayList();
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("响应成功").build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").build());
        return responseMessageList;
    }
}