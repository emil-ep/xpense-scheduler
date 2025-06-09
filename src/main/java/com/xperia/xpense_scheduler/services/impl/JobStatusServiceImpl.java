package com.xperia.xpense_scheduler.services.impl;

import com.xperia.xpense_scheduler.jobs.models.JobStatus;
import com.xperia.xpense_scheduler.repository.JobStatusRepository;
import com.xperia.xpense_scheduler.services.JobStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobStatusServiceImpl implements JobStatusService {

    @Autowired
    private JobStatusRepository jobStatusRepository;

    @Override
    public JobStatus saveStatus(JobStatus jobStatus) {

        return jobStatusRepository.save(jobStatus);
    }
}
