package co.com.bancolombia.api.commons;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ContextDTOTest {

    @Test
    void testValidationSuccess() {
        ContextDTO contextDTO = ContextDTO.builder()
                .id("1")
                .sessionId("session123")
                .requestDate("2023-06-28")
                .channel("web")
                .domain("example.com")
                .agent(ContextDTO.Agent.builder()
                        .name("John Doe")
                        .appVersion("1.0.0")
                        .build())
                .device(ContextDTO.Device.builder()
                        .id("device123")
                        .ip("127.0.0.1")
                        .type("mobile")
                        .build())
                .customer(ContextDTO.Customer.builder()
                        .identification(ContextDTO.Customer.Identification.builder()
                                .type("passport")
                                .number("AB123456")
                                .build())
                        .mdmCode("mdmCode")
                        .build())
                .contentType("application/json")
                .authorization("Bearer token123")
                .build();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ContextDTO>> violations = validator.validate(contextDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidationFailure() {

        ContextDTO contextDTO = ContextDTO.builder()
                .id("")
                .sessionId("session123")
                .requestDate("2023-06-28")
                .channel("web")
                .domain("example.com")
                .agent(ContextDTO.Agent.builder()
                        .name("")
                        .appVersion("1.0.0")
                        .build())
                .device(ContextDTO.Device.builder()
                        .id("device123")
                        .ip("127.0.0.1")
                        .type("mobile")
                        .build())
                .customer(ContextDTO.Customer.builder()
                        .identification(ContextDTO.Customer.Identification.builder()
                                .type("passport")
                                .number("")
                                .build())
                        .build())
                .contentType("application/json")
                .authorization("Bearer token123")
                .build();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ContextDTO>> violations = validator.validate(contextDTO);

        assertThat(violations).isNotEmpty();

    }
}
