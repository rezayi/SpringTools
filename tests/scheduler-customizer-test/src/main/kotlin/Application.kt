package morez.spring.tools.test

import morez.spring.tools.scheduling.customizer.annotation.EnableSchedulerCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableSchedulerCustomizer
@ComponentScan(value = ["morez.spring.tools"])
open class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
