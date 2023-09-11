package co.com.bancolombia.api.parameter.mapper;

import co.com.bancolombia.api.parameter.dto.ParameterDataResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ParameterResponseMapper {
    private static final String CODE_KEY = "code";
    private static final String NAME_KEY = "name";

    public static ParameterDataResponseDTO mapToParametersResponse(
            Map<String, String> productTypesMap, Map<String, String> documentTypesMap) {
        return new ParameterDataResponseDTO(
                new ParameterDataResponseDTO.ParametersDTO(mapToList(productTypesMap), mapToList(documentTypesMap)));
    }

    private static List<Map<String, String>> mapToList(Map<String, String> map) {
        List<Map<String, String>> mapList = new ArrayList<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            Map<String, String> formattedMap = new HashMap<>();
            formattedMap.put(CODE_KEY, entry.getKey());
            formattedMap.put(NAME_KEY, entry.getValue());
            mapList.add(formattedMap);
        }
        return mapList;
    }
}