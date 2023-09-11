package co.com.bancolombia.rest.mapper;

import co.com.bancolombia.rest.entities.dto.BankEntitiesResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class BankEntitiesMapper {

    public Set<String> toBankEntitiesSet(BankEntitiesResponseDTO bankEntitiesResponseDTO) {
        return bankEntitiesResponseDTO.data().bank().stream()
                .map(BankEntitiesResponseDTO.BankEntityDTO::bankCode)
                .collect(Collectors.toSet());
    }
}