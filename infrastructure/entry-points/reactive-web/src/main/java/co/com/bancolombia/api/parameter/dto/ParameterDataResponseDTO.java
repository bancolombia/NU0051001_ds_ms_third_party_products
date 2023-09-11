package co.com.bancolombia.api.parameter.dto;

import java.util.List;
import java.util.Map;

public record ParameterDataResponseDTO(ParametersDTO data) {

    public record ParametersDTO(List<Map<String, String>> productTypes, List<Map<String, String>> documentTypes) {
    }
}