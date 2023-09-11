package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.BeneficiaryDTO;
import co.com.bancolombia.event.commons.ProductTypeDTO;

import java.io.Serializable;

public record ProductDeleteDTO(String id, String customName, String bankCode, ProductTypeDTO type,
                               String number,
                               BeneficiaryDTO beneficiary) implements Serializable {
}
