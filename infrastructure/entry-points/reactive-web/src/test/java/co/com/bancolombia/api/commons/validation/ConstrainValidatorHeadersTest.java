package co.com.bancolombia.api.commons.validation;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;
import org.mockito.Mockito;
import jakarta.validation.ConstraintViolation;
import reactor.test.StepVerifier;
import java.util.Collections;
import java.util.Set;
import static org.mockito.Mockito.when;

class ConstrainValidatorHeadersTest {

    private static  final String validData = "";

    @InjectMocks
    private ConstraintValidator constraintValidator;

    @Mock
    private Validator validator;


    @BeforeEach
    void setUp() {
        openMocks(this);
        constraintValidator = new ConstraintValidator(validator);

    }

    @Test
    void testValidateDataWithValidData() {
        Mono<String> result = constraintValidator.validateData(validData);
        assertEquals(validData, result.block());
    }

    @Test
    void testValidateDataFailed() {

        ConstraintViolation<String> violation = Mockito.mock(ConstraintViolation.class);
        Set<ConstraintViolation<String>> violations = Collections.singleton(violation);

        when(validator.validate("")).thenReturn(violations);

        var result = constraintValidator.validateData("");
        StepVerifier.create(result)
                .expectError(ArrayIndexOutOfBoundsException.class)
                .verify();


    }


}