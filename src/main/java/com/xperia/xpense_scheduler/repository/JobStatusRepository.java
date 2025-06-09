package com.xperia.xpense_scheduler.repository;

import com.xperia.xpense_scheduler.jobs.models.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobStatusRepository extends JpaRepository<JobStatus, String> {

}
