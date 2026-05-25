package com.example.demo.worker;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import com.example.demo.worker.dto.WorkerResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerRepository workerRepository;

    @PostMapping
    //@CacheEvict(value = "workers", allEntries = true)
    public WorkerResponse createWorker(@RequestBody Worker worker) {

        Worker savedWorker = workerRepository.save(worker);

        return WorkerResponse.builder()
            .id(savedWorker.getId())
            .fullName(savedWorker.getFullName())
            .phoneNumber(savedWorker.getPhoneNumber())
            .designation(savedWorker.getDesignation())
            .hourlyWage(savedWorker.getHourlyWage())
            .active(savedWorker.isActive())
            .build();
}

    @GetMapping
//@Cacheable("workers")
public List<WorkerResponse> getAllWorkers() {

    return workerRepository.findAll()
            .stream()
            .map(worker -> WorkerResponse.builder()
                    .id(worker.getId())
                    .fullName(worker.getFullName())
                    .phoneNumber(worker.getPhoneNumber())
                    .designation(worker.getDesignation())
                    .hourlyWage(worker.getHourlyWage())
                    .active(worker.isActive())
                    .build())
            .collect(Collectors.toList());
}
}