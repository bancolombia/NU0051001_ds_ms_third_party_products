package co.com.bancolombia.rest.account.management.dto;

import co.com.bancolombia.rest.commons.dto.CustomerDTO;

import java.util.List;

public record AccountOwnerShipValidationRequestDTO(List<AccountOwnerShipValidationRequest> data) {
    public record AccountOwnerShipValidationRequest(CustomerDTO customer, AccountDTO account) {
    }
}