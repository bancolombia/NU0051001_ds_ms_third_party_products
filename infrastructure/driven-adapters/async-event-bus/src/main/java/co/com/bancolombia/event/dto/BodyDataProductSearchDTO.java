package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.MetaDTO;
import lombok.NonNull;

import java.io.Serializable;

public record BodyDataProductSearchDTO (@NonNull MetaDTO meta,
                                        @NonNull Request request,
                                        @NonNull Response response)implements Serializable {

    public record Request (@NonNull SearchCriteriaDTO searchCriteria,
                           @NonNull String transactionOperation) implements Serializable {
    }

    public record Response (@NonNull String code,
                            @NonNull String message,
                            @NonNull String transactionState) implements Serializable {
    }
}