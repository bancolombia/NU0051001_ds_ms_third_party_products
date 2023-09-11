package co.com.bancolombia.api.product.mapper;

import co.com.bancolombia.model.commons.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class DataModify {
    private String customName;
    private Set<Function> functions;
}
