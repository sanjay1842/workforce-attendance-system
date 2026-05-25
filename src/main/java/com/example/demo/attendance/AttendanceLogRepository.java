package com.example.demo.attendance;

import com.example.demo.worker.Worker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

    Optional<AttendanceLog> findByWorkerAndClockOutTimeIsNull(Worker worker);
}