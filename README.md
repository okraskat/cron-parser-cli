### Project dependencies
1. Java 17
2. Maven

The easiest way to install that tools is to use SDKMAN (https://sdkman.io/).

### Build project
```
mvn clean package
```

### Run application
Example run command with passed argument.
```
java -jar target/cron-parser-cli-*.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
```

### Tests
There are unit and integration tests (according to Intellij coverage report, they cover 98% of code).

`CronParserTest` is a unit test which verifies if cron expressions are parsed correctly.

`CronParserCommandTest` runs a cli application and verifies if cron expression is correct and output is properly formatted.

### Extending parser
To support new cron field expressions you need to implement new field parser.
It has to implement interface: `CronFieldExpressionParser`.
This interface provides two methods:
1. `supportsExpression` which checks if given expression can be parsed
2. `parseFieldExpression` which parse expression and return list of integers as a result


### ADR
I decided to split parsing of each type of cron field syntax with dedicated class.
It allows to easily add new implementations for new syntax.
I decided to not throw exception, during parsing single field to not break processing of other fields.
I thought that expression parsed in a half is better and gives more feedback than throwing exception.
All exceptions are logged, so errors can be analysed after the program execution.
I picked up Micronaut as an application framework, it provides dependency injection mechanism
and has support for command line applications.