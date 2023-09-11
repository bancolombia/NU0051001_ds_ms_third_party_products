package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.BeneficiaryDTO;
import co.com.bancolombia.event.commons.FunctionDTO;

import java.io.Serializable;
import java.util.Set;

/**
 * Object needed in the event emission
 * @param customName
 * @param bankCode
 * @param productType
 * @param number
 * @param beneficiary
 * @param functions
 * @param date
 */
public record SearchCriteriaDTO(String customName, String bankCode, String productType, String number,
                                BeneficiaryDTO beneficiary, Set<FunctionDTO> functions,
                                String date) implements Serializable {
}