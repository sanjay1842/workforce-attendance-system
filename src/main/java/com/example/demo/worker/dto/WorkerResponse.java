package com.example.demo.worker.dto;

import com.example.demo.worker.Designation;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class WorkerResponse {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private Designation designation;
    private BigDecimal hourlyWage;
    private boolean active;
}