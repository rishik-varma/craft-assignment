package com.craft.demo.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidDTO extends BaseDTO {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date biddingTime;
    private double biddingAmount;
    @JsonIgnore
    private JobDTO job;
    private UserDTO bidder;
}
