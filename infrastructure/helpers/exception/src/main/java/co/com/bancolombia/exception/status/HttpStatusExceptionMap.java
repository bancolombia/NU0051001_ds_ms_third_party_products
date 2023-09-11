package co.com.bancolombia.exception.status;

import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class HttpStatusExceptionMap {

    private static final Map<String, HttpStatus> httpStatusException;

    static {
        httpStatusException = new HashMap<>();

        httpStatusException
                .put(TechnicalErrorMessage.UNEXPECTED_EXCEPTION.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        httpStatusException
                .put(BusinessErrorMessage.CUSTOMER_DOES_NOT_EXIST.getCode(), HttpStatus.NOT_FOUND);
        httpStatusException
                .put(TechnicalErrorMessage.DATABASE_CONNECTION.getCode(), HttpStatus.CONFLICT);
        httpStatusException
                .put(TechnicalErrorMessage.ERROR_IN_MODIFY_PRODUCT_DATABASE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_PAGE_NUMBER.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_PAGE_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_PRODUCT_TYPE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_IDENTIFICATION_TYPE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_IDENTIFICATION_NUMBER.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_OWNERSHIP.getCode(), HttpStatus.NOT_FOUND);
        httpStatusException
                .put(BusinessErrorMessage.PRODUCT_NOT_EXIST.getCode(), HttpStatus.NOT_FOUND);
        httpStatusException
                .put(BusinessErrorMessage.NON_PRODUCTS_FOUND.getCode(), HttpStatus.NOT_FOUND);
        httpStatusException
                .put(BusinessErrorMessage.MESSAGE_ID_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.SESSION_TRACKER_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUEST_TIMESTAMP_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.CHANNEL_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.USER_AGENT_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.APP_VERSION_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.DEVICE_ID_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.IP_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.PLATFORM_TYPE_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.CUSTOMER_IDENTIFICATION_TYPE_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.CUSTOMER_IDENTIFICATION_NUMBER_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.MDM_CODE_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.CONTENT_TYPE_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.AUTHORIZATION_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.CUSTOM_NAME_UNAVAILABLE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.PRODUCT_INVALID_INFORMATION.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INCORRECT_PRODUCT_FUNCTION.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_DATA_TO_MODIFY.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_NAME.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_PRODUCT_NUMBER.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_ENTITY.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_FUNCTIONS.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_BENEFICIARY.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(TechnicalErrorMessage.PARAMETER_NOT_FOUND.getCode(), HttpStatus.NOT_FOUND);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_PRODUCT_NUMBER_CONTENT.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_IDENTIFICATION_TYPE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_PRODUCT_OBJECT.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_IDENTIFICATION_OBJECT.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.BENEFICIARY_DIFFERENT_THAN_CUSTOMER.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.EMPTY_FUNCTIONS.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_NAME_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_ENTITY_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_PRODUCT_TYPE_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_PRODUCT_NUMBER_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_BENEFICIARY_NAME_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_IDENTIFICATION_NUMBER_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_IDENTIFICATION_TYPE_SIZE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.CUSTOM_NAME_UNAVAILABLE.getCode(), HttpStatus.CONFLICT);
        httpStatusException
                .put(BusinessErrorMessage.INVALID_PRODUCT_NUMBER.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.BENEFICIARY_NAME_IS_REQUIRED.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(BusinessErrorMessage.PRODUCT_EXIST.getCode(), HttpStatus.CONFLICT);
        httpStatusException
                .put(BusinessErrorMessage.MDM_CODE_IS_INCORRECT.getCode(), HttpStatus.NOT_FOUND);
        httpStatusException
                .put(BusinessErrorMessage.REQUIRED_FORMAT.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException.put(BusinessErrorMessage.UNAVAILABLE_PRODUCT_TYPE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException.put(BusinessErrorMessage.INCORRECT_ENTITY_CODE.getCode(), HttpStatus.BAD_REQUEST);
        httpStatusException
                .put(TechnicalErrorMessage.ERROR_GETTING_PRODUCT_TYPES.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static HttpStatus get(String code) {
        return httpStatusException.containsKey(code) ? httpStatusException.get(code) : getDefaultStatus();
    }

    public static HttpStatus getDefaultStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}