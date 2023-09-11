package co.com.bancolombia.event.builder;

import co.com.bancolombia.event.commons.BeneficiaryDTO;
import co.com.bancolombia.event.commons.FunctionDTO;
import co.com.bancolombia.event.commons.ProductTypeDTO;
import co.com.bancolombia.event.dto.ProductModifyDTO;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.user.Beneficiary;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductModifyDTOBuilder {

    public ProductModifyDTO productModifyDTOBuilder(Product product, String productTypeName) {
        return new ProductModifyDTO(product.getId(), product.getName(), product.getEntity(),
                new ProductTypeDTO(product.getType(), productTypeName),
                product.getNumber(), Optional.ofNullable(product.getFunctionList()).orElse(Collections.emptySet())
                .stream()
                .map(Function::getProductFunction)
                .map(FunctionDTO::new)
                .collect(Collectors.toList()),
                new BeneficiaryDTO(Optional.ofNullable(product.getBeneficiary())
                        .map(Beneficiary::getName).orElse(null),
                        new BeneficiaryDTO.IdentificationDTO(
                                Optional.ofNullable(product.getBeneficiary()).map(beneficiary ->
                                        beneficiary.getIdentification().type()).orElse(null),
                                Optional.ofNullable(product.getBeneficiary()).map(beneficiary ->
                                        beneficiary.getIdentification().number()).orElse(null))));
    }
}
