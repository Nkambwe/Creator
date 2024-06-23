package com.pbu.sam.services;

import com.pbu.sam.requests.AppRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AppService {
    CompletableFuture<AppRequest> findById(long id);
    CompletableFuture<AppRequest>  findByName(String name);
    CompletableFuture<List<AppRequest>> findAll(long owner_id);
    CompletableFuture<AppRequest>  create(AppRequest app);
    void updateApp(AppRequest app);
    void softDelete(long id, boolean isDeleted);
}
