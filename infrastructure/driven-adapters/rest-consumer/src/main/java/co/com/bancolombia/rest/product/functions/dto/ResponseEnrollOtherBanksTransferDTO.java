package co.com.bancolombia.rest.product.functions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseEnrollOtherBanksTransferDTO(ResponseEnrollOtherBanksTransfer data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResponseEnrollOtherBanksTransfer(@JsonProperty(value = "Payer") List<PayerDTO> payer) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PayerDTO(BeneficiaryDTO beneficiary) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BeneficiaryDTO(String answerCode, String descriptionAnswerCode) {
    }

}
