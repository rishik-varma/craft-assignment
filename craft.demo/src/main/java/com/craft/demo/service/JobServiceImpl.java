package com.craft.demo.service;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.BidDTO;
import com.craft.demo.models.dtos.JobDTO;
import com.craft.demo.models.dtos.UserDTO;
import com.craft.demo.models.enitities.Bid;
import com.craft.demo.models.enitities.Job;
import com.craft.demo.models.enitities.User;
import com.craft.demo.models.enums.JobStatus;
import com.craft.demo.models.enums.SortBy;
import com.craft.demo.repositories.BidRepository;
import com.craft.demo.repositories.CustomORM;
import com.craft.demo.repositories.JobRepository;
import com.craft.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GenericResponse<JobDTO> postJob(JobDTO jobDTO, long userId) {
        try {
            String jobDescription = jobDTO.getDescription();
            String jobRequirements = jobDTO.getRequirements();
            System.out.println(jobDescription.getBytes().length);
            if(jobDescription.getBytes().length > 16000) {
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, "Job Description exceeded 16KB !!");
            }
            System.out.println(jobRequirements.getBytes().length);
            if(jobRequirements.getBytes().length > 16000) {
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, "Job Requirements exceeded 16KB !!");
            }

            User user = CustomORM.findUserById(userId);
            jobDTO.setJobPoster(modelMapper.map(user, UserDTO.class));
            Job savedJob = CustomORM.saveJob(modelMapper.map(jobDTO, Job.class));
            return new GenericResponse<>(HttpStatus.CREATED, modelMapper.map(savedJob, JobDTO.class), "Success");
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failure");
        }
    }

    @Override
    public GenericResponse<List<JobDTO>> fetchTopTenJobs(SortBy condition) {
        try {
            List<Job> jobList;
            switch (condition){
                case RECENTLY_PUBLISHED :
                    jobList = CustomORM.fetchTop10MostRecentlyPublishedJobs();
                    break;
                case MOST_ACTIVE_AND_OPEN:
                    jobList = CustomORM.fetchTop10OpenJobsByNumberOfBidsDesc();
                    break;
                default:
                    throw new Exception("Sort Operation not supported !!");
            }

            return new GenericResponse<>(HttpStatus.OK, jobList.stream().map(job -> modelMapper.map(job, JobDTO.class)).collect(Collectors.toList()), "Success");
        } catch (Exception e) {
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failure");
        }
    }

    @Override
    public GenericResponse<JobDTO> placeBid(BidDTO bidDTO, long jobId, long bidderId) {
        try {
            Job job = CustomORM.findJobById(jobId);
            JobDTO jobDTO = modelMapper.map(job, JobDTO.class);

            if(jobDTO.getBids()!= null && !jobDTO.getBids().isEmpty()
                    && jobDTO.getBids().get(0).getBiddingAmount() <= bidDTO.getBiddingAmount()) {
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, "Bidding Amount must be less than current minimum bid amount!!");
            }

            User bidder = CustomORM.findUserById(bidderId);
            bidDTO.setBidder(modelMapper.map(bidder, UserDTO.class));


            bidDTO.setJob(jobDTO);

            Bid savedBid = CustomORM.saveBid(modelMapper.map(bidDTO, Bid.class));




            if (jobDTO.getBids() == null) {
                jobDTO.setBids(new ArrayList<>());
            }
            jobDTO.getBids().add(modelMapper.map(savedBid, BidDTO.class));

            Job jobToSave = modelMapper.map(jobDTO, Job.class);

            Job savedJob = CustomORM.saveJob(jobToSave);
            return new GenericResponse<>(HttpStatus.CREATED, modelMapper.map(savedJob, JobDTO.class), "Success");
        }catch (Exception e) {
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failure");
        }
    }

    @Override
    public GenericResponse<JobDTO> getJob(long jobId) {
        try {
            Job job = CustomORM.findJobById(jobId);
            JobDTO jobDTO = modelMapper.map(job, JobDTO.class);

            return new GenericResponse<>(HttpStatus.OK, jobDTO, "Success");
        } catch (Exception e) {
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failure");
        }
    }

    @Override
    public GenericResponse<JobDTO> updateJobStatus(JobDTO jobDTO) {
        try {
            Job job = CustomORM.findJobById(jobDTO.getId());
            JobDTO jobDTOFromDb = modelMapper.map(job, JobDTO.class);

            jobDTOFromDb.setJobStatus(jobDTO.getJobStatus());

            Job savedJob = CustomORM.saveJob(modelMapper.map(jobDTOFromDb, Job.class));

            return new GenericResponse<>(HttpStatus.OK, modelMapper.map(savedJob, JobDTO.class), "Success");

        } catch (Exception e) {
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failure");
        }
    }
}