package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.ErrorDTO;
import co.com.bancolombia.event.commons.MetaDTO;
import lombok.NonNull;

import java.io.Serializable;

public record BodyDataProductModifyFailureDTO(@NonNull MetaDTO meta,
                                              @NonNull Request request,
                                              @NonNull Response response) implements Serializable {
    public record Request(@NonNull ProductModifyDTO product,
                          @NonNull String transactionOperation) implements Serializable {
    }

    public record Response(@NonNull ErrorDTO error,
                           @NonNull String transactionState) implements Serializable {
    }
}