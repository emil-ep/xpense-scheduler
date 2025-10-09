package com.xperia.xpense_scheduler.jobs.scheduler;

public interface ScheduledJob {

    String getName();

    void execute();

    boolean isEnabled();

}
