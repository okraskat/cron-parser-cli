package okraskat.cron.parser;

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.stream.Collectors;

@Command(name = "cron-parser",
        description = "Parse cron expression passed with a command as an input parameter.",
        version = "0.0.1",
        mixinStandardHelpOptions = true)
public class CronParserCommand implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CronParserCommand.class);

    @Parameters(index = "0", description = "Cron expression with command to parse.")
    private String cronExpressionWithCommand;

    @Inject
    private CronParser cronParser;

    public static void main(String[] args) {
        PicocliRunner.run(CronParserCommand.class, args);
    }

    public void run() {
        cronParser.parse(cronExpressionWithCommand).ifPresentOrElse(this::printCronParsingResult,
                () -> log.error("Failed to parse {}", cronExpressionWithCommand));
    }

    private void printCronParsingResult(CronParsingResult parsingResult) {
        log.info("""
                                
                minute       %s
                hour         %s
                day of month %s
                month        %s
                day of week  %s
                command      %s
                """.formatted(formatList(parsingResult.minutes()), formatList(parsingResult.hours()),
                formatList(parsingResult.daysOfMonth()), formatList(parsingResult.months()),
                formatList(parsingResult.daysOfWeek()), parsingResult.command()));
    }

    private String formatList(List<Integer> integers) {
        return integers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }
}
