package co.com.bancolombia.event.commons;


import java.io.Serializable;

public record BeneficiaryDTO(String name, IdentificationDTO identification) implements Serializable {

    public record IdentificationDTO(String type, String number) implements Serializable {
    }
}
