package com.pbu.agents.services;

import com.pbu.agents.requests.DistrictRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DistrictServiceImp implements DistrictService {
    @Override
    public CompletableFuture<Boolean> districtExists(long id) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String name, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<DistrictRequest> findDistrictById(long id) {
        return null;
    }

    @Override
    public CompletableFuture<DistrictRequest> findDistrictByName(String name) {
        return null;
    }

    @Override
    public CompletableFuture<List<DistrictRequest>> getAllDistricts() {
        return null;
    }

    @Override
    public CompletableFuture<List<DistrictRequest>> getActiveDistricts() {
        return null;
    }

    @Override
    public CompletableFuture<DistrictRequest> create(DistrictRequest district) throws InterruptedException {
        return null;
    }

    @Override
    public void update(DistrictRequest district) {

    }

    @Override
    public void softDelete(long id, boolean deleted) {

    }

    @Override
    public void delete(long id) {

    }
}
