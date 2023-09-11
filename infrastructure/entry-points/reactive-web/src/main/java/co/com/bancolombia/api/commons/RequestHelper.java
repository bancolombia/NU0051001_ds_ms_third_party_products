package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.commons.enums.RequiredPathVariable;
import co.com.bancolombia.api.commons.enums.RequiredQueryParam;
import co.com.bancolombia.api.properties.PageDefaultProperties;
import co.com.bancolombia.model.commons.PageRequest;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.report.AvailableFormat;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;

@UtilityClass
public class RequestHelper {

    public static final String FORMAT = "format";
    private String REGEX_PATH_VARIABLE_NUMERIC = "\\d+";

    public static String getQueryParam(ServerRequest serverRequest, RequiredQueryParam queryParam) {
        return serverRequest.queryParam(queryParam.getValue())
                .filter(paramQuery -> !Strings.isBlank(paramQuery))
                .orElseThrow(() -> new BusinessException(BusinessErrorMessage
                        .valueOf(queryParam.getBusinessErrorMessage().name())));
    }

    public static PageRequest getPageRequest(ServerRequest serverRequest, PageDefaultProperties defaultPage) {
        return new PageRequest(
                Integer.parseInt(serverRequest.queryParam("pageNumber")
                        .orElse(defaultPage.getDefaultPageNumber())),
                Integer.parseInt(serverRequest.queryParam("pageSize")
                        .orElse(defaultPage.getDefaultPageSize())));
    }

    public static String getPathVariableByNumericContent(ServerRequest serverRequest, RequiredPathVariable variable) {
        String pathVariable = serverRequest.pathVariable(variable.getValue());

        if (!pathVariable.isBlank() && pathVariable.matches(REGEX_PATH_VARIABLE_NUMERIC)) {
            return pathVariable;
        } else {
            throw new BusinessException(BusinessErrorMessage.valueOf(variable.getBusinessErrorMessage().name()));
        }
    }

    public static AvailableFormat getAvailableFormatHeader(ServerRequest serverRequest) {

        String headerValue = serverRequest.headers().header(FORMAT).stream()
                .filter(header -> !Strings.isBlank(header) &&
                        Arrays.stream(AvailableFormat.values()).anyMatch(
                                format -> format.getFormat().equals(header.toLowerCase()))).findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorMessage.REQUIRED_FORMAT));

        return AvailableFormat.valueOf(headerValue.toUpperCase());
    }

}
