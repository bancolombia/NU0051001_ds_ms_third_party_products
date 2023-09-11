package co.com.bancolombia;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.report.AvailableFormat;
import lombok.Builder;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder(toBuilder = true)
public class ReportFilename {
    private static final String DOT = ".";
    private static final String UNDERSCORE = "_";
    private static final String MIDDLE_DASH = "-";
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy-hh-mm-ss";
    @NonNull
    private final Context context;
    @NonNull
    private final String filename;
    @NonNull
    private final AvailableFormat format;

    private Mono<String> getFilenameSecureBox(IdentificationHomologator identificationHomologator) {
        return identificationHomologator.getHomologatedIdentificationType(context.getCustomer().getIdentification())
                .map(identificationType -> new StringBuilder()
                        .append(context.getChannel())
                        .append(UNDERSCORE)
                        .append(identificationType)
                        .append(UNDERSCORE)
                        .append(context.getCustomer().getIdentification().number())
                        .append(UNDERSCORE)
                        .append(filename)
                        .append(MIDDLE_DASH)
                        .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .append(DOT)
                        .append(format.getFormat())
                        .toString());
    }

    public static Mono<String> buildReportFilename(Context context, AvailableFormat format, String filename,
                                                   IdentificationHomologator identificationHomologator) {
        return ReportFilename.builder()
                .context(context)
                .filename(filename)
                .format(format)
                .build()
                .getFilenameSecureBox(identificationHomologator);
    }
}
