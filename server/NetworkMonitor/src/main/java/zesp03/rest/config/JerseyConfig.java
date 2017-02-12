package zesp03.rest.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("zesp03.rest.resource");
        register(MyObjectMapperProvider.class);
        register(JacksonFeature.class);
    }
}
