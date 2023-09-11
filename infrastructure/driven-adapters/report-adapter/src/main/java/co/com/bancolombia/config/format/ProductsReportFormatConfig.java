package co.com.bancolombia.config.format;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import reactor.core.publisher.Mono;

public interface ProductsReportFormatConfig {

    Mono<byte[]> convertToFormat(JasperReportBuilder jasperReportBuilder);
}
