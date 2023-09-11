package co.com.bancolombia.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

/**
 * Class that contains the information of the products loaded by the user to massive upload
 */
@Getter
@Builder
public class PlainFile {
    private String id;
    @NonNull
    private Date preparationDate;
    private Date executionDate;
    @NonNull
    private String name;
    private Integer recordsAmount;
    @NonNull
    private String state;
    @NonNull
    private List<Product> productList;
}
