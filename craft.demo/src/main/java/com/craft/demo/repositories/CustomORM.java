package com.craft.demo.repositories;

import com.craft.demo.models.enitities.Bid;
import com.craft.demo.models.enitities.Job;
import com.craft.demo.models.enitities.User;
import com.craft.demo.models.enums.JobStatus;
import com.craft.demo.models.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomORM {

    private static UserRepository userRepository;
    private static JobRepository jobRepository;
    private static BidRepository bidRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        CustomORM.userRepository = userRepository;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        CustomORM.jobRepository = jobRepository;
    }

    @Autowired
    public void setBidRepository(BidRepository bidRepository) {
        CustomORM.bidRepository = bidRepository;
    }

    public static User saveUser(User user) {
        return userRepository.save(user);
    }

    public static User findUserById(long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given Id !!"));
    }
    
    public static User findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
    
    public static Job saveJob(Job job) {
        job.setNumberOfBids(job.getBids() == null ? 0 : job.getBids().size());
        return jobRepository.save(job);
    }
    
    public static Job findJobById(long jobId) throws ResourceNotFoundException {
        return jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job not found with given Id !!"));
    }
    
    public static Bid saveBid(Bid bid) {
        return bidRepository.save(bid);
    }

    public static List<Job> fetchTop10OpenJobsByNumberOfBidsDesc() {
        return jobRepository.findTop10ByJobStatusOrderByNumberOfBidsDesc(JobStatus.OPEN);
    }

    public static List<Job> fetchTop10MostRecentlyPublishedJobs() {
        return jobRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
