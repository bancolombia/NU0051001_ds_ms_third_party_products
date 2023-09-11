package co.com.bancolombia.rest.account.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerBasicInformationResponseDTO {

    private CustomerBasicInformationResponse data;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerBasicInformationResponse {

        private String fullName;
    }
}
