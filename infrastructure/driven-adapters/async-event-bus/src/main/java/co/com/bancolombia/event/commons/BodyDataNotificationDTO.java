package co.com.bancolombia.event.commons;

import co.com.bancolombia.event.properties.NotificationProperties;
import lombok.NonNull;

import java.io.Serializable;

public record BodyDataNotificationDTO(@NonNull String alertTransactionId,
                                      @NonNull String alertTransactionCode,
                                      @NonNull String validationId,
                                      @NonNull String validationsNumber,
                                      @NonNull String value,
                                      @NonNull String message) implements Serializable {

    public static BodyDataNotificationDTO buildBodyDataNotificationDTO(NotificationProperties properties,
                                                                       String alertTransactionCode) {
        return new BodyDataNotificationDTO(properties.alertTransactionId(),
                alertTransactionCode, properties.validationId(), properties.validationsNumber(),
                properties.valueChargeForSendingNotifications(), properties.channelNameForSendingNotifications());
    }
}
