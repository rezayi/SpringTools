package morez.spring.tools.scheduling.customizer;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.RootClassFilter;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import java.lang.reflect.Method;

class SchedulerProxyScheduledLockAdvisor extends AbstractPointcutAdvisor {
    private final Pointcut pointcut = new TaskSchedulerPointcut();
    private final Advice advice;
    private static final Logger logger = LoggerFactory.getLogger(SchedulerProxyScheduledLockAdvisor.class);

    SchedulerProxyScheduledLockAdvisor(SchedulerCustomizerInterface schedulerCustomizer) {
        this.advice = new LockingInterceptor(schedulerCustomizer);
    }

    /**
     * Get the Pointcut that drives this advisor.
     */
    @Override
    @NonNull
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    @NonNull
    public Advice getAdvice() {
        return advice;
    }

    private static class LockingInterceptor implements MethodInterceptor {
        private final SchedulerCustomizerInterface schedulerCustomizer;

        private LockingInterceptor(SchedulerCustomizerInterface schedulerCustomizer) {
            this.schedulerCustomizer=schedulerCustomizer;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Object[] arguments = invocation.getArguments();
            if (arguments.length >= 1 && arguments[0] instanceof Runnable) {
                arguments[0] = new CustomizableRunnable((ScheduledMethodRunnable) arguments[0],schedulerCustomizer);
            } else {
                logger.warn("Task scheduler first argument should be Runnable");
            }
            return invocation.proceed();
        }
    }

    private static class TaskSchedulerPointcut implements Pointcut {
        @Override
        @NonNull
        public ClassFilter getClassFilter() {
            return new RootClassFilter(TaskScheduler.class);
        }

        @Override
        @NonNull
        public MethodMatcher getMethodMatcher() {
            return new MethodMatcher() {

                @Override
                public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
                    return method.getName().startsWith("schedule");
                }

                @Override
                public boolean isRuntime() {
                    return true;
                }

                @Override
                public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass, @NonNull Object... args) {
                    return method.getName().startsWith("schedule");
                }
            };
        }
    }
}
