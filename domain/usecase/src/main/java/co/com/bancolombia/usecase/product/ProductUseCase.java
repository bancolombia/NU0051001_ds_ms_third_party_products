package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductManager;
import co.com.bancolombia.model.product.enums.ProductState;
import co.com.bancolombia.usecase.function.FunctionsProductUseCase;
import co.com.bancolombia.usecase.product.validations.ProductAccountValidationUseCase;
import co.com.bancolombia.usecase.product.validations.ProductValidationUseCase;
import lombok.NonNull;
import reactor.core.publisher.Mono;

public class ProductUseCase implements ProductManager {

    private final FunctionsProductUseCase functionsProductUseCase;
    private final ProductValidationUseCase productValidationUseCase;
    private final ProductAccountValidationUseCase productAccountValidationUseCase;

    public ProductUseCase(@NonNull FunctionsProductUseCase functionsProductUseCase,
                          @NonNull ProductValidationUseCase productValidationUseCase,
                          @NonNull ProductAccountValidationUseCase productAccountValidationUseCase) {
        this.functionsProductUseCase = functionsProductUseCase;
        this.productValidationUseCase = productValidationUseCase;
        this.productAccountValidationUseCase = productAccountValidationUseCase;
    }

    @Override
    public Mono<Boolean> validateProduct(Product product, Context context) {
        return productValidationUseCase.isProductValidToEnroll(product)
                .then(productAccountValidationUseCase.validateOwnership(product.getNumber(), product.getType(),
                        product.getBeneficiary().getIdentification(), context));
    }

    @Override
    public Mono<Product> enrollProduct(Product product, Context context) {
        return functionsProductUseCase.enrollProduct(product, context)
                .filter(functions -> !functions.isEmpty())
                .map(functions -> {
                    product.setFunctionList(functions);
                    return product;
                })
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        new BusinessException(BusinessErrorMessage.ERROR_IN_ENROLLMENT_OF_PRODUCT_FUNCTIONS))))
                .thenReturn(activateProduct(product));
    }

    @Override
    public Mono<Product> modifyProduct(Product currentProduct, Product newProduct, Context context) {
        return functionsProductUseCase.modifyProduct(currentProduct, newProduct, context )
                .map(functions -> {
                    newProduct.setFunctionList(functions);
                    return newProduct;
                }).onErrorReturn(currentProduct);
    }

    private Product activateProduct(Product product) {
        product.setState(ProductState.ACTIVE.getState());
        return product;
    }
}