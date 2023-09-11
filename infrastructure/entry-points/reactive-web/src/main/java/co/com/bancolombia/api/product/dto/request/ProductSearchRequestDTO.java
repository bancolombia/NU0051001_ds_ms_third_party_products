package co.com.bancolombia.api.product.dto.request;

import co.com.bancolombia.api.product.dto.BeneficiarySearchDTO;
import co.com.bancolombia.api.product.dto.DateRangeDTO;
import co.com.bancolombia.api.product.dto.ProductSearchDTO;

import java.util.Set;

public record ProductSearchRequestDTO (String customName, String bankingEntityCode, ProductSearchDTO product,
                                       BeneficiarySearchDTO beneficiary, Set<FunctionDTO> functions,
                                       DateRangeDTO dateRange) {
}