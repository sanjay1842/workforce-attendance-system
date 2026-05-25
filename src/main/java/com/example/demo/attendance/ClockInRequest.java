package com.example.demo.attendance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockInRequest {

    private Long workerId;
    private Long siteId;
}