package morez.spring.tools.test

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
open class ScheduledTasks {

    @Scheduled(fixedDelay = 1000)
    fun reportCurrentTime() {
        println(Date())
    }
}
