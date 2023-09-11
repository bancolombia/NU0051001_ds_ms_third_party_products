package co.com.bancolombia.rest.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BankEntitiesResponseDTO(BankEntitiesDTO data, BankEntitiesPaginationDTO links) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BankEntitiesDTO(List<BankEntityDTO> bank) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BankEntityDTO(String bankCode) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BankEntitiesPaginationDTO(String last) {
    }
}