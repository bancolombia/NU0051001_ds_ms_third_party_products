package co.com.bancolombia.event.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties of the event that are located on the configuration properties file
 */
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "adapter")
public class EventNameProperties {

    private final String channel;
    private final String publisher;
    private final String prefix;
    private final String transactionCode;
    private final String transactionCodeDescription;
    private final EventBusinessProduct eventBusinessProduct;


    @Getter
    @RequiredArgsConstructor
    public static class EventBusinessProduct {
        private final String productPrefix;
        private final String reportPrefix;
        private final Suffix suffix;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Suffix {

        private final String enrollProductDone;
        private final String enrollProductRejected;
        private final String enrolledProductsConsultedDone;
        private final String enrolledProductsConsultedRejected;
        private final String productsEnrolledDownloadDone;
        private final String productsEnrolledDownloadRejected;
        private final String removeProductDone;
        private final String removeProductRejected;
        private final String modificationProductDone;
        private final String modificationProductRejected;
    }
}