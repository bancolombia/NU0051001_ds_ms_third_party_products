package co.com.bancolombia.event.builder;

import co.com.bancolombia.event.commons.BeneficiaryDTO;
import co.com.bancolombia.event.commons.ProductTypeDTO;
import co.com.bancolombia.event.dto.ProductDeleteDTO;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.user.Beneficiary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductDeleteDTOBuilder {

    public ProductDeleteDTO productDeleteDTOBuilder(Product product, String productTypeName) {
        return new ProductDeleteDTO(product.getId(), product.getName(), product.getEntity(),
                new ProductTypeDTO(product.getType(), productTypeName), product.getNumber(),
                new BeneficiaryDTO(Optional.ofNullable(product.getBeneficiary())
                        .map(Beneficiary::getName).orElse(null),
                        new BeneficiaryDTO.IdentificationDTO(
                                Optional.ofNullable(product.getBeneficiary()).map(beneficiary ->
                                        beneficiary.getIdentification().type()).orElse(null),
                                Optional.ofNullable(product.getBeneficiary()).map(beneficiary ->
                                        beneficiary.getIdentification().number()).orElse(null))));
    }
}
