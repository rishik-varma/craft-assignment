package com.craft.demo.models.dtos;

import com.craft.demo.models.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class JobDTO extends BaseDTO {
    private String title;
    private String description;
    private String requirements;
    private JobStatus jobStatus;
    private UserDTO jobPoster;
    private String recruiterContact;
    private String nameOfRecruiter;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date auctionExpiryTime;

    private List<BidDTO> bids;

    public JobDTO() {
        this.bids = new ArrayList<>();
    }

    private int numberOfBids;
}
