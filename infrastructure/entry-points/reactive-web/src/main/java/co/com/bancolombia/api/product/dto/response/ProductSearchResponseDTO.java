package co.com.bancolombia.api.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ProductSearchResponseDTO {

    private String id;
    private String inscriptionDate;
    private String customName;
    private String bankingEntity;
    private SearchProductDTO product;
    private SearchBeneficiaryDTO beneficiary;
    private Set<SearchFunctionDTO> functions;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchProductDTO {
        private String type;
        private String number;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchBeneficiaryDTO {
        private String name;
        private SearchIdentificationDTO identification;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchIdentificationDTO {
        private String number;
        private String type;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchFunctionDTO {
        private String code;
    }
}