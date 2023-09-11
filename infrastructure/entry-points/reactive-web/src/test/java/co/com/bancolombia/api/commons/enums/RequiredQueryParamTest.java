package co.com.bancolombia.api.commons.enums;

import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequiredQueryParamTest {

    @ParameterizedTest
    @CsvSource({
            "PRODUCT_TYPE, product[type], REQUIRED_PRODUCT_TYPE",
            "IDENTIFICATION_TYPE, identification[type], REQUIRED_IDENTIFICATION_TYPE",
            "IDENTIFICATION_NUMBER, identification[number], REQUIRED_IDENTIFICATION_NUMBER",
    })
    void shouldValidateBusinessErrorMessageSuccessfully(String enumName, String value, String businessErrorMessage) {
        RequiredQueryParam requiredQueryParam = RequiredQueryParam.valueOf(enumName);
        assertEquals(value, requiredQueryParam.getValue());
        assertEquals(requiredQueryParam.getBusinessErrorMessage(), BusinessErrorMessage.valueOf(businessErrorMessage));
    }
}
