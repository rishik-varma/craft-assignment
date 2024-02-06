package com.craft.demo.repositories;

import com.craft.demo.models.enitities.Job;
import com.craft.demo.models.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findTop10ByOrderByCreatedAtDesc();
    List<Job> findTop10ByJobStatusOrderByNumberOfBidsDesc(JobStatus jobStatus);
}
