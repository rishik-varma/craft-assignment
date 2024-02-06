package com.craft.demo.models.enitities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Entity
public class Bid extends BaseEntity {

    @Column(name = "bidding_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date biddingTime;

    @Column(name = "bidding_amount")
    private double biddingAmount;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "job_id")
    @JsonBackReference
    @JsonIgnore
    private Job job;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "bidder_id")
    private User bidder;
}
