package com.craft.demo.models.enitities;

import com.craft.demo.models.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Job extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "requirements")
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatus jobStatus;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "job_poster_id")
    private User jobPoster;

    @Column(name = "recruiter_contact")
    private String recruiterContact;

    @Column(name = "name_of_recruiter")
    private String nameOfRecruiter;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "auction_expiry_time")
    private Date auctionExpiryTime;

    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
    @OrderBy(value = "biddingAmount")
    @JsonManagedReference
    private List<Bid> bids;

    private int numberOfBids;
}
