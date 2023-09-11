package co.com.bancolombia.event.commons;


import java.io.Serializable;

public record ProductRequestDTO(ProductDTO product, String transactionOperation) implements Serializable {

}
