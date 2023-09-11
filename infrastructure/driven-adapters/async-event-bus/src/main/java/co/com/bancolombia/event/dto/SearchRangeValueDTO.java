package co.com.bancolombia.event.dto;

import lombok.Value;

import java.util.Optional;

/**
 * SearchRangeValue represents the value object that will be compared by the criterion
 * when querying by ranges is required.
 */
@Value
public class SearchRangeValueDTO<T> {

    T fromValue;
    T toValue;

    @Override
    public String toString() {
        return "{fromValue="
                .concat(Optional.ofNullable(fromValue).map(Object::toString).orElse("null"))
                .concat(", toValue=")
                .concat(Optional.ofNullable(toValue).map(Object::toString).orElse("null"))
                .concat("}");
    }
}
