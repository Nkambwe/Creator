package com.pbu.sam.services;

import com.pbu.sam.requests.ArchiveUserRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArchivedUserService {
    CompletableFuture<List<ArchiveUserRequest>> findAll();
    CompletableFuture<ArchiveUserRequest> findById(long id);
    CompletableFuture<ArchiveUserRequest>  findByPfNo(String pfNo);
    CompletableFuture<ArchiveUserRequest> create(ArchiveUserRequest log) throws InterruptedException;
    void delete(long id);
}
