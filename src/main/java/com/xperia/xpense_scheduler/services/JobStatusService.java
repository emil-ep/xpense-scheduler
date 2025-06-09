package com.xperia.xpense_scheduler.services;


import com.xperia.xpense_scheduler.jobs.models.JobStatus;

public interface JobStatusService {

    JobStatus saveStatus(JobStatus jobStatus);
}
