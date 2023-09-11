package co.com.bancolombia.config;

import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;


@RequiredArgsConstructor
@ConfigurationProperties("report")
@Getter
public class ProductsReportProperties {
    private final Fields fields;
    private final Format format;
    private final Filename filename;
    private final String cloudName;

    @RequiredArgsConstructor
    public static class Fields {
        private final String productsEnrolledInformation;
        private final ObjectMapper objectMapper = new ObjectMapper();

        public Map<String, String> getProductsEnrolledInformation() {
            return getReportInformationMap(this.productsEnrolledInformation);
        }

        private Map<String, String> getReportInformationMap(String reportInformationJson) {
            try {
                return objectMapper.readValue(reportInformationJson, Map.class);
            } catch (JsonProcessingException processingException) {
                throw new TechnicalException(processingException, TechnicalErrorMessage.INVALID_REPORT_FIELD_FORMAT);
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Format {
        private final String date;
        private final String number;
        private final String pageIndex;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Filename {
        public final String productsEnrolled;
    }

}
