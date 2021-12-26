package info.stepanoff.trsis.samples.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class DatabaseProperties {

    private String uri;
    private String database;
    private String host;
    private String port;
    private String password;
    private String username;
    private String authenticationDatabase;

}
