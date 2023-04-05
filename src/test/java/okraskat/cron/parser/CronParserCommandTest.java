package okraskat.cron.parser;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CronParserCommandTest {

    @Test
    void shouldPrintLogWithCorrectResult() {
        // given
        LogCaptor logCaptor = LogCaptor.forClass(CronParserCommand.class);
        // when
        executeCommand(CronParserCommand.class, new String[]{"*/15 0 1,15 * 1-5 /usr/bin/find"});
        // then
        assertThat(logCaptor.getInfoLogs())
                .hasSize(1)
                .first()
                .isEqualTo("""
                        
                        minute       0 15 30 45
                        hour         0
                        day of month 1 15
                        month        1 2 3 4 5 6 7 8 9 10 11 12
                        day of week  1 2 3 4 5
                        command      /usr/bin/find
                        """);
    }

    @Test
    void shouldLogErrorWhenCronExpressionIsToShort() {
        // given
        LogCaptor logCaptor = LogCaptor.forClass(CronParserCommand.class);
        // when
        executeCommand(CronParserCommand.class, new String[]{"*/15 0 1,15 * "});
        // then
        assertThat(logCaptor.getErrorLogs())
                .hasSize(1)
                .first()
                .isEqualTo("Failed to parse */15 0 1,15 * ");
    }

    private void executeCommand(Class commandClass, String[] args) {
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            PicocliRunner.run(commandClass, ctx, args);
        }
    }
}
