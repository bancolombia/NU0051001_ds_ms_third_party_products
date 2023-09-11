package co.com.bancolombia.rest.commons;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ActionsResponseBuilder {
    private static final String ACTION = "action";
    private static final String SERVICE = "service";

    public Map<String, String> actionsResponseBuilder(String action, String service) {
        Map<String, String> plainHeaders = new HashMap<>();
        plainHeaders.put(ACTION, action);
        plainHeaders.put(SERVICE,service);
        return plainHeaders;
    }
}
