package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.BodyDataNotificationDTO;
import co.com.bancolombia.event.commons.MetaDTO;
import co.com.bancolombia.event.commons.ProductRequestDTO;

import java.io.Serializable;

public record BodyDataProductEnrollmentDTO(MetaDTO meta, ProductRequestDTO request, Response response,
                                           BodyDataNotificationDTO notification) implements Serializable {

    public record Response(String code, String message,
                           String transactionState) implements Serializable {
    }
}