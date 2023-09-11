package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.MetaDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class BodyDataProductDeleteDTO implements Serializable {

    @NonNull
    private final MetaDTO meta;
    @NonNull private final Request request;
    @NonNull private final Response response;

    @Getter
    @RequiredArgsConstructor
    public static class Request implements Serializable {
        @NonNull private final ProductDeleteDTO product;
        @NonNull private final String transactionOperation;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response implements Serializable {
        @NonNull private final String code;
        @NonNull private final String message;
        @NonNull private final String transactionState;
    }
}