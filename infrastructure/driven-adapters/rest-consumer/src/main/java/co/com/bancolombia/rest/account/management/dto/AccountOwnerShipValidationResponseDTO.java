package co.com.bancolombia.rest.account.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOwnerShipValidationResponseDTO {

    private List<AccountOwnerShipValidationResponse> data;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountOwnerShipValidationResponse {

        private ParticipantDTO participant;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParticipantDTO {

        private String relation;
    }
}
