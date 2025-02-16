package gr.hua.dit.ds.housingsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Prevent Spring from treating "/images/**" as static resources
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/home/alexj/property_photos/");
    }
}

