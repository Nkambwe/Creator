package com.pbu.agents.services;

import com.pbu.agents.requests.SettingsRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SettingService {
    CompletableFuture<Boolean> existsById(long id);
    CompletableFuture<Boolean> existsByParamName(String paramName);
    CompletableFuture<SettingsRequest> findByParamName(String paramName);
    CompletableFuture<List<SettingsRequest>> findAllByParamNames(List<String> paramNames);
    CompletableFuture<SettingsRequest> create(SettingsRequest settings) throws InterruptedException;
    void updateSettings(SettingsRequest setting);
    void softDelete(long id, boolean is_deleted);
    void deleteById(long id);
}
