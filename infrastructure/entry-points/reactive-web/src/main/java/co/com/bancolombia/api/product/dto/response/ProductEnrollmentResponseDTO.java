package co.com.bancolombia.api.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductEnrollmentResponseDTO {

    private ProductEnrollmentDataDTO data;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class ProductEnrollmentDataDTO {
        private String status;
    }
}
