package gr.hua.dit.ds.housingsystem.config;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.entities.model.Photo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {

        // Expose IDs for all entities
        /*
        config.exposeIdsFor(AppUser.class);
        config.exposeIdsFor(Property.class);
        config.exposeIdsFor(RentalRequest.class);
        config.exposeIdsFor(ViewingRequest.class);
        config.exposeIdsFor(Photo.class);
        */

        // Configure CORS for the frontend
        cors.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
