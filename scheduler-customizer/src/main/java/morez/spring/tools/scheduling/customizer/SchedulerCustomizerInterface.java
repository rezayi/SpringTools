package morez.spring.tools.scheduling.customizer;

import java.lang.reflect.Method;

public interface SchedulerCustomizerInterface {
    void executeBefore(Object target, Method method);

    boolean checkEnableCondition(Object target, Method method);

    void executeAfter(Object target, Method method);
}
