package okraskat.cron.parser;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@MicronautTest
class CronParserTest {

    @Inject
    private CronParser cronParser;

    @Test
    void shouldParseCronExpressionFromTaskDescription() {
        // given
        String cronExpressionWithCommand = "*/15 0 1,15 * 1-5 /usr/bin/find";
        // when
        Optional<CronParsingResult> result = cronParser.parse(cronExpressionWithCommand);
        // then
        assertThat(result).isPresent()
                .get()
                .satisfies(r -> {
                    assertThat(r.minutes()).containsExactly(0, 15, 30, 45);
                    assertThat(r.hours()).containsExactly(0);
                    assertThat(r.daysOfMonth()).containsExactly(1, 15);
                    assertThat(r.months()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
                    assertThat(r.daysOfWeek()).containsExactly(1, 2, 3, 4, 5);
                    assertThat(r.command()).isEqualTo("/usr/bin/find");
                });
    }

    @Test
    void shouldNotParseCronExpressionFieldsWithUnsupportedSyntax() {
        // given
        String cronExpressionWithCommand = "A/15 & 1,B */B MON-FRI /usr/bin/find";
        // when
        Optional<CronParsingResult> result = cronParser.parse(cronExpressionWithCommand);
        // then
        assertThat(result).isPresent()
                .get()
                .satisfies(r -> {
                    assertThat(r.minutes()).isEqualTo(Collections.emptyList());
                    assertThat(r.hours()).isEqualTo(Collections.emptyList());
                    assertThat(r.daysOfMonth()).isEqualTo(Collections.emptyList());
                    assertThat(r.months()).isEqualTo(Collections.emptyList());
                    assertThat(r.daysOfWeek()).isEqualTo(Collections.emptyList());
                    assertThat(r.command()).isEqualTo("/usr/bin/find");
                });
    }

    @Test
    void shouldParseCronExpressionWithPeriodInStep() {
        // given
        String cronExpressionWithCommand = "0-20/5 * 1,15 * 1-5 /usr/bin/find test.txt";
        // when
        Optional<CronParsingResult> result = cronParser.parse(cronExpressionWithCommand);
        // then
        assertThat(result).isPresent()
                .get()
                .satisfies(cpr -> {
                    assertThat(cpr.minutes()).containsExactly(0, 5, 10, 15, 20);
                    assertThat(cpr.hours()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
                    assertThat(cpr.daysOfMonth()).containsExactly(1, 15);
                    assertThat(cpr.months()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
                    assertThat(cpr.daysOfWeek()).containsExactly(1, 2, 3, 4, 5);
                    assertThat(cpr.command()).isEqualTo("/usr/bin/find test.txt");
                });
    }
}