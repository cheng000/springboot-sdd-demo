package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Knife4j 文档配置
 * <p>
 * 作用：
 * <ol>
 *   <li>定义全局文档信息（标题、版本、作者等）</li>
 *   <li>启动后访问 {@code http://localhost:8080/api/doc.html} 即可打开 Knife4j 增强文档 UI</li>
 *   <li>Knife4j 的「调试」Tab 会自动根据接口定义预填参数，直接点击「发送」即可发起请求</li>
 * </ol>
 * <p>
 * 接口描述、入参示例通过 {@code @Tag} / {@code @Operation} / {@code @Schema} 等注解维护。
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("SpringBoot SDD Demo API")
                        .description("SpringBoot 2.5 + MyBatis + PostgreSQL 三层架构示例工程接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("demo-team")
                                .email("demo@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
