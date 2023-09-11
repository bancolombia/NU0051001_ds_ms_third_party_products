package co.com.bancolombia.rest.mapper;

import co.com.bancolombia.rest.parameters.ParametersResponseDTO;
import co.com.bancolombia.rest.parameters.DocumentTypesResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class ParametersMapper {

    public Map<String, String> toParametersMap(ParametersResponseDTO parametersResponseDTO) {
        return parametersResponseDTO.data().paramValue().stream()
                .collect(Collectors.toMap(
                        ParametersResponseDTO.ParameterValueDTO::type,
                        ParametersResponseDTO.ParameterValueDTO::description));
    }

    public Map<String, String> toParametersMap(DocumentTypesResponseDTO documentTypesResponseDTO) {
        return documentTypesResponseDTO.data().documentList().stream()
                .collect(Collectors.toMap(
                        DocumentTypesResponseDTO.DocumentTypesValueDTO::code,
                        DocumentTypesResponseDTO.DocumentTypesValueDTO::description));
    }
}