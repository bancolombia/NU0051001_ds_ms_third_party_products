package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.ErrorDTO;
import co.com.bancolombia.event.commons.MetaDTO;
import co.com.bancolombia.event.commons.ProductRequestDTO;

import java.io.Serializable;

public record BodyDataProductEnrollmentFailureDTO(MetaDTO meta, ProductRequestDTO request,
                                                  Response response) implements Serializable {

    public record Response(ErrorDTO error, String transactionState) implements Serializable {
    }
}
