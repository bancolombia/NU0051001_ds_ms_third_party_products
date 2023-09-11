package co.com.bancolombia.model.product;

import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.user.Beneficiary;
import co.com.bancolombia.model.user.Customer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Product represents the class that contains all the information of a third party product
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Product {

    private String id;
    private String name;
    private String state;
    private String number;
    private String type;
    private String entity;
    private LocalDateTime inscriptionDate;
    private Set<Function> functionList;
    private Customer customer;
    private Beneficiary beneficiary;
    private String additionalData;
}
