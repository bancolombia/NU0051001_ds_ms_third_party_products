package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.BeneficiaryDTO;
import co.com.bancolombia.event.commons.FunctionDTO;
import co.com.bancolombia.event.commons.ProductTypeDTO;

import java.io.Serializable;
import java.util.List;

public record ProductModifyDTO(String id, String customName, String bankCode, ProductTypeDTO type,
                               String number,
                               List<FunctionDTO> functions, BeneficiaryDTO beneficiary) implements Serializable {
}
