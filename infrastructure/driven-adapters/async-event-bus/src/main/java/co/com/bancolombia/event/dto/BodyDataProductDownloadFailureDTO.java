package co.com.bancolombia.event.dto;

import co.com.bancolombia.event.commons.ErrorDTO;
import co.com.bancolombia.event.commons.MetaDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class BodyDataProductDownloadFailureDTO implements Serializable {
    @NonNull
    private final MetaDTO meta;
    @NonNull
    private final BodyDataProductDownloadFailureDTO.Request request;
    @NonNull
    private final BodyDataProductDownloadFailureDTO.Response response;

    @Getter
    @EqualsAndHashCode
    public static class Request implements Serializable {
        @NonNull SearchCriteriaDTO searchCriteria;
        @NonNull String format;
        @NonNull String transactionOperation;

        public Request(@NonNull SearchCriteriaDTO searchCriteria, @NonNull String format,
                       @NonNull String transactionOperation) {
            this.searchCriteria = searchCriteria;
            this.format = format;
            this.transactionOperation = transactionOperation;
        }
    }

    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class Response implements Serializable {
        @NonNull
        private final ErrorDTO error;
    }
}
