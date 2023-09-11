package co.com.bancolombia.rest.product.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "d2b.raven")
public class RavenHomologationProperties {

    private List<Homologation> homologations;


    public record Homologation(String id, String name, List<Data> data) {
    }


    public record Data(String externalSystemValue, String internalContextValue) {
    }

}
