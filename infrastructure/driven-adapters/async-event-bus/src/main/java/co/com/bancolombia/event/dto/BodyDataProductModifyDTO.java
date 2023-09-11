package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.BodyDataNotificationDTO;
import co.com.bancolombia.event.commons.MetaDTO;
import lombok.NonNull;

import java.io.Serializable;

public record BodyDataProductModifyDTO(@NonNull MetaDTO meta,
                                       @NonNull Request request, @NonNull Response response,
                                       @NonNull BodyDataNotificationDTO notification) implements Serializable {

    public record Request(@NonNull ProductModifyDTO product,
                          @NonNull String transactionOperation) implements Serializable {
    }

    public record Response(@NonNull String code, @NonNull String message,
                           @NonNull String transactionState) implements Serializable {
    }

}