package com.pbu.agents.services;
import com.pbu.agents.requests.TelecomRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TelecomService {
    CompletableFuture<Boolean> telecomExists(long id);
    CompletableFuture<Boolean> existsByName(String name);
    CompletableFuture<Boolean>  existsByNameAndNotId(String name, Long id);
    CompletableFuture<TelecomRequest> findTelecomById(long id);
    CompletableFuture<TelecomRequest>  findTelecomByName(String name);
    CompletableFuture<List<TelecomRequest>> getAllTelecoms();
    CompletableFuture<List<TelecomRequest>> getActiveTelecoms();
    CompletableFuture<TelecomRequest> create(TelecomRequest telecom) throws InterruptedException;
    void update(TelecomRequest telecom);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}
