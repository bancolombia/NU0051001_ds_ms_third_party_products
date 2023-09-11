package co.com.bancolombia.exception.status;

import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpStatusExceptionMapTest {

    @Test
    void shouldReturnAnInternalServerErrorWhenNoErrorCodeExists() {
        HttpStatus status = HttpStatusExceptionMap.get("NonExistingErrorCode");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status);
    }

    @Test
    void shouldGetTechnicalStatusSuccessfully() {
        assertTrue(getHttpStatus(TechnicalErrorMessage.UNEXPECTED_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR));
        assertTrue(getHttpStatus(TechnicalErrorMessage.DATABASE_CONNECTION,
                HttpStatus.CONFLICT));
        assertTrue(getHttpStatus(TechnicalErrorMessage.ERROR_IN_MODIFY_PRODUCT_DATABASE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(TechnicalErrorMessage.ERROR_GETTING_PRODUCT_TYPES,
                HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    void shouldGetBusinessStatusSuccessfully() {
        assertTrue(getHttpStatus(BusinessErrorMessage.CUSTOMER_DOES_NOT_EXIST,
                HttpStatus.NOT_FOUND));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_PAGE_NUMBER,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_PAGE_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_PRODUCT_TYPE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_IDENTIFICATION_TYPE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_IDENTIFICATION_NUMBER,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_OWNERSHIP,
                HttpStatus.NOT_FOUND));
        assertTrue(getHttpStatus(BusinessErrorMessage.PRODUCT_NOT_EXIST,
                HttpStatus.NOT_FOUND));
        assertTrue(getHttpStatus(BusinessErrorMessage.PRODUCT_INVALID_INFORMATION,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INCORRECT_PRODUCT_FUNCTION,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_DATA_TO_MODIFY,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_PRODUCT_NUMBER,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.BENEFICIARY_NAME_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.UNAVAILABLE_PRODUCT_TYPE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INCORRECT_ENTITY_CODE,
                HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldGetHeaderStatusSuccessfully() {
        assertTrue(getHttpStatus(BusinessErrorMessage.MESSAGE_ID_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.SESSION_TRACKER_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUEST_TIMESTAMP_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.CHANNEL_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.USER_AGENT_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.APP_VERSION_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.DEVICE_ID_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.IP_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.PLATFORM_TYPE_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.CUSTOMER_IDENTIFICATION_TYPE_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.CUSTOMER_IDENTIFICATION_NUMBER_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.MDM_CODE_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldGetStatusSuccessfullySecond() {
        assertTrue(getHttpStatus(BusinessErrorMessage.CONTENT_TYPE_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.AUTHORIZATION_IS_REQUIRED,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_NAME,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_PRODUCT_NUMBER,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_ENTITY,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_FUNCTIONS,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.REQUIRED_BENEFICIARY,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_NAME_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_ENTITY_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_PRODUCT_TYPE_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_PRODUCT_NUMBER_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_BENEFICIARY_NAME_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_IDENTIFICATION_NUMBER_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.INVALID_IDENTIFICATION_TYPE_SIZE,
                HttpStatus.BAD_REQUEST));
        assertTrue(getHttpStatus(BusinessErrorMessage.CUSTOM_NAME_UNAVAILABLE,
                HttpStatus.CONFLICT));
    }

    @Test
    void shouldGetDefaultStatusSuccessfully() {
        HttpStatus defaultStatus = HttpStatusExceptionMap.getDefaultStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, defaultStatus);
    }

    private Boolean getHttpStatus(TechnicalErrorMessage technicalErrorMessage, HttpStatus httpStatus) {
        HttpStatus status = HttpStatusExceptionMap.get(technicalErrorMessage.getCode());
        return httpStatus.equals(status);
    }

    private Boolean getHttpStatus(BusinessErrorMessage businessErrorMessage, HttpStatus httpStatus) {
        HttpStatus status = HttpStatusExceptionMap.get(businessErrorMessage.getCode());
        return httpStatus.equals(status);
    }
}