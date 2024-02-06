package com.craft.demo.service;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.BidDTO;
import com.craft.demo.models.dtos.JobDTO;
import com.craft.demo.models.enitities.Job;
import com.craft.demo.models.enums.JobStatus;
import com.craft.demo.models.enums.SortBy;

import java.util.List;

public interface JobService {
    public GenericResponse<JobDTO> postJob(JobDTO job, long userId);
    public GenericResponse<List<JobDTO>> fetchTopTenJobs(SortBy condition);
    public GenericResponse<JobDTO> placeBid(BidDTO bidDTO, long jobId, long bidderId);

    public GenericResponse<JobDTO> getJob(long jobId);

    public GenericResponse<JobDTO> updateJobStatus(JobDTO jobDTO);
}
