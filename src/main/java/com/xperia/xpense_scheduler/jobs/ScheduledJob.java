package com.xperia.xpense_scheduler.jobs;

public interface ScheduledJob {

    String getName();

    void execute();

    boolean isEnabled();

}
