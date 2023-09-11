package co.com.bancolombia.rest.commons.logger;

import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import co.com.bancolombia.rest.commons.HeadersBuilder;
import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.commons.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class LoggerRest {

    private static final String APP_NAME = "ds_ms_third_party_products";
    private static final TechLogger techLogger = LoggerFactory.getLog(APP_NAME);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void logRequestResponse(Request<?> request, Response<?> response, String action, String service) {
        var loggerMessage = new LoggerMessage(request, response);
        String message;
        try {
            message = objectMapper.writeValueAsString(loggerMessage);
        } catch (JsonProcessingException jsonProcessingException) {
            message = loggerMessage.toString();
        }
        String transactionId = Optional.ofNullable(request.getHeaders().get(HeadersBuilder.MESSAGE_ID)).orElse("");
        String channel = Optional.ofNullable(request.getHeaders().get(HeadersBuilder.CHANNEL)).orElse("");
        List<String> tagList = List.of(channel);
        var log = new ObjectTechMsg<>(APP_NAME, transactionId, action, service,
                WebClient.class.getName(), tagList, message).toString();
        techLogger.debug(log);
    }
}