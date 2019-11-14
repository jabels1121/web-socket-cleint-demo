package com.jaybe.websocketcleintdemo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Block {

    private String blockNumber;

    private State blockState;

    private Long lastUpdated;

}
