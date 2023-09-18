package morez.spring.tools.test

import morez.spring.tools.scheduling.customizer.SchedulerCustomizerInterface
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class SchedulerCustomizer : SchedulerCustomizerInterface {
    override fun executeBefore(target: Any, method: Method) {
        println("executeBefore -------------- ")
    }

    override fun checkEnableCondition(target: Any, method: Method): Boolean {
        println("checkEnableCondition -------------- ")
        return true
    }

    override fun executeAfter(target: Any, method: Method) {
        println("executeAfter -------------- \n\n")
    }
}