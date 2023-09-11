package co.com.bancolombia.config.format;

import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

import static net.sf.dynamicreports.report.builder.DynamicReports.export;

@RequiredArgsConstructor
public class ProductsReportTXTConfig implements ProductsReportFormatConfig {

    @Override
    public Mono<byte[]> convertToFormat(JasperReportBuilder report) {
        var outputStream = new ByteArrayOutputStream();
        try {
            report.toText(export.textExporter(outputStream));
        } catch (DRException drException) {
            return Mono.error(new TechnicalException(drException, TechnicalErrorMessage.GENERATE_REPORT));
        }
        return Mono.just(outputStream.toByteArray());
    }
}
