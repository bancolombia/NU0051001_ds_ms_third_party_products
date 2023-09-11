package co.com.bancolombia.api.commons.enums;

import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequiredPathVariableTest {

    @ParameterizedTest
    @CsvSource({
            "PRODUCT_NUMBER, productNumber, INVALID_PRODUCT_NUMBER",
    })
    void shouldValidateBusinessErrorMessageSuccessfully(String enumName, String value, String businessErrorMessage) {
        RequiredPathVariable requiredPathVariable = RequiredPathVariable.valueOf(enumName);
        assertEquals(value, requiredPathVariable.getValue());
        assertEquals(requiredPathVariable.getBusinessErrorMessage(), BusinessErrorMessage.valueOf(businessErrorMessage));
    }
}
