package co.com.bancolombia;

import co.com.bancolombia.d2b.raven.core.Homologator;
import co.com.bancolombia.model.user.Identification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class IdentificationHomologator {

    private final Homologator homologator;
    private final Integer homologationId;

    public IdentificationHomologator(Homologator homologator,
                                     @Value("${d2b.raven.homologations[0].id}") Integer homologationId) {
        this.homologator = homologator;
        this.homologationId = homologationId;
    }

    public Mono<String> getHomologatedIdentificationType(Identification identification) {
        return homologator.homologateValueTo(identification.type(), homologationId);
    }
}
