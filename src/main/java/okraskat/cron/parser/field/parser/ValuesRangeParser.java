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
class ValuesRangeParser implements CronFieldExpressionParser {
    private static final Logger log = LoggerFactory.getLogger(ValuesRangeParser.class);

    @Override
    public List<Integer> parseFieldExpression(String fieldExpression, CronField field) {
        String[] valueRanges = fieldExpression.split("-");
        try {
            int rangeStart = Integer.parseInt(valueRanges[0]);
            int rangeEnd = Integer.parseInt(valueRanges[1]);
            return IntStream.rangeClosed(rangeStart, rangeEnd)
                    .boxed()
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            log.error("Error during parsing field: {} for type: {}", fieldExpression, field, e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean supportsExpression(String expression) {
        return expression.contains("-") && !expression.contains("/");
    }
}
