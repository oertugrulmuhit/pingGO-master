package org.oem.pinggo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("BOOKZ API")
                        .description("Rest api for crud operations.")
                        .version("1.0").contact(new Contact().name("Osman Ertugrul").email("oertugrulmuhit@gmail.com").url("https://github.com/oertugrulmuhit/pingGO"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }
}