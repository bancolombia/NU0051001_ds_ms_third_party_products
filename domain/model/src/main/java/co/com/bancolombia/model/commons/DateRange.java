package co.com.bancolombia.model.commons;

import java.time.LocalDateTime;

/**
 * DateRangeHelper to filter by dates
 */
public record DateRange(LocalDateTime initialDate, LocalDateTime endDate) {

}
