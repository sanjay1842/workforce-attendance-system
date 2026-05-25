package com.example.demo.attendance;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/clock-in")
    public AttendanceLog clockIn(@RequestBody ClockInRequest request) {

        return attendanceService.clockIn(
                request.getWorkerId(),
                request.getSiteId()
        );
    }

    @PostMapping("/clock-out/{workerId}")
    public AttendanceLog clockOut(@PathVariable Long workerId) {

        return attendanceService.clockOut(workerId);
    }
}