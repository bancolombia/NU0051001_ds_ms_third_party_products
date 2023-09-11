package co.com.bancolombia.event.builder;

import co.com.bancolombia.event.commons.MetaDTO;
import co.com.bancolombia.model.commons.Context;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MetaDTOBuilder {

    private final String NON_MONETARY_TRANSACTION = "No Monetaria";
    private final String SUCCESSFUL_TRANSACTION_STATE = "Exitosa";
    private final String FAILED_TRANSACTION_STATE = "Fallido";


    public MetaDTO buildSuccessMeta(Context context, String transactionCode, String transactionCodeDescription) {
        return new MetaDTO(context, transactionCode, transactionCodeDescription,
                NON_MONETARY_TRANSACTION, SUCCESSFUL_TRANSACTION_STATE);
    }

    public MetaDTO buildErrorMeta(Context context, String transactionCode, String transactionCodeDescription) {
        return new MetaDTO(context, transactionCode, transactionCodeDescription, NON_MONETARY_TRANSACTION,
                FAILED_TRANSACTION_STATE);
    }
}
