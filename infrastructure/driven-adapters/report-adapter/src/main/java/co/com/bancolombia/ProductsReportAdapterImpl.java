package co.com.bancolombia;

import co.com.bancolombia.config.ProductsReportProperties;
import co.com.bancolombia.config.format.ProductsReportFormatConfigFactory;
import co.com.bancolombia.d2b.model.storage.CloudFile;
import co.com.bancolombia.d2b.model.storage.CloudFileGateway;
import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.report.AvailableFormat;
import co.com.bancolombia.model.product.report.ProductsReportAdapter;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.sf.dynamicreports.report.builder.DynamicReports.report;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ProductsReportProperties.class)
public class ProductsReportAdapterImpl implements ProductsReportAdapter {


    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String NUMBER = "number";
    public static final String TYPE = "type";
    public static final String ENTITY = "entity";
    public static final String INSCRIPTION_DATE = "inscriptionDate";
    public static final String FUNCTION_LIST = "functionList";
    public static final String CUSTOMER_ID = "customerId";
    public static final String BENEFICIARY = "beneficiary";
    public static final String ADDITIONAL_DATA = "additionalData";
    public static final String SEPARATOR = " - ";


    private final ProductsReportFormatConfigFactory productsReportFormatConfigFactory;
    private final ProductsReportProperties productsReportProperties;
    private final ComponentBuilder componentBuilder;
    private final CloudFileGateway<DataBuffer> cloudFileGateway;
    private final IdentificationHomologator identificationHomologator;

    private static final Integer FIRST_ELEMENT = 0;

    @Override
    public Mono<byte[]> generateReport(List<Product> products, AvailableFormat format) {
        Map<String, String> informationFields = productsReportProperties.getFields().getProductsEnrolledInformation();

        return generateReport(products, informationFields, format);
    }

    @Override
    public Mono<String> uploadReport(byte[] bytes, Context context, AvailableFormat format) {
        return ReportFilename.buildReportFilename(context, format,
                        productsReportProperties.getFilename().getProductsEnrolled(), identificationHomologator)
                .flatMap(filename -> uploadReport(bytes, filename))
                .onErrorResume(error -> Mono.error(new TechnicalException(error, TechnicalErrorMessage.SAVE_REPORT)));
    }

    private Mono<String> uploadReport(byte[] bytes, String filename) {
        DataBuffer buffer = new DefaultDataBufferFactory().wrap(bytes);
        return cloudFileGateway.put(CloudFile
                        .dataFrom(Flux.just(buffer))
                        .bucketName(productsReportProperties.getCloudName())
                        .key(filename)
                        .size(bytes.length)
                        .build())
                .thenReturn(filename);
    }

    private Mono<byte[]> generateReport(List<Product> products, Map<String, String> informationFields,
                                        AvailableFormat format) {
        return Mono.just(report())
                .flatMap(report -> {
                    var config = productsReportFormatConfigFactory
                            .createReportFormatConfig(format);
                    report.columns(componentBuilder.getColumns(informationFields))
                            .setDataSource(createDataSource(informationFields, products));
                    return config.convertToFormat(report);
                });
    }

    private DRDataSource createDataSource(Map<String, String> informationFields, List<Product> products) {

        List<Map<String, Object>> productsList = products.stream().map(product -> {
            Map<String, Object> productRow = new HashMap<>();
            productRow.put(ID, product.getId());
            productRow.put(NAME, product.getName());
            productRow.put(STATE, product.getState());
            productRow.put(NUMBER, product.getNumber());
            productRow.put(TYPE, product.getType());
            productRow.put(ENTITY, product.getEntity());
            productRow.put(INSCRIPTION_DATE, product.getInscriptionDate());
            productRow.put(FUNCTION_LIST, product.getFunctionList());
            productRow.put(CUSTOMER_ID, product.getCustomer().getIdentification().type()
                    + SEPARATOR + product.getCustomer().getIdentification().number());
            productRow.put(BENEFICIARY, product.getBeneficiary().getIdentification().type()
                    + SEPARATOR + product.getBeneficiary().getIdentification().number());
            productRow.put(ADDITIONAL_DATA, product.getAdditionalData());
            return productRow;
        }).collect(Collectors.toList());

        Set<String> titleField = informationFields.keySet();
        var dataSource = new DRDataSource(titleField.toArray(new String[FIRST_ELEMENT]));

        productsList.forEach(productMap -> dataSource
                .add(titleField.stream()
                        .map(key -> componentBuilder.applyFormat(productMap.get(key)))
                        .toArray())
        );
        return dataSource;
    }
}
