package co.com.bancolombia.builders;

import co.com.bancolombia.model.user.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerCreator {

    private static final String MDM_CODE = "mdmcode";

    public Customer buildCustomer() {
        return new Customer(IdentificationCreator.buildIdentification(), MDM_CODE);
    }
}
