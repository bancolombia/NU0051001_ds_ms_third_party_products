package co.com.bancolombia.builders;

import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.user.Beneficiary;
import co.com.bancolombia.model.user.Customer;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class ProductCreator {

    public final String ID = "1";
    public final String NAME = "TEST";
    public final String ACTIVE_STATE = "active";
    public final String NUMBER = "12345";
    public final String TYPE = "MOCK";
    public final String BANK_ENTITY = "MOCK";
    public final Set<Function> FUNCTIONS = Set.of(Function.TRANSFER);
    public final Customer CUSTOMER = CustomerCreator.buildCustomer();
    public final Beneficiary BENEFICIARY = BeneficiaryCreator.buildBeneficiary();
    public final Beneficiary BENEFICIARY_INVALID_TYPE = BeneficiaryCreator.buildBeneficiaryWithInvalidDocumentType();
    public final LocalDateTime INSCRIPTION_DATE = LocalDateTime.now();

    public Product buildNewProduct() {
        return Product.builder()
                .name(NAME)
                .number(NUMBER)
                .type(TYPE)
                .entity(BANK_ENTITY)
                .functionList(FUNCTIONS)
                .customer(CUSTOMER)
                .beneficiary(BENEFICIARY)
                .build();
    }

    public Product buildNewProductWithInvalidBeneficiaryDocumentType() {
        return Product.builder()
                .name(NAME)
                .number(NUMBER)
                .type(TYPE)
                .entity(BANK_ENTITY)
                .functionList(FUNCTIONS)
                .customer(CUSTOMER)
                .beneficiary(BENEFICIARY_INVALID_TYPE)
                .build();
    }

    public Product buildNewModifyProduct() {
        return Product.builder()
                .id(ID)
                .name(NAME)
                .number(NUMBER)
                .state(ACTIVE_STATE)
                .type(TYPE)
                .entity(BANK_ENTITY)
                .functionList(FUNCTIONS)
                .customer(CUSTOMER)
                .beneficiary(BENEFICIARY)
                .build();
    }

    public Product buildEnrolledBankProduct() {
        return Product.builder()
                .id(ID)
                .name(NAME)
                .state(ACTIVE_STATE)
                .number(NUMBER)
                .type(TYPE)
                .entity(BANK_ENTITY)
                .functionList(FUNCTIONS)
                .customer(CUSTOMER)
                .beneficiary(BENEFICIARY)
                .build();
    }
    public Product buildModifyBankProduct() {
        return Product.builder()
                .id(ID)
                .name(NAME)
                .state(ACTIVE_STATE)
                .number(NUMBER)
                .type(TYPE)
                .entity(BANK_ENTITY)
                .functionList(Set.of(Function.PAYROLL_PAYMENT))
                .customer(CUSTOMER)
                .beneficiary(BENEFICIARY)
                .build();
    }

    public Product buildSearchProduct() {
        return Product.builder()
                .name(NAME)
                .state(ACTIVE_STATE)
                .number(NUMBER)
                .type(TYPE)
                .entity(BANK_ENTITY)
                .inscriptionDate(INSCRIPTION_DATE)
                .functionList(Set.of(Function.PAYROLL_PAYMENT))
                .customer(CUSTOMER)
                .beneficiary(BENEFICIARY)
                .build();
    }
}
