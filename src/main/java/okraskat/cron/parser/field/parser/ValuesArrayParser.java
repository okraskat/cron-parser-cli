package okraskat.cron.parser.field.parser;

import jakarta.inject.Singleton;
import okraskat.cron.parser.CronField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
class ValuesArrayParser implements CronFieldExpressionParser {
    private static final Logger log = LoggerFactory.getLogger(ValuesArrayParser.class);

    @Override
    public List<Integer> parseFieldExpression(String fieldExpression, CronField field) {
        try {
            return Arrays.stream(fieldExpression.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            log.error("Failed to parse field: {}", fieldExpression);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean supportsExpression(String expression) {
        return expression.contains(",") && !expression.contains("/");
    }
}
