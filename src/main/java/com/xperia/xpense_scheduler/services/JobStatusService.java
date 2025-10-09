package com.xperia.xpense_scheduler.services;


import com.xperia.xpense_scheduler.models.entity.tracker.JobStatus;

public interface JobStatusService {

    JobStatus saveStatus(JobStatus jobStatus);
}
