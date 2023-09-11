package co.com.bancolombia.api.commons.enums;

import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RequiredQueryParam {

    PRODUCT_TYPE("product[type]", BusinessErrorMessage.REQUIRED_PRODUCT_TYPE),
    IDENTIFICATION_TYPE("identification[type]", BusinessErrorMessage.REQUIRED_IDENTIFICATION_TYPE),
    IDENTIFICATION_NUMBER("identification[number]", BusinessErrorMessage.REQUIRED_IDENTIFICATION_NUMBER),
    PRODUCT_ID("productId", BusinessErrorMessage.REQUIRED_PRODUCT_ID);

    private final String value;
    private final BusinessErrorMessage businessErrorMessage;
}
