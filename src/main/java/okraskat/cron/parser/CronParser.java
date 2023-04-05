package okraskat.cron.parser;

import jakarta.inject.Singleton;
import okraskat.cron.parser.field.parser.CronFieldExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
class CronParser {
    private static final Logger log = LoggerFactory.getLogger(CronParser.class);

    private final List<CronFieldExpressionParser> cronFieldExpressionParsers;

    CronParser(List<CronFieldExpressionParser> cronFieldExpressionParsers) {
        this.cronFieldExpressionParsers = cronFieldExpressionParsers;
    }

    Optional<CronParsingResult> parse(String cronExpressionWithCommand) {
        String[] parts = cronExpressionWithCommand.split(" ");
        return Optional.of(parts)
                .filter(p -> p.length >= 6)
                .map(this::parseCronFields);
    }

    private CronParsingResult parseCronFields(String[] cronFields) {
        List<Integer> seconds = parseCronField(cronFields[0], CronField.MINUTE);
        List<Integer> hours = parseCronField(cronFields[1], CronField.HOUR);
        List<Integer> daysOfMonth = parseCronField(cronFields[2], CronField.DAY_OF_MONTH);
        List<Integer> months = parseCronField(cronFields[3], CronField.MONTH);
        List<Integer> daysOfWeek = parseCronField(cronFields[4], CronField.DAY_OF_WEEK);
        String command = Arrays.stream(cronFields)
                .skip(5L)
                .collect(Collectors.joining(" "));
        return new CronParsingResult(seconds, hours, daysOfMonth, months, daysOfWeek, command);
    }

    private List<Integer> parseCronField(String cronFieldExpression, CronField cronField) {
        return cronFieldExpressionParsers.stream()
                .filter(parser -> parser.supportsExpression(cronFieldExpression))
                .findFirst()
                .map(parser -> parser.parseFieldExpression(cronFieldExpression, cronField))
                .orElseGet(() -> {
                    log.error("Could not parse cron expression field: {} with type: {}",
                            cronFieldExpression, cronField);
                    return Collections.emptyList();
                });
    }
}
