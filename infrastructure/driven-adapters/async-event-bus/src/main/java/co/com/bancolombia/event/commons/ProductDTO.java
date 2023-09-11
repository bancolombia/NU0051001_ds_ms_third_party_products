package co.com.bancolombia.event.commons;

import java.io.Serializable;
import java.util.List;

public record ProductDTO(String id, String customName, String bankCode, ProductTypeDTO type,
                         String number, BeneficiaryDTO beneficiary,
                         List<FunctionDTO> functions) implements Serializable {
}
