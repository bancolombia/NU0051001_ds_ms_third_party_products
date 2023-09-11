package co.com.bancolombia;

import co.com.bancolombia.config.ProductsReportProperties;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ProductsReportProperties.class)
public class ComponentBuilder {
    public static final String EMPTY_STRING = "";
    private final ProductsReportProperties productsReportProperties;

    public ColumnBuilder[] getColumns(Map<String, String> informationFields) {
        return informationFields.keySet().stream()
                .map(key -> DynamicReports.col.column(informationFields.get(key), key, String.class))
                .collect(Collectors.toList())
                .toArray(new ColumnBuilder[0]);
    }

    public String applyFormat(Object field) {
        return Optional.ofNullable(field)
                .map(fieldToFormat -> {
                    if (fieldToFormat instanceof Number) {
                        return new DecimalFormat(productsReportProperties.getFormat().getNumber())
                                .format(fieldToFormat);
                    } else if (fieldToFormat instanceof LocalDateTime) {
                        return ((LocalDateTime) fieldToFormat)
                                .format(DateTimeFormatter.ofPattern(productsReportProperties.getFormat().getDate()));
                    }
                    return fieldToFormat.toString();
                })
                .orElse(EMPTY_STRING);
    }
}