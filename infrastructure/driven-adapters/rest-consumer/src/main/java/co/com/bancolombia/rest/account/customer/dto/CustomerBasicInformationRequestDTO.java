package co.com.bancolombia.rest.account.customer.dto;

public record CustomerBasicInformationRequestDTO(CustomerBasicInformationRequest data) {

    public record CustomerBasicInformationRequest(String customerDocumentType, String customerDocumentId) {
    }
}
