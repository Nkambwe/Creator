package com.pbu.agents.services;

import com.pbu.agents.requests.TelecomRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TelecomServiceImp implements TelecomService {
    @Override
    public CompletableFuture<Boolean> telecomExists(long id) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String name, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<TelecomRequest> findTelecomById(long id) {
        return null;
    }

    @Override
    public CompletableFuture<TelecomRequest> findTelecomByName(String name) {
        return null;
    }

    @Override
    public CompletableFuture<List<TelecomRequest>> getAllTelecoms() {
        return null;
    }

    @Override
    public CompletableFuture<List<TelecomRequest>> getActiveTelecoms() {
        return null;
    }

    @Override
    public CompletableFuture<TelecomRequest> create(TelecomRequest telecom) throws InterruptedException {
        return null;
    }

    @Override
    public void update(TelecomRequest telecom) {

    }

    @Override
    public void softDelete(long id, boolean deleted) {

    }

    @Override
    public void delete(long id) {

    }
}
