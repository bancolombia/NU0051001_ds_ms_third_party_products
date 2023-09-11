package co.com.bancolombia.builders;

import co.com.bancolombia.model.user.Beneficiary;
import co.com.bancolombia.model.user.Identification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BeneficiaryCreator {

    public final String NAME = "TEST";
    public final String TYPE = "TIPDOC_FS000";
    public final String TYPE_NOT_EXISTS = "TIPDOC_NE";
    public final String NUMBER = "12345678";

    public Beneficiary buildBeneficiary() {
        return new Beneficiary(new Identification(TYPE, NUMBER), NAME);
    }

    public Beneficiary buildBeneficiaryWithInvalidDocumentType() {
        return new Beneficiary(new Identification(TYPE_NOT_EXISTS, NUMBER), NAME);
    }
}
