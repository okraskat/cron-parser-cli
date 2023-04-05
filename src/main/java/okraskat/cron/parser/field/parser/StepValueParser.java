package okraskat.cron.parser.field.parser;

import jakarta.inject.Singleton;
import okraskat.cron.parser.CronField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
class StepValueParser implements CronFieldExpressionParser {
    private static final Logger log = LoggerFactory.getLogger(StepValueParser.class);

    @Override
    public List<Integer> parseFieldExpression(String fieldExpression, CronField field) {
        String[] parts = fieldExpression.split("/");
        int step;
        try {
            step = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            log.error("Failed to parse step {}", parts[1], e);
            return Collections.emptyList();
        }
        String valuesRange = parts[0];
        if ("*".equals(valuesRange)) {
            return generateValuesFromRange(field.getMinimumValue(), field.getMaximumValue(), step);
        } else if (valuesRange.contains("-")) {
            return parseValueRange(step, valuesRange);
        } else {
            log.error("Failed to parse step value field {} for type {}.", fieldExpression, field);
            return Collections.emptyList();
        }
    }

    private static List<Integer> generateValuesFromRange(int start, int end, int step) {
        return IntStream.rangeClosed(start, end)
                .filter(i -> i % step == 0)
                .boxed()
                .collect(Collectors.toList());
    }

    private static List<Integer> parseValueRange(int step, String valuesRange) {
        String[] valueRangeLimits = valuesRange.split("-");
        int valueRangeStart = Integer.parseInt(valueRangeLimits[0]);
        int valueRangeEnd = Integer.parseInt(valueRangeLimits[1]);
        return generateValuesFromRange(valueRangeStart, valueRangeEnd, step);
    }

    @Override
    public boolean supportsExpression(String expression) {
        return expression.contains("/");
    }
}
