package co.com.bancolombia.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Beneficiary represents the owner of the product.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Beneficiary extends User {

    private String name;

    public Beneficiary(Identification identification, String name) {
        super(identification);
        this.name = name;
    }

    public Beneficiary(String mdmCode, Identification identification, String name) {
        super(identification, mdmCode);
        this.name = name;
    }
}
