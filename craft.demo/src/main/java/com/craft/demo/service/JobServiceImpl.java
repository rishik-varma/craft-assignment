package com.craft.demo.service;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.BidDTO;
import com.craft.demo.models.dtos.JobDTO;
import com.craft.demo.models.dtos.UserDTO;
import com.craft.demo.models.enitities.Bid;
import com.craft.demo.models.enitities.Job;
import com.craft.demo.models.enitities.User;
import com.craft.demo.models.enums.SortBy;
import com.craft.demo.models.enums.UserType;
import com.craft.demo.models.exceptions.ResourceNotFoundException;
import com.craft.demo.models.exceptions.SortConditionNotSupportedException;
import com.craft.demo.repositories.CustomORM;
import com.craft.demo.utils.CraftConstants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.craft.demo.models.enums.SortBy.MOST_ACTIVE_AND_OPEN;
import static com.craft.demo.models.enums.SortBy.RECENTLY_PUBLISHED;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GenericResponse<JobDTO> postJob(JobDTO jobDTO, long userId) {
        try {
            logger.info("JobServiceImpl.postJob => method called with Job : {}, userId : {}", jobDTO, userId);
            String jobDescription = jobDTO.getDescription();
            String jobRequirements = jobDTO.getRequirements();
            if(jobDescription.getBytes().length > 16000) {
                logger.error("JobServiceImpl.postJob => " + CraftConstants.JOB_DESCRIPTION_SIZE_LIMIT_EXCEEDED, jobDTO);
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, CraftConstants.JOB_DESCRIPTION_SIZE_LIMIT_EXCEEDED);
            }

            if(jobRequirements.getBytes().length > 16000) {
                logger.error("JobServiceImpl.postJob => " + CraftConstants.JOB_REQUIREMENTS_SIZE_LIMIT_EXCEEDED, jobDTO);
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, CraftConstants.JOB_DESCRIPTION_SIZE_LIMIT_EXCEEDED);
            }

            User user = CustomORM.findUserById(userId);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            if(userDTO.getUserType() != UserType.POSTER) {
                logger.error("JobServiceImpl.postJob => " + CraftConstants.INVALID_USER_TYPE);
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, CraftConstants.INVALID_USER_TYPE);
            }

            jobDTO.setJobPoster(userDTO);

            Job savedJob = CustomORM.saveJob(modelMapper.map(jobDTO, Job.class));
            return new GenericResponse<>(HttpStatus.CREATED, modelMapper.map(savedJob, JobDTO.class), CraftConstants.SUCCESS_MESSAGE);
        } catch (ResourceNotFoundException e) {
            logger.error("JobServiceImpl.postJob => {}", e.getMessage());
            return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch (Exception e) {
            logger.error("JobServiceImpl.postJob => " + CraftConstants.EXCEPTION_OCCURRED_MESSAGE, e);
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, CraftConstants.FAILURE_MESSAGE);
        }
    }

    @Override
    public GenericResponse<List<JobDTO>> fetchTopTenJobs(String condition) {
        try {
            logger.info("JobServiceImpl.fetchTopTenJobs => method called with condition : {} ", condition);
            List<Job> jobList;
            SortBy searchCondition = Arrays.stream(SortBy.values())
                    .filter(sortBy -> sortBy.name().equalsIgnoreCase(condition))
                    .findAny()
                    .orElseThrow(() -> new SortConditionNotSupportedException());

            switch (searchCondition){
                case RECENTLY_PUBLISHED :
                    jobList = CustomORM.fetchTop10MostRecentlyPublishedJobs();
                    break;
                case MOST_ACTIVE_AND_OPEN:
                    jobList = CustomORM.fetchTop10OpenJobsByNumberOfBidsDesc();
                    break;
                default:
                    throw new SortConditionNotSupportedException();
            }

            return new GenericResponse<>(HttpStatus.OK, jobList.stream().map(job -> modelMapper.map(job, JobDTO.class)).collect(Collectors.toList()), CraftConstants.SUCCESS_MESSAGE);
        } catch (SortConditionNotSupportedException e) {
            logger.error("JobServiceImpl.fetchTop10Jobs => " + e.getMessage());
            return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch (Exception e) {
            logger.error("JobServiceImpl.fetchTopTenJobs => " + CraftConstants.EXCEPTION_OCCURRED_MESSAGE, e);
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, CraftConstants.FAILURE_MESSAGE);
        }
    }

    @Override
    public GenericResponse<JobDTO> placeBid(BidDTO bidDTO, long jobId, long bidderId) {
        try {
            logger.info("JobServiceImpl.placeBid => method called with Bid: {}, jobId: {}, bidderId: {}", bidDTO, jobId, bidderId);
            Job job = CustomORM.findJobById(jobId);
            JobDTO jobDTO = modelMapper.map(job, JobDTO.class);

            if(jobDTO.getBids()!= null && !jobDTO.getBids().isEmpty()
                    && jobDTO.getBids().get(0).getBiddingAmount() <= bidDTO.getBiddingAmount()) {
                logger.error("JobServiceImpl.placeBid => " + CraftConstants.HIGHER_BIDDING_AMOUNT_ERROR_MESSAGE);
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, CraftConstants.HIGHER_BIDDING_AMOUNT_ERROR_MESSAGE);
            }

            User bidder = CustomORM.findUserById(bidderId);
            UserDTO bidderDTO = modelMapper.map(bidder, UserDTO.class);

            if(bidderDTO.getUserType() != UserType.BIDDER) {
                logger.error("JobServiceImpl.placeBid => "  + CraftConstants.INVALID_USER_TYPE);
                return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, CraftConstants.INVALID_USER_TYPE);
            }

            bidDTO.setBidder(bidderDTO);
            bidDTO.setJob(jobDTO);

            Bid savedBid = CustomORM.saveBid(modelMapper.map(bidDTO, Bid.class));

            if (jobDTO.getBids() == null) {
                jobDTO.setBids(new ArrayList<>());
            }
            jobDTO.getBids().add(modelMapper.map(savedBid, BidDTO.class));

            Job jobToSave = modelMapper.map(jobDTO, Job.class);

            Job savedJob = CustomORM.saveJob(jobToSave);
            return new GenericResponse<>(HttpStatus.CREATED, modelMapper.map(savedJob, JobDTO.class), CraftConstants.SUCCESS_MESSAGE);
        } catch (ResourceNotFoundException e) {
            logger.error("JobServiceImpl.placeBid => " + e.getMessage());
            return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }
        catch (Exception e) {
            logger.error("JobServiceImpl.placeBid => " + CraftConstants.EXCEPTION_OCCURRED_MESSAGE, e);
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, CraftConstants.FAILURE_MESSAGE);
        }
    }

    @Override
    public GenericResponse<JobDTO> getJob(long jobId) {
        try {
            logger.info("JobServiceImpl.getJob => method called with jobId: {}", jobId);
            Job job = CustomORM.findJobById(jobId);
            JobDTO jobDTO = modelMapper.map(job, JobDTO.class);

            return new GenericResponse<>(HttpStatus.OK, jobDTO, CraftConstants.SUCCESS_MESSAGE);
        } catch(ResourceNotFoundException e) {
            logger.error("JobServiceImpl.getJob => " + e.getMessage());
            return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch(Exception e) {
            logger.error("JobServiceImpl.getJob => " + CraftConstants.EXCEPTION_OCCURRED_MESSAGE, e);
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, CraftConstants.FAILURE_MESSAGE);
        }
    }

    @Override
    public GenericResponse<JobDTO> updateJobStatus(JobDTO jobDTO) {
        try {
            logger.info("JobServiceImpl.updateJobStatus => method called with job: {}", jobDTO);
            Job job = CustomORM.findJobById(jobDTO.getId());
            JobDTO jobDTOFromDb = modelMapper.map(job, JobDTO.class);

            jobDTOFromDb.setJobStatus(jobDTO.getJobStatus());

            Job savedJob = CustomORM.saveJob(modelMapper.map(jobDTOFromDb, Job.class));

            return new GenericResponse<>(HttpStatus.OK, modelMapper.map(savedJob, JobDTO.class), CraftConstants.SUCCESS_MESSAGE);

        } catch (ResourceNotFoundException e) {
            logger.error("JobServiceImpl.updateJobStatus => " + e.getMessage());
            return new GenericResponse<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch (Exception e) {
            logger.error("JobServiceImpl.updateJobStatus => " + CraftConstants.EXCEPTION_OCCURRED_MESSAGE, e);
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, CraftConstants.FAILURE_MESSAGE);
        }
    }
}
