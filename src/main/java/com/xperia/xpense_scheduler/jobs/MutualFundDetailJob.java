package com.xperia.xpense_scheduler.jobs;

import com.xperia.xpense_scheduler.jobs.scheduler.ScheduledJob;
import com.xperia.xpense_scheduler.kafka.XpenseProducer;
import com.xperia.xpense_scheduler.models.entity.mf.MutualFundScheme;
import com.xperia.xpense_scheduler.models.entity.tracker.JobStatus;
import com.xperia.xpense_scheduler.services.JobStatusService;
import com.xperia.xpense_scheduler.services.MutualFundSchemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.xperia.models.JobStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component("MutualFundDetailJob")
public class MutualFundDetailJob implements ScheduledJob {

    private final MutualFundSchemeService mutualFundSchemeService;

    private final JobStatusService jobStatusService;

    private final XpenseProducer<String, String> kafkaProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(MutualFundDetailJob.class);

    @Autowired
    public MutualFundDetailJob(@Qualifier("mutualFundDetailXpenseProducer") XpenseProducer<String, String> kafkaProducer,
                               JobStatusService jobStatusService, MutualFundSchemeService mutualFundSchemeService){
        this.kafkaProducer = kafkaProducer;
        this.jobStatusService = jobStatusService;
        this.mutualFundSchemeService = mutualFundSchemeService;
    }


    @Override
    public String getName() {
        return "MutualFundDetailJob";
    }

    @Override
    public void execute() {
        Long startTime = System.currentTimeMillis();
        LOGGER.info("Executing MutualFundDetailJob");

        JobStatus jobStatus = new JobStatus("MutualFundDetailJob", System.currentTimeMillis(), JobStatusEnum.STARTED);
        jobStatus = jobStatusService.saveStatus(jobStatus);

        Optional<List<MutualFundScheme>> schemes = mutualFundSchemeService.findAllSchemes();
        if (schemes.isEmpty()){
            LOGGER.info("Couldn't find any schemes available in database. Skipping job");
        }

        schemes.get().forEach(scheme -> {
            kafkaProducer.send("scheme_detail", "schemeCode", scheme.getCode());
        });


        Long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        Long timeTakenInSeconds = TimeUnit.SECONDS.convert(timeTaken, TimeUnit.MILLISECONDS);
        LOGGER.info("Completed MutualFundDetailJob in {} seconds", timeTakenInSeconds);
        jobStatus.setStatus(JobStatusEnum.COMPLETED);
        jobStatusService.saveStatus(jobStatus);

    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
