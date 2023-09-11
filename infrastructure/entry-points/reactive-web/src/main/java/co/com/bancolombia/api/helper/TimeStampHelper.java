package co.com.bancolombia.api.helper;

import co.com.bancolombia.api.logger.LogConstantsEnum;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@UtilityClass
public class TimeStampHelper {

    public static String getFormattedTimeStamp(Long currentTimeMillis) {
        var dateFormat = new SimpleDateFormat(LogConstantsEnum.TIME_PATTERN.getName());
        return dateFormat.format(Date.from(Instant.ofEpochMilli(currentTimeMillis)));
    }
}
