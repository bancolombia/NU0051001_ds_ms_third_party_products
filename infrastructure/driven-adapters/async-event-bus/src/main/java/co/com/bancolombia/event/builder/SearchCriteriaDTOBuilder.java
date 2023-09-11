package co.com.bancolombia.event.builder;

import co.com.bancolombia.event.commons.BeneficiaryDTO;
import co.com.bancolombia.event.commons.FunctionDTO;
import co.com.bancolombia.event.dto.SearchCriteriaDTO;
import co.com.bancolombia.event.dto.SearchRangeValueDTO;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Creates the Object needed in the event emission
 */
@Component
public class SearchCriteriaDTOBuilder {

    public SearchCriteriaDTO buildSearchCriteriaDTO(Product product, DateRange dateRange) {
        return new SearchCriteriaDTO(
                product.getName(),
                product.getEntity(),
                product.getType(),
                product.getNumber(),
                new BeneficiaryDTO(product.getBeneficiary().getName(),
                        new BeneficiaryDTO.IdentificationDTO(
                                product.getBeneficiary().getIdentification().type(),
                                product.getBeneficiary().getIdentification().number())),
                Optional.ofNullable(product.getFunctionList()).orElse(Collections.emptySet())
                        .stream()
                        .map(Function::getProductFunction)
                        .map(FunctionDTO::new)
                        .collect(Collectors.toSet()),
                new SearchRangeValueDTO<>(dateRange.initialDate(), dateRange.endDate()).toString());
    }
}
