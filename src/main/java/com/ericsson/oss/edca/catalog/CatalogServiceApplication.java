/*******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 * The copyright to the computer program(s) herein is the property of
 *
 * Ericsson Inc. The programs may be used and/or copied only with written
 *
 * permission from Ericsson Inc. or in accordance with the terms and
 *
 * conditions stipulated in the agreement/contract under which the
 *
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.oss.edca.catalog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CatalogServiceApplication.class);
    }

    @Bean
    public Docket swaggerDocs(@Value("${info.app.name}") final String title, @Value("${info.app.version}") final String version, @Value("${info.app.description}") final String description) {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo(title, description, version)).useDefaultResponseMessages(false).select()
                .apis(RequestHandlerSelectors.basePackage("com.ericsson.oss.edca.catalog")).build();
    }

    private ApiInfo getApiInfo(final String title, final String description, final String version) {
        return new ApiInfoBuilder().title(title).version(version).description(description).build();
    }

}
