package morez.spring.tools.scheduling.customizer;

import morez.spring.tools.scheduling.customizer.annotation.EnableSchedulerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
class SchedulerProxyLockConfiguration {
    @Bean
    @Conditional(OnSchedulerCustomizerEnabledCondition.class)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    SchedulerProxyScheduledLockAdvisor proxyScheduledLockAopBeanPostProcessor(SchedulerCustomizerInterface schedulerCustomizer) {
        return new SchedulerProxyScheduledLockAdvisor(schedulerCustomizer);
    }


    static class OnSchedulerCustomizerEnabledCondition implements Condition {
        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
            return isSchedulerCustomizerEnabled(context);
        }

        private Boolean isSchedulerCustomizerEnabled(@NonNull ConditionContext context) {
            var beanFactory = context.getBeanFactory();
            if (beanFactory == null)
                return false;

            var applicationBeanName = beanFactory.getBeanNamesForAnnotation(SpringBootApplication.class)[0];
            boolean isMarkedAsEnabled = beanFactory
                    .findAnnotationOnBean(applicationBeanName, EnableSchedulerCustomizer.class) != null;
            if (!isMarkedAsEnabled) {
                logger.info("SpringBootApplication class does not marked SchedulerCustomizer as enabled. " +
                        "To enable SchedulerCustomizer add @EnableSchedulerCustomizer to SpringBootApplication class");
                return false;
            }

            var customizerBeans=beanFactory.getBeansOfType(SchedulerCustomizerInterface.class);
            if (customizerBeans.isEmpty()) {
                logger.info("SchedulerCustomizer bean is not declared. " +
                        "To enable SchedulerCustomizer declare a bean inherited from SchedulerCustomizerInterface interface");
                return false;
            }
            if(customizerBeans.size()>1){
                logger.info("more than one SchedulerCustomizer beans are declared." +
                        "To enable SchedulerCustomizer declare Just one bean inherited from SchedulerCustomizerInterface interface");
                return false;
            }
            return true;
        }
    }
}
