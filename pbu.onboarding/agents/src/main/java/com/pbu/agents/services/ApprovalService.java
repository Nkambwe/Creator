package com.pbu.agents.services;

import com.pbu.agents.enums.AgentType;
import com.pbu.agents.requests.ApprovalRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApprovalService {
    CompletableFuture<Boolean> existsById(long id);
    CompletableFuture<Boolean>  existsByPersonId(long personId);
    CompletableFuture<Boolean>  existsByBusinessId(long businessId);
    CompletableFuture<ApprovalRequest> findApprovalById(long id);
    CompletableFuture<List<ApprovalRequest>> getApprovals(long agentId, AgentType type);
    CompletableFuture<ApprovalRequest> create(ApprovalRequest approval, AgentType type) throws InterruptedException;
    void update(ApprovalRequest approval);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}

