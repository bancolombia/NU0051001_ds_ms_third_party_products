package co.com.bancolombia.rest.parameters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParametersResponseDTO(ParametersDTO data){

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ParametersDTO(List<ParameterValueDTO> paramValue){
    }
    public record ParameterValueDTO(String type, String description){
    }
}