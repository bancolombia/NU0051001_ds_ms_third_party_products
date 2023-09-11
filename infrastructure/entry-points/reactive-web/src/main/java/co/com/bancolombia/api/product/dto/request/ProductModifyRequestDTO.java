package co.com.bancolombia.api.product.dto.request;

import java.util.Set;

public record ProductModifyRequestDTO (String customName, Set<FunctionDTO> functions){

}
