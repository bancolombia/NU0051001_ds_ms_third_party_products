package co.com.bancolombia.model.product.report;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Communicates the domain with the adapter responsible for generating and uploading reports in different formats.
 */
public interface ProductsReportAdapter {

    Mono<byte[]> generateReport(List<Product> products, AvailableFormat format);

    Mono<String> uploadReport(byte[] bytes, Context context, AvailableFormat format);
}
