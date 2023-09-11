package co.com.bancolombia.api.commons;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ContextDTO extends BaseContextDTO {

    @Valid
    @NotNull
    private final Customer customer;

    @Getter
    @Builder
    public static class Customer {

        @Valid
        @NotNull
        private final Identification identification;
        @NotBlank(message = "%MDM_CODE_IS_REQUIRED%")
        private final String mdmCode;

        @Getter
        @Builder
        public static class Identification {

            @NotBlank(message = "%CUSTOMER_IDENTIFICATION_TYPE_IS_REQUIRED%")
            private final String type;
            @NotBlank(message = "%CUSTOMER_IDENTIFICATION_NUMBER_IS_REQUIRED%")
            private final String number;
        }
    }
}