package co.com.bancolombia.api.commons.enums;

import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RequiredPathVariable {
    PRODUCT_NUMBER("productNumber", BusinessErrorMessage.INVALID_PRODUCT_NUMBER);

    private final String value;
    private final BusinessErrorMessage businessErrorMessage;
}
