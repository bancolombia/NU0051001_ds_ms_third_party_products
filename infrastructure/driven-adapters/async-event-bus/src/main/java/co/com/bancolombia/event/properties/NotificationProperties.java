package co.com.bancolombia.event.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.notification")
public record NotificationProperties(String alertTransactionId,
                                     String productEnrollmentTransactionAlertCode,
                                     String productModificationTransactionAlertCode,
                                     String validationId, String validationsNumber,
                                     String valueChargeForSendingNotifications,
                                     String channelNameForSendingNotifications) {
}
