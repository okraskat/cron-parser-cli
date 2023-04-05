package okraskat.cron.parser.field.parser;

import jakarta.inject.Singleton;
import okraskat.cron.parser.CronField;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
class AllValuesParser implements CronFieldExpressionParser {
    @Override
    public List<Integer> parseFieldExpression(String fieldExpression, CronField field) {
        return IntStream.rangeClosed(field.getMinimumValue(), field.getMaximumValue())
                .boxed().collect(Collectors.toList());
    }

    @Override
    public boolean supportsExpression(String expression) {
        return "*".equals(expression);
    }
}
