# Spring Tools
This project consists of some libraries for help in Spring projects

### Scheduler Customizer
This library has been implemented to customize scheduled methods using Spring `@Scheduled` annotation.<br>
Using this library, you can declare a bean inherited from `SchedulerCustomizerInterface` class to customize scheduled methods.
You can run some codes `Before` and `After` some methods. Also, you can check a condition for each method and prevent running some of them by implementing `checkEnableCondition` method.