package co.com.bancolombia.rest.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({RestConsumerProperties.AccountManagementProperties.class,
        RestConsumerProperties.CustomerProperties.class,
        RestConsumerProperties.AchProperties.class})
public class RestConsumerProperties {

    private final AccountManagementProperties accountManagementProperties;

    @RequiredArgsConstructor
    @ConfigurationProperties(prefix = "adapter.rest-consumer.deposits")
    public static class AccountManagementProperties {

        @NotBlank
        private final String baseUrl;
        @NotBlank
        private final String validationAccount;
        @NotBlank
        @Getter
        private final Integer timeout;
        @NotNull
        @Getter
        private final Map<String, String> homologationCodes;

        public String getValidationAccountUrl() {
            return baseUrl.concat(validationAccount);
        }
    }

    @RequiredArgsConstructor
    @ConfigurationProperties(prefix = "adapter.rest-consumer.mdm")
    public static class CustomerProperties {

        @NotBlank
        private final String baseUrl;
        @NotBlank
        private final String basicInformation;
        @NotBlank
        @Getter
        private final Integer timeout;

        public String getBasicInformationUrl() {
            return baseUrl.concat(basicInformation);
        }
    }

    @RequiredArgsConstructor
    @ConfigurationProperties(prefix = "adapter.rest-consumer.payments")
    public static class PaymentsProperties {

        @NotBlank
        private final String baseUrl;
        @NotBlank
        private final String registerBeneficiaryOthersBanks;
        @NotBlank
        private final String payrollAndSupplierPayment;
        @NotBlank
        private final String enrollNequiAccount;
        @NotBlank
        @Getter
        private final Integer timeout;

        public String getRegisterBeneficiaryOthersBanksUrl() {
            return baseUrl.concat(registerBeneficiaryOthersBanks);
        }

        public String getPayrollAndSupplierPaymentUrl() {
            return baseUrl.concat(payrollAndSupplierPayment);
        }

        public String getEnrollNequiAccountUrl() {
            return baseUrl.concat(enrollNequiAccount);
        }
    }

    @RequiredArgsConstructor
    @ConfigurationProperties(prefix = "adapter.rest-consumer.ach")
    public static class AchProperties {

        @NotBlank
        @Getter
        public final String baseUrl;
        @NotBlank
        @Getter
        public final String listBankEntities;
        @NotBlank
        @Getter
        private final Integer timeout;

        public String getListBanksUrl() {
            return baseUrl.concat(listBankEntities);
        }
    }
}
