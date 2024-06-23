package com.pbu.agents.services;

import com.pbu.agents.models.IndividualAgent;
import com.pbu.agents.requests.IndividualRequest;
import com.pbu.agents.requests.KinRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface KinService {
    CompletableFuture<Boolean> kinExists(long id);
    CompletableFuture<Boolean>  existsByFullNameAndAgentId(String fullName, Long agentId);
    CompletableFuture<Boolean>  existsByFullNameAndAgentIdNotId(String fullName, Long agentId, Long id);
    CompletableFuture<KinRequest> findKinById(long id);
    CompletableFuture<KinRequest>  findKinByName(String name, long agentId);
    CompletableFuture<List<KinRequest>> getKins(long agentId);
    CompletableFuture<KinRequest> create(KinRequest kin, IndividualRequest agent) throws InterruptedException;
    void update(KinRequest kin, IndividualRequest agent);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}

