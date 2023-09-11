package co.com.bancolombia.api.builders;

import co.com.bancolombia.api.product.dto.BeneficiaryDTO;
import co.com.bancolombia.api.product.dto.BeneficiarySearchDTO;
import co.com.bancolombia.api.product.dto.DateRangeDTO;
import co.com.bancolombia.api.product.dto.IdentificationDTO;
import co.com.bancolombia.api.product.dto.IdentificationSearchDTO;
import co.com.bancolombia.api.product.dto.ProductDTO;
import co.com.bancolombia.api.product.dto.ProductSearchDTO;
import co.com.bancolombia.api.product.dto.request.FunctionDTO;
import co.com.bancolombia.api.product.dto.request.ProductEnrollmentRequestDTO;
import co.com.bancolombia.api.product.dto.request.ProductModifyRequestDTO;
import co.com.bancolombia.api.product.dto.request.ProductSearchRequestDTO;
import co.com.bancolombia.api.product.dto.response.ProductEnrollmentResponseDTO;
import co.com.bancolombia.api.product.dto.response.ProductModifyResponseDTO;
import co.com.bancolombia.model.commons.Function;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductTestBuilder {

    public final String CUSTOM_NAME = "TEST";
    private final String BANKING_ENTITY_CODE = "201";
    private final String PRODUCT_TYPE = "ELECTRONIC";
    private final String PRODUCT_NUMBER = "123";
    private final String BENEFICIARY_NAME = "BENEFICIARY";
    private final String IDENTIFICATION_TYPE = "123";
    private final String IDENTIFICATION_NUMBER = "123";
    public final String CODE = "payrollPayment";
    public final String INITIAL_DATE = "2020-05-15";
    public final String FINAL_DATE = "2022-08-20";

    private Set<FunctionDTO> FUNCTIONDTOS = new HashSet<>();
    private FunctionDTO FUNCTIONDTO = new FunctionDTO(CODE);

    public ProductEnrollmentRequestDTO buildProductEnrollmentRequestDTO() {
        return new ProductEnrollmentRequestDTO(CUSTOM_NAME, BANKING_ENTITY_CODE,
                new ProductDTO(PRODUCT_TYPE, PRODUCT_NUMBER), new BeneficiaryDTO(BENEFICIARY_NAME,
                new IdentificationDTO(IDENTIFICATION_TYPE, IDENTIFICATION_NUMBER)), FUNCTIONDTOS);
    }

    public ProductEnrollmentResponseDTO buildProductEnrollmentResponseDTO(String state) {
        return new ProductEnrollmentResponseDTO(new ProductEnrollmentResponseDTO.ProductEnrollmentDataDTO(state));
    }

    public ProductModifyRequestDTO buildProductModifyRequestDTO() {
        FUNCTIONDTOS.add(FUNCTIONDTO);
        return new ProductModifyRequestDTO(CUSTOM_NAME, FUNCTIONDTOS);
    }

    public ProductModifyResponseDTO buildProductModifyResponseDTO(String customName, Set<Function> functions) {
        return new ProductModifyResponseDTO(new ProductModifyResponseDTO.ProductModifyDataDTO(customName,
                functions.stream()
                        .map(Function::getProductFunction)
                        .map(FunctionDTO::new)
                        .collect(Collectors.toSet())));
    }

    public ProductSearchRequestDTO buildProductSearchRequestDTO() {
        return new ProductSearchRequestDTO(CUSTOM_NAME, BANKING_ENTITY_CODE,
                new ProductSearchDTO(PRODUCT_TYPE, PRODUCT_NUMBER), new BeneficiarySearchDTO(BENEFICIARY_NAME,
                new IdentificationSearchDTO(IDENTIFICATION_TYPE, IDENTIFICATION_NUMBER)), Set.of(FUNCTIONDTO),
                new DateRangeDTO(INITIAL_DATE, FINAL_DATE));
    }

}
