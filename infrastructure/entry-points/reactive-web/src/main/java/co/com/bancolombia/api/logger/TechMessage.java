package co.com.bancolombia.api.logger;

import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static co.com.bancolombia.api.helper.ServerWebExchangeHelper.getFirstHeader;
import static co.com.bancolombia.api.helper.ServerWebExchangeHelper.getTransactionId;
import static co.com.bancolombia.api.helper.ServerWebExchangeHelper.getMessageFromExchange;
import static co.com.bancolombia.api.logger.LogConstantsEnum.SERVICE_NAME;

@UtilityClass
public class TechMessage {

    private static final String EMPTY_STRING = "";
    private static final String SPACE_STRING = " ";
    private static final String CHANNEL = "channel";
    private static final String APP_VERSION = "app-version";
    private static final String TRACE = "trace";
    private static final String CAUSE = "cause";

    public static ObjectTechMsg<Map<String, Object>> getTechErrorMessage(@NonNull Throwable error,
                                                                         ServerRequest request) {
        return new ObjectTechMsg<>(
                SERVICE_NAME.getName(),
                getFirstHeaderFromRequest(request, LogConstantsEnum.MESSAGE_ID.getName()),
                request.path(),
                SERVICE_NAME.getName(),
                WebExceptionHandler.class.getSimpleName(),
                getTagList(getFirstHeaderFromRequest(request, CHANNEL),
                        getFirstHeaderFromRequest(request, APP_VERSION)),
                Map.of(TRACE, error.getStackTrace(),
                        CAUSE, Optional.ofNullable(error.getCause()).orElse(new Throwable()))
        );
    }

    public static ObjectTechMsg<Object> getTechInfoMessage(ServerWebExchange webExchange) {
        return new ObjectTechMsg<>(SERVICE_NAME.getName(), getTransactionId(webExchange),
                webExchange.getRequest().getPath().value(),
                SERVICE_NAME.getName(), InfoLogFilter.class.getSimpleName(),
                getTagList(getFirstHeader(webExchange, CHANNEL), getFirstHeader(webExchange, APP_VERSION)),
                getMessageFromExchange(webExchange));
    }

    private static String getFirstHeaderFromRequest(ServerRequest request, String name) {
        return Optional.ofNullable(request)
                .map(ServerRequest::headers)
                .map(headers -> headers.firstHeader(name))
                .orElse(EMPTY_STRING);
    }

    private static List<String> getTagList(String channel, String appVersion) {
        return List.of(channel, formatAppVersion(appVersion));
    }

    private static String formatAppVersion(String appVersion) {
        return String.join(SPACE_STRING, APP_VERSION, appVersion);
    }
}