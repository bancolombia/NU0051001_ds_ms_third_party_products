package co.com.bancolombia.usecase.product.validations;

import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.entity.EntityAdapter;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductRepository;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;

public class ProductValidationUseCase {

    private final ProductRepository productRepository;
    private final String BANK_CODE;
    private ProductTypeAdapter productTypeAdapter;
    private EntityAdapter entityAdapter;


    public ProductValidationUseCase(@NonNull EntityAdapter entityAdapter,
                                    @NonNull ProductTypeAdapter productTypeAdapter,
                                    @NonNull ProductRepository productRepository) {
        if (Objects.isNull(entityAdapter.getBankCode())) {
            throw new BusinessException(BusinessErrorMessage.INCORRECT_ENTITY_CODE);
        }
        this.productTypeAdapter = productTypeAdapter;
        this.entityAdapter = entityAdapter;
        this.productRepository = productRepository;
        this.BANK_CODE = entityAdapter.getBankCode();
    }

    public Boolean isProductValidForCreation(Product product) {
        Objects.requireNonNull(product.getName(), BusinessErrorMessage.REQUIRED_NAME.getMessage());
        Objects.requireNonNull(product.getNumber(), BusinessErrorMessage.REQUIRED_PRODUCT_NUMBER.getMessage());
        Objects.requireNonNull(product.getType(), BusinessErrorMessage.REQUIRED_PRODUCT_TYPE.getMessage());
        Objects.requireNonNull(product.getEntity(), BusinessErrorMessage.REQUIRED_ENTITY.getMessage());
        Objects.requireNonNull(product.getFunctionList(), BusinessErrorMessage.REQUIRED_FUNCTIONS.getMessage());
        if (product.getFunctionList().isEmpty()) {
            throw new BusinessException(BusinessErrorMessage.EMPTY_FUNCTIONS);
        }
        Objects.requireNonNull(product.getCustomer(), BusinessErrorMessage.REQUIRED_CUSTOMER.getMessage());
        Objects.requireNonNull(product.getBeneficiary(), BusinessErrorMessage.REQUIRED_BENEFICIARY.getMessage());
        if (product.getCustomer().getIdentification().equals(product.getBeneficiary().getIdentification())) {
            throw new BusinessException(BusinessErrorMessage.BENEFICIARY_DIFFERENT_THAN_CUSTOMER);
        }
        return Boolean.TRUE;
    }

    public Mono<Boolean> isProductEntityFromTheBank(Product product) {
        return Mono.just(BANK_CODE.equals(product.getEntity()))
                .filter(Boolean.TRUE::equals)
                .flatMap(success -> Mono.just(Boolean.TRUE))
                .switchIfEmpty(entityAdapter.getAffiliateCodes()
                        .map(result -> result.contains(product.getEntity()))
                        .filter(Boolean.TRUE::equals)
                        .flatMap(success -> Mono.just(Boolean.FALSE))
                        .switchIfEmpty(Mono.defer(() ->
                                Mono.error(new BusinessException(BusinessErrorMessage.INCORRECT_ENTITY_CODE)))));
    }

    public Mono<Boolean> isProductTypeAuthorized(Product product) {
        return productTypeAdapter.getProductTypes()
                .mapNotNull(productTypes -> productTypes.get(product.getType()))
                .map(productType -> Boolean.TRUE)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new BusinessException((BusinessErrorMessage.UNAVAILABLE_PRODUCT_TYPE)))));
    }

    public Mono<Boolean> isProductValidToEnroll(Product product) {
        return Mono.just(this.isProductValidForCreation(product))
                .then(this.isProductTypeAuthorized(product))
                .then(this.isIdentificationTypeValid(product.getBeneficiary().getIdentification().type()))
                .then(this.isIdentificationTypeValid(product.getCustomer().getIdentification().type()))
                .filterWhen(valid -> productRepository.isCustomerValid(product.getCustomer()))
                .filterWhen(valid -> productRepository.isCustomNameAvailable(product.getCustomer(), product.getName()))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new BusinessException(BusinessErrorMessage.CUSTOM_NAME_UNAVAILABLE))))
                .filterWhen(valid -> productRepository.isProductRegistered(product))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new BusinessException(BusinessErrorMessage.PRODUCT_EXIST))));
    }

    private Mono<Boolean> isIdentificationTypeValid(String documentType) {
        boolean idTypeMatches = productTypeAdapter.getIdentificationTypes().stream().anyMatch(documentType::equals);
        if (!idTypeMatches) {
            throw new BusinessException((BusinessErrorMessage.INVALID_IDENTIFICATION_TYPE));
        }
        return Mono.just(Boolean.TRUE);
    }


    public Boolean isNewInformationProduct(Product product, String customName, Set<Function> functions) {
        if ((product.getName().equals(customName) || product.getFunctionList().equals(functions))) {
            throw new BusinessException((BusinessErrorMessage.PRODUCT_INVALID_INFORMATION));
        }
        return Boolean.TRUE;
    }

}
