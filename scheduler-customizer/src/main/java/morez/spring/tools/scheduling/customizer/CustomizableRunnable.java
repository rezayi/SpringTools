package morez.spring.tools.scheduling.customizer;

import org.springframework.scheduling.support.ScheduledMethodRunnable;

import static java.util.Objects.requireNonNull;

public class CustomizableRunnable implements Runnable {
    private final ScheduledMethodRunnable task;
    private final SchedulerCustomizerInterface schedulerCustomizer;

    public CustomizableRunnable(ScheduledMethodRunnable task, SchedulerCustomizerInterface schedulerCustomizer) {
        this.task = requireNonNull(task);
        this.schedulerCustomizer = schedulerCustomizer;
    }

    @Override
    public void run() {
        schedulerCustomizer.executeBefore(task.getTarget(), task.getMethod());
        if (schedulerCustomizer.checkEnableCondition(task.getTarget(), task.getMethod()))
            task.run();
        schedulerCustomizer.executeAfter(task.getTarget(), task.getMethod());
    }
}
