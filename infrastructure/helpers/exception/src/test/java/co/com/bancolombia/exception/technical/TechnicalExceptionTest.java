package co.com.bancolombia.exception.technical;

import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TechnicalExceptionTest {

    @Test
    void shouldReturnATechnicalErrorWhenUnexpectedExceptionIsThrown() {
        TechnicalException technicalException = new TechnicalException(new Throwable(),
                TechnicalErrorMessage.UNEXPECTED_EXCEPTION);

        assertEquals(TechnicalErrorMessage.UNEXPECTED_EXCEPTION, technicalException.getTechnicalErrorMessage());
        assertEquals(technicalException.getMessage(), TechnicalErrorMessage.UNEXPECTED_EXCEPTION.getMessage());
        assertEquals(technicalException.getTechnicalErrorMessage().getCode(),
                TechnicalErrorMessage.UNEXPECTED_EXCEPTION.getCode());
        assertEquals(TechnicalException.class.getSuperclass(), RuntimeException.class);
    }

}