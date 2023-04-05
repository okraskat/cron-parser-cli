package okraskat.cron.parser.field.parser;

import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;
import okraskat.cron.parser.CronField;

import java.util.List;

@Singleton
class ExactValueParser implements CronFieldExpressionParser {
    @Override
    public List<Integer> parseFieldExpression(String fieldExpression, CronField field) {
        return List.of(Integer.parseInt(fieldExpression));
    }

    @Override
    public boolean supportsExpression(String expression) {
        return StringUtils.isDigits(expression);
    }
}
