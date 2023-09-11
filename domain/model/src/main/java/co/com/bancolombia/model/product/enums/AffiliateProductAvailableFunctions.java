package co.com.bancolombia.model.product.enums;

import co.com.bancolombia.model.commons.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * Types of products available that can be handled and managed by the other banks.
 */
@Getter
@RequiredArgsConstructor
public enum AffiliateProductAvailableFunctions {

    SAVING_ACCOUNT(Set.of(Function.values())),
    CURRENT_ACCOUNT(Set.of(Function.values()));

    private final Set<Function> functions;
}
