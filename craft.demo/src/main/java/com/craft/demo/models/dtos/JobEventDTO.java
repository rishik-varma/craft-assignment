package com.craft.demo.models.dtos;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobEventDTO {
    private int numberOfBids;
    private String winner;
    private double minimumBiddingAmount;
}
