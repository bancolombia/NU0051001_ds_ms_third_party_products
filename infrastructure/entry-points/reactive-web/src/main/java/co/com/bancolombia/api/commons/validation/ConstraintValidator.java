package co.com.bancolombia.api.commons.validation;

import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ConstraintValidator {

    private final Validator validator;
    private static final String REGEX_MESSAGE_ERROR = "%";

    public <T> Mono<T> validateData(T data) {
        return Mono.just(data)
                .map(validator::validate)
                .map(this::evaluateValidations)
                .onErrorMap(ConstraintViolationException.class, this::getBusinessException)
                .map(Set::isEmpty)
                .thenReturn(data);
    }

    public BusinessException getBusinessException(ConstraintViolationException cons) {
        String[] message = cons.getMessage().split(REGEX_MESSAGE_ERROR);
        return new BusinessException(BusinessErrorMessage.valueOf(message[1]));
    }

    public <T> Set<ConstraintViolation<T>> evaluateValidations(Set<ConstraintViolation<T>> constraint) {
        if (!constraint.isEmpty()) {
            throw new ConstraintViolationException(constraint);
        } else {
            return constraint;
        }
    }
}
