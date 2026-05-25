package com.example.demo.attendance;

import com.example.demo.attendance.exception.AttendanceException;
import com.example.demo.overtime.OvertimeEntry;
import com.example.demo.overtime.OvertimeEntryRepository;
import com.example.demo.overtime.SettlementStatus;
import com.example.demo.site.Site;
import com.example.demo.site.SiteRepository;
import com.example.demo.worker.Worker;
import com.example.demo.worker.WorkerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceLogRepository attendanceLogRepository;
    private final WorkerRepository workerRepository;
    private final SiteRepository siteRepository;
    private final OvertimeEntryRepository overtimeEntryRepository;

    @Transactional
    public AttendanceLog clockIn(Long workerId, Long siteId) {

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new AttendanceException("Worker not found"));

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new AttendanceException("Site not found"));

        if (!worker.isActive()) {
            throw new AttendanceException("Worker is inactive");
        }

        if (!site.isActive()) {
            throw new AttendanceException("Site is inactive");
        }

        attendanceLogRepository
                .findByWorkerAndClockOutTimeIsNull(worker)
                .ifPresent(existing -> {
                    throw new AttendanceException("Worker already clocked in");
                });

        AttendanceLog attendanceLog = AttendanceLog.builder()
                .worker(worker)
                .site(site)
                .clockInTime(LocalDateTime.now())
                .build();

        return attendanceLogRepository.save(attendanceLog);
    }

    @Transactional
    public AttendanceLog clockOut(Long workerId) {

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new AttendanceException("Worker not found"));

        AttendanceLog attendanceLog = attendanceLogRepository
                .findByWorkerAndClockOutTimeIsNull(worker)
                .orElseThrow(() -> new AttendanceException("No active attendance found"));

        LocalDateTime clockOutTime = LocalDateTime.now();

        double totalHours = Duration.between(
                attendanceLog.getClockInTime(),
                clockOutTime
        ).toMinutes() / 60.0;

        attendanceLog.setClockOutTime(clockOutTime);
        attendanceLog.setTotalHoursWorked(totalHours);

        if (totalHours > 16) {
            attendanceLog.setFlaggedForReview(true);
        }

        attendanceLogRepository.save(attendanceLog);

        double overtimeHours = Math.max(0, totalHours - 8);

        if (overtimeHours > 0) {

            BigDecimal overtimeAmount = worker.getHourlyWage()
                    .multiply(BigDecimal.valueOf(overtimeHours * 1.5));

            OvertimeEntry overtimeEntry = OvertimeEntry.builder()
                    .attendanceLog(attendanceLog)
                    .worker(worker)
                    .overtimeHours(overtimeHours)
                    .overtimeAmount(overtimeAmount)
                    .settlementStatus(SettlementStatus.PENDING)
                    .build();

            overtimeEntryRepository.save(overtimeEntry);
        }

        return attendanceLog;
    }
}