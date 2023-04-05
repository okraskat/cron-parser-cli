package okraskat.cron.parser;

import java.util.List;

record CronParsingResult(List<Integer> minutes, List<Integer> hours, List<Integer> daysOfMonth,
                         List<Integer> months, List<Integer> daysOfWeek, String command) {
}
