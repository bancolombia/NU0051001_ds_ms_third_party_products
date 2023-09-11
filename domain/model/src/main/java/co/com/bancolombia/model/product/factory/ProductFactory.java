package co.com.bancolombia.model.product.factory;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.user.Beneficiary;
import co.com.bancolombia.model.user.Customer;
import co.com.bancolombia.model.user.Identification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductFactory {

    public static Product newInstance(Product product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .state(product.getState())
                .number(product.getNumber())
                .type(product.getType())
                .entity(product.getEntity())
                .inscriptionDate(product.getInscriptionDate())
                .functionList(product.getFunctionList())
                .customer(
                        new Customer(
                                new Identification(
                                        product.getCustomer().getIdentification().type(),
                                        product.getCustomer().getIdentification().number()),
                                product.getCustomer().getMdmCode()))
                .beneficiary(
                        new Beneficiary(
                                new Identification(
                                        product.getBeneficiary().getIdentification().type(),
                                        product.getBeneficiary().getIdentification().number()),
                                product.getBeneficiary().getName()))
                .additionalData(product.getAdditionalData())
                .build();
    }
}