package co.com.bancolombia.api.builders;

import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.IdentificationCreator;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

@UtilityClass
public class RequestBuilder {

    public Consumer<HttpHeaders> getContextHeaders = headers -> {
        headers.add("message-id", ContextCreator.ID_ONE);
        headers.add("mdm-code", ContextCreator.MDM_CODE);
        headers.add("session-tracker", ContextCreator.SESSION_ID);
        headers.add("channel", ContextCreator.CHANNEL);
        headers.add("domain", ContextCreator.DOMAIN);
        headers.add("request-timestamp", ContextCreator.REQUEST_DATE);
        headers.add("user-agent", ContextCreator.AGENT_NAME);
        headers.add("app-version", ContextCreator.APP_VERSION);
        headers.add("device-id", ContextCreator.DEVICE_ID);
        headers.add("platform-type", ContextCreator.DEVICE_TYPE);
        headers.add("ip", ContextCreator.DEVICE_IP);
        headers.add("content-type", ContextCreator.CONTENT_TYPE);
        headers.add("authorization", ContextCreator.AUTHORIZATION);
        headers.add("customer-identification-type", IdentificationCreator.TYPE);
        headers.add("customer-identification-number", IdentificationCreator.NUMBER);
    };


    public Consumer<HttpHeaders> getDownloadHeaders = headers -> {
        headers.add("message-id", ContextCreator.ID_ONE);
        headers.add("mdm-code", ContextCreator.MDM_CODE);
        headers.add("session-tracker", ContextCreator.SESSION_ID);
        headers.add("channel", ContextCreator.CHANNEL);
        headers.add("domain", ContextCreator.DOMAIN);
        headers.add("request-timestamp", ContextCreator.REQUEST_DATE);
        headers.add("user-agent", ContextCreator.AGENT_NAME);
        headers.add("app-version", ContextCreator.APP_VERSION);
        headers.add("device-id", ContextCreator.DEVICE_ID);
        headers.add("platform-type", ContextCreator.DEVICE_TYPE);
        headers.add("ip", ContextCreator.DEVICE_IP);
        headers.add("content-type", ContextCreator.CONTENT_TYPE);
        headers.add("authorization", ContextCreator.AUTHORIZATION);
        headers.add("customer-identification-type", IdentificationCreator.TYPE);
        headers.add("customer-identification-number", IdentificationCreator.NUMBER);
        headers.add("format", ContextCreator.FORMAT_CSV);
    };

}

