package com.xperia.xpense_scheduler.jobs;

import com.xperia.xpense_scheduler.jobs.scheduler.ScheduledJob;
import org.springframework.stereotype.Component;

@Component("MutualFundDetailJob")
public class MutualFundDetailJob implements ScheduledJob {
    @Override
    public String getName() {
        return "MutualFundDetailJob";
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
