package co.com.bancolombia.rest.parameters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DocumentTypesResponseDTO(DocumentTypesDTO data){

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DocumentTypesDTO(List<DocumentTypesValueDTO> documentList){
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DocumentTypesValueDTO(String code, String description){
    }
}