package co.com.bancolombia.config.format;

import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.product.report.AvailableFormat;
import org.springframework.stereotype.Component;

@Component
public class ProductsReportFormatConfigFactory {

    public ProductsReportFormatConfig createReportFormatConfig(AvailableFormat availableFormat) {
        ProductsReportFormatConfig productsReportFormatConfig;
        switch (availableFormat) {
            case CSV:
                productsReportFormatConfig = new ProductsReportCSVConfig();
                break;
            case TXT:
                productsReportFormatConfig = new ProductsReportTXTConfig();
                break;
            default:
                throw new TechnicalException(new Throwable(), TechnicalErrorMessage.INVALID_REPORT_FORMAT);
        }
        return productsReportFormatConfig;
    }
}
