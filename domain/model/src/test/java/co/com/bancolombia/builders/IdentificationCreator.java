package co.com.bancolombia.builders;

import co.com.bancolombia.model.user.Identification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IdentificationCreator {

    public final String TYPE = "TIPDOC_FS001";
    public final String NUMBER = "12345678";

    public Identification buildIdentification() {
        return new Identification(TYPE, NUMBER);
    }
}
