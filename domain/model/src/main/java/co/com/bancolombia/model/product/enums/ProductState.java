package co.com.bancolombia.model.product.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Possible states that a product can have.
 */
@Getter
@RequiredArgsConstructor
public enum ProductState {

    ACTIVE("active"),
    PENDING_APPROVAL("pending approval");

    private final String state;
}
