package co.com.bancolombia.rest.product.functions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseEnrollNequiTransferDTO(ResponseEnrollNequiTransfer data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResponseEnrollNequiTransfer(BeneficiaryDTO beneficiary) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BeneficiaryDTO(AccountDTO account) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AccountDTO(String answerCode, String descriptionAnswerCode) {
    }
}