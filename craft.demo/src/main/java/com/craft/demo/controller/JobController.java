package com.craft.demo.controller;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.BidDTO;
import com.craft.demo.models.dtos.JobDTO;
import com.craft.demo.models.enums.SortBy;
import com.craft.demo.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping("")
    public ResponseEntity<GenericResponse> listJob(@RequestBody JobDTO jobDTO, @RequestParam(name = "poster_id") long userId) {
        GenericResponse<JobDTO> response = jobService.postJob(jobDTO, userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("")
    public ResponseEntity<GenericResponse> fetchJobById(@RequestParam long jobId) {
        GenericResponse<JobDTO> response = jobService.getJob(jobId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/fetchTop10Jobs")
    public ResponseEntity<GenericResponse> fetchTopTenJobs(@RequestParam String sortCondition) {
        SortBy sortBy = SortBy.valueOf(sortCondition);
        GenericResponse<List<JobDTO>> response = jobService.fetchTopTenJobs(sortBy);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/{jobId}/bids")
    public ResponseEntity<GenericResponse> placeBid(@RequestBody BidDTO bidDTO, @RequestParam(name = "bidder_id") long bidderId,
                                                    @PathVariable long jobId) {

        GenericResponse<JobDTO> response = jobService.placeBid(bidDTO, jobId, bidderId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/updateJobStatus")
    public ResponseEntity<GenericResponse> updateJobStatus(@RequestBody JobDTO jobDTO) {
        GenericResponse<JobDTO> response = jobService.updateJobStatus(jobDTO);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
