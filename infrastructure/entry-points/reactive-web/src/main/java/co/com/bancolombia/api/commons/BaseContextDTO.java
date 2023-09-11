package co.com.bancolombia.api.commons;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BaseContextDTO {

    @NotBlank(message = "%MESSAGE_ID_IS_REQUIRED%")
    private final String id;
    @NotBlank(message = "%SESSION_TRACKER_IS_REQUIRED%")
    private final String sessionId;
    @NotBlank(message = "%REQUEST_TIMESTAMP_IS_REQUIRED%")
    private final String requestDate;
    @NotBlank(message = "%CHANNEL_IS_REQUIRED%")
    private final String channel;
    @NotNull
    private final String domain;
    @Valid
    @NotNull
    private final Agent agent;
    @Valid
    @NotNull
    private final Device device;
    @NotBlank(message = "%CONTENT_TYPE_IS_REQUIRED%")
    private final String contentType;
    @NotBlank(message = "%AUTHORIZATION_IS_REQUIRED%")
    private final String authorization;

    @Getter
    @Builder
    public static class Agent {

        @NotBlank(message = "%USER_AGENT_IS_REQUIRED%")
        private final String name;
        @NotBlank(message = "%APP_VERSION_IS_REQUIRED%")
        private final String appVersion;
    }

    @Getter
    @Builder
    public static class Device {

        @NotBlank(message = "%DEVICE_ID_IS_REQUIRED%")
        private final String id;
        @NotBlank(message = "%IP_IS_REQUIRED%")
        private final String ip;
        @NotBlank(message = "%PLATFORM_TYPE_IS_REQUIRED%")
        private final String type;
    }
}