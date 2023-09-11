package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.enums.ProductState;
import co.com.bancolombia.model.product.gateway.ProductManager;
import co.com.bancolombia.usecase.function.FunctionsAffiliateProductUseCase;
import co.com.bancolombia.usecase.product.validations.ProductValidationUseCase;
import lombok.NonNull;
import reactor.core.publisher.Mono;

public class AffiliateProductUseCase implements ProductManager {

    private final FunctionsAffiliateProductUseCase functionsAffiliateProductUseCase;
    private final ProductValidationUseCase productValidationUseCase;

    public AffiliateProductUseCase(@NonNull FunctionsAffiliateProductUseCase functionsAffiliateProductUseCase,
                                   @NonNull ProductValidationUseCase productValidationUseCase) {
        this.functionsAffiliateProductUseCase = functionsAffiliateProductUseCase;
        this.productValidationUseCase = productValidationUseCase;
    }

    @Override
    public Mono<Boolean> validateProduct(Product product, Context context) {
        return productValidationUseCase.isProductValidToEnroll(product);
    }

    public Mono<Product> enrollProduct(@NonNull Product product, Context context) {
        return functionsAffiliateProductUseCase.enrollProduct(product, context)
                .filter(functions -> !functions.isEmpty())
                .map(functions -> {
                    product.setFunctionList(functions);
                    return product;
                })
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        new BusinessException(BusinessErrorMessage.ERROR_IN_ENROLLMENT_OF_PRODUCT_FUNCTIONS))))
                .thenReturn(getProductPendingBankApproval(product));
    }

    @Override
    public Mono<Product> modifyProduct(Product currentProduct, Product newProduct, Context context) {
        return functionsAffiliateProductUseCase.modifyProduct(currentProduct, newProduct, context)
                .map(functions -> {
                    newProduct.setFunctionList(functions);
                    return newProduct;
                }).onErrorReturn(currentProduct);
    }

    private Product getProductPendingBankApproval(@NonNull Product product) {
        product.setState(ProductState.PENDING_APPROVAL.getState());
        return product;
    }
}