package co.com.bancolombia.api.product.dto.response;

import co.com.bancolombia.api.product.dto.request.FunctionDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

public record ProductModifyResponseDTO(ProductModifyDataDTO data) {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public record ProductModifyDataDTO(String customName, Set<FunctionDTO> functions) {

    }


}