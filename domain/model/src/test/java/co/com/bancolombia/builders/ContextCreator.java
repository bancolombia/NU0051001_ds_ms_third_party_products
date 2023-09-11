package co.com.bancolombia.builders;

import co.com.bancolombia.model.commons.Context;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContextCreator {

    public static final String ID_ONE = "id1";
    public static final String SESSION_ID = "sessionId";
    public static final String REQUEST_DATE = "requestDate";
    public static final String CHANNEL = "channel";
    public static final String AGENT_NAME = "agentName";
    public static final String APP_VERSION = "appVersion";
    public static final String DEVICE_ID = "deviceId";
    public static final String DEVICE_IP = "deviceIp";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String PLATFORM_TYPE = "mobile";
    public static final String CONTENT_TYPE = "application/json";
    public static final String AUTHORIZATION = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final String DOMAIN = "/myPath";
    public static final String MDM_CODE = "mdmcode";
    public static final String FORMAT_CSV = "CSV";

    public Context buildNewContext() {
        return Context.builder().id(ID_ONE).sessionId(SESSION_ID)
                .requestDate(REQUEST_DATE).channel(CHANNEL).domain(DOMAIN)
                .contentType(CONTENT_TYPE).authorization(AUTHORIZATION)
                .agent(Context.Agent.builder().name(AGENT_NAME).appVersion(APP_VERSION).build())
                .device(Context.Device.builder().id(DEVICE_ID).ip(DEVICE_IP).type(PLATFORM_TYPE).build())
                .customer(CustomerCreator.buildCustomer()).build();
    }
}
