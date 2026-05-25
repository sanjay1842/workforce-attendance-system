package com.example.demo.overtime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OvertimeEntryRepository extends JpaRepository<OvertimeEntry, Long> {
}