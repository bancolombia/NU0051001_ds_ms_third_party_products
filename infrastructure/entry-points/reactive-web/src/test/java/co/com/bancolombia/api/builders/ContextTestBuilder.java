package co.com.bancolombia.api.builders;

import co.com.bancolombia.api.commons.ContextDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContextTestBuilder {

    public final String ID = "idValue";
    public final String SESSION_ID = "sessionIdValue";
    public final String REQUEST_DATE = "requestDateValue";
    public final String CHANNEL = "channelValue";
    public final String DOMAIN = "localhost";
    public final String AGENT_NAME = "agentNameValue";
    public final String AGENT_APP_VERSION = "agentAppVersionValue";
    public final String DEVICE_ID = "deviceIdValue";
    public final String DEVICE_IP = "deviceIpValue";
    public final String DEVICE_TYPE = "deviceTypeValue";
    public final String IDENTIFICATION_TYPE = "identificationTypeValue";
    public final String IDENTIFICATION_NUMBER = "identificationNumberValue";
    public final String CONTENT_TYPE = "contentTypeValue";
    public final String AUTHORIZATION = "authorizationValue";
    public final String MDM_CODE = "mdmcode";

    public static ContextDTO createContextDTO() {
        return ContextDTO.builder()
                .id(ID)
                .sessionId(SESSION_ID)
                .requestDate(REQUEST_DATE)
                .channel(CHANNEL)
                .domain(DOMAIN)
                .agent(ContextDTO.Agent.builder()
                        .name(AGENT_NAME)
                        .appVersion(AGENT_APP_VERSION)
                        .build())
                .device(ContextDTO.Device.builder()
                        .id(DEVICE_ID)
                        .ip(DEVICE_IP)
                        .type(DEVICE_TYPE)
                        .build())
                .customer(ContextDTO.Customer.builder()
                        .identification(ContextDTO.Customer.Identification.builder()
                                .type(IDENTIFICATION_TYPE)
                                .number(IDENTIFICATION_NUMBER)
                                .build())
                        .mdmCode(MDM_CODE)
                        .build())
                .contentType(CONTENT_TYPE)
                .authorization(AUTHORIZATION)
                .build();
    }

}
