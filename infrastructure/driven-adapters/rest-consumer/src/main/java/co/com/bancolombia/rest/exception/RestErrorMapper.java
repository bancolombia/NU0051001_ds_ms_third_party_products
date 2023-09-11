package co.com.bancolombia.rest.exception;

import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import org.springframework.stereotype.Component;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;

import java.util.HashMap;
import java.util.Map;

@Component
public class RestErrorMapper {

    private final Map<String, TechnicalErrorMessage> technicalErrorMessageCodes;
    private final Map<String, BusinessErrorMessage> businessErrorMessageCodes;

    public RestErrorMapper() {
        technicalErrorMessageCodes = new HashMap<>();
        technicalErrorMessageCodes.put("CTB0014", TechnicalErrorMessage.PARAMETER_NOT_FOUND);
        businessErrorMessageCodes = new HashMap<>();
        businessErrorMessageCodes.put("BP12700025", BusinessErrorMessage.CUSTOMER_DOES_NOT_EXIST);
        businessErrorMessageCodes.put("BP12900003", BusinessErrorMessage.PRODUCT_NOT_EXIST);
        businessErrorMessageCodes.put("SA400", BusinessErrorMessage.PRODUCT_NOT_EXIST);
    }

    public TechnicalErrorMessage getTechnicalErrorMessage(String code) {
        return technicalErrorMessageCodes.getOrDefault(code, TechnicalErrorMessage.REST_CLIENT_EXCEPTION);
    }

    public BusinessErrorMessage getBusinessErrorMessage(String code) {
        return businessErrorMessageCodes.get(code);
    }

}

