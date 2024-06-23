package com.pbu.agents.services;

import com.pbu.agents.models.IndividualAgent;
import com.pbu.agents.requests.IndividualRequest;
import com.pbu.agents.requests.PartnerRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PartnerService {
    CompletableFuture<Boolean> partnerExists(long id);
    CompletableFuture<Boolean> existsByFullNameAndAgentId(String fullName, Long agentId);
    CompletableFuture<Boolean> existsByIdAndFullNameAndAgentId(Long id, String fullName, Long agentId);
    CompletableFuture<Boolean>  existsByFullNameAndAgentIdNotId(String fullName, Long agentId, Long id);
    CompletableFuture<PartnerRequest> findPartnerById(long id);
    CompletableFuture<PartnerRequest>  findPartnerByFullName(String name, long agentId);
    CompletableFuture<List<PartnerRequest>> getPartners(long agentId);
    CompletableFuture<PartnerRequest> create(PartnerRequest partner, IndividualRequest agent) throws InterruptedException;
    void update(PartnerRequest partner, IndividualRequest agent);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}

