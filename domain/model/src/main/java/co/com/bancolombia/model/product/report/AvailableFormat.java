package co.com.bancolombia.model.product.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Available Formats to create and return a file with the information of the action reports .
 */
@Getter
@RequiredArgsConstructor
public enum AvailableFormat {

    CSV("csv"),
    TXT("txt");

    private final String format;
}
