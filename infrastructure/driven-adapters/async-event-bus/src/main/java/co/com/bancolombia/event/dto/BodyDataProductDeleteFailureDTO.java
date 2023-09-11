package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.ErrorDTO;
import co.com.bancolombia.event.commons.MetaDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class BodyDataProductDeleteFailureDTO implements Serializable {
    @NonNull
    private final MetaDTO meta;
    @NonNull
    private final BodyDataProductDeleteFailureDTO.Request request;
    @NonNull
    private final BodyDataProductDeleteFailureDTO.Response response;

    @Getter
    public static class Request implements Serializable {
        @NonNull ProductDeleteDTO product;
        @NonNull String transactionOperation;

        public Request(@NonNull ProductDeleteDTO product,
                       @NonNull String transactionOperation) {
            this.product = product;
            this.transactionOperation = transactionOperation;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response implements Serializable {
        @NonNull private final ErrorDTO error;
        @NonNull private final String transactionState;
    }
}