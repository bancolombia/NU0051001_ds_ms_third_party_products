package co.com.bancolombia.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Customer represents the users of digital banking with whom there is a contractual relationship.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {

    public Customer(@NonNull Identification identification) {
        super(identification);
    }

    public Customer(@NonNull Identification identification, @NonNull String mdmCode) {
        super(identification, mdmCode);
    }
}
