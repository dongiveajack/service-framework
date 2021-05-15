package com.avail.service_framework.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created By Abhinav Tripathi on 2019-09-13
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(apiKeys())
                .securityContexts(securityContexts())
                .produces(Collections.singleton(MediaType.APPLICATION_JSON_VALUE))
                .consumes(Collections.singleton(MediaType.APPLICATION_JSON_VALUE))
                .enableUrlTemplating(true);
    }

    private List<ApiKey> apiKeys() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("secret", "X-Client-Secret", "header"));
        apiKeys.add(new ApiKey("client_id", "X-Client-Id", "header"));
        apiKeys.add(new ApiKey("requested_by", "X-Requested-By", "header"));
        return apiKeys;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        SecurityContext securityContext = SecurityContext
                .builder()
                .securityReferences(defaultAuth())
                .forPaths(regex("/.*"))
                .build();
        securityContexts.add(securityContext);
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("secret", authorizationScopes));  //Here we use the same key defined in the security scheme oms_key
        securityReferences.add(new SecurityReference("client_id", authorizationScopes));
        securityReferences.add(new SecurityReference("requested_by", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("APIs Documentation")
                .version("1.0")
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("X-Client-Id")
                .clientSecret("X-Auth-Token")
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }
}
