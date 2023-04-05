package okraskat.cron.parser.field.parser;

import okraskat.cron.parser.CronField;

import java.util.List;

public interface CronFieldExpressionParser {
    List<Integer> parseFieldExpression(String fieldExpression, CronField field);

    boolean supportsExpression(String expression);
}
