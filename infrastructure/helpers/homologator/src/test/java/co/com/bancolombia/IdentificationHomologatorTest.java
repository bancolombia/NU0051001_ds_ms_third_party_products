package co.com.bancolombia;

import co.com.bancolombia.d2b.raven.core.Homologator;
import co.com.bancolombia.model.user.Identification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IdentificationHomologatorTest {

    private Homologator homologator;
    private IdentificationHomologator identificationHomologator;
    private Integer homologationId = 1;

    @BeforeEach
    void setUp() {
        homologator = mock(Homologator.class);
        identificationHomologator = new IdentificationHomologator(homologator, homologationId);
    }

    @Test
    void shouldGetHomologatedIdenticationSuccessfully() {
        var homologatedIdentificationType = "CC";
        Identification identification = new Identification("", "TIPDOC_FS001");

        when(homologator.homologateValueTo(identification.type(), homologationId))
                .thenReturn(Mono.just(homologatedIdentificationType));

        StepVerifier.create(identificationHomologator.getHomologatedIdentificationType(identification))
                .expectNext(homologatedIdentificationType)
                .verifyComplete();
    }
}