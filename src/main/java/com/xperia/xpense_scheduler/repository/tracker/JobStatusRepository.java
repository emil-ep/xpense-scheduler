package com.xperia.xpense_scheduler.repository.tracker;

import com.xperia.xpense_scheduler.models.entity.tracker.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobStatusRepository extends JpaRepository<JobStatus, String> {

}
