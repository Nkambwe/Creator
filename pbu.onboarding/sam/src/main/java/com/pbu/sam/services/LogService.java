package com.pbu.sam.services;

import com.pbu.sam.requests.LogRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LogService {
    CompletableFuture<List<LogRequest>> findAll();
    CompletableFuture<LogRequest> create(LogRequest log);
    CompletableFuture<List<LogRequest>> findAll(LocalDateTime startDate, LocalDateTime endDate);
}
