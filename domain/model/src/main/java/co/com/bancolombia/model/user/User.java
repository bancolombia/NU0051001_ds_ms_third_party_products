package co.com.bancolombia.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Basic class with the user information
 */
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private Integer id;
    private String mdmCode;
    @NonNull
    private final Identification identification;

    protected User(Identification identification) {
        this.identification = identification;
    }
    protected User(Identification identification, String mdmCode) {
        this.identification = identification;
        this.mdmCode = mdmCode;
    }
}
