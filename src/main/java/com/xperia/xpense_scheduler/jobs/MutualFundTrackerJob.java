package com.xperia.xpense_scheduler.jobs;


import com.xperia.xpense_scheduler.jobs.models.JobStatus;
import com.xperia.xpense_scheduler.jobs.models.JobStatusEnum;
import com.xperia.xpense_scheduler.jobs.models.MutualFundScheme;
import com.xperia.xpense_scheduler.kafka.XpenseProducer;
import com.xperia.xpense_scheduler.services.JobStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component("MutualFundTrackerJob")
public class MutualFundTrackerJob implements ScheduledJob{

    private static final Logger LOGGER = LoggerFactory.getLogger(MutualFundTrackerJob.class);

    private final RestTemplate restTemplate;

    private final JobStatusService jobStatusService;

    private final XpenseProducer kafkaProducer;

    @Value("${mutualFund.api.url}")
    private String mutualFundUrl;

    @Value("${mutualFund.job.enabled}")
    private boolean jobEnabled;

    @Autowired
    public MutualFundTrackerJob(RestTemplate restTemplate, JobStatusService jobStatusService, XpenseProducer kafkaProducer){
        this.restTemplate = restTemplate;
        this.jobStatusService = jobStatusService;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public String getName() {
        return "MutualFundTrackerJob";
    }

    @Override
    public void execute() {
        Long startTime = System.currentTimeMillis();
        LOGGER.info("Executing MutualFundTrackerJob");
        JobStatus jobStatus = new JobStatus("MutualFundTrackerJob", System.currentTimeMillis(), JobStatusEnum.STARTED);
        jobStatus = jobStatusService.saveStatus(jobStatus);
        MutualFundScheme[] response = restTemplate.getForObject(mutualFundUrl, MutualFundScheme[].class);
        if (response != null){
            LOGGER.debug("Received data : {}", response.length);
            List<MutualFundScheme> list = List.of(response);
            list.forEach(scheme -> {
                kafkaProducer.send("mf_scheme", "scheme", scheme);
                LOGGER.debug("Send value {} to topic : {}", scheme, "mf_scheme");
            });
        }
        Long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        Long timeTakenInSeconds = TimeUnit.SECONDS.convert(timeTaken, TimeUnit.MILLISECONDS);
        LOGGER.info("Completed MutualFundTrackerJob in {} seconds", timeTakenInSeconds);
        jobStatus.setStatus(JobStatusEnum.COMPLETED);
        jobStatusService.saveStatus(jobStatus);
    }

    @Override
    public boolean isEnabled() {
        return jobEnabled;
    }

}
