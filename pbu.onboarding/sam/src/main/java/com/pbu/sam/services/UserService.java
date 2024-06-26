package com.pbu.sam.services;

import com.pbu.sam.requests.LoginRequest;
import com.pbu.sam.requests.UserRequest;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    boolean exists(long id);
    boolean pfNoTaken(String pfNo);
    boolean pfNoDuplicated(String pfNo, long id);
    boolean usernameTaken(String username);
    boolean usernameDuplicated(String username, long id);
    boolean emailTaken(String email);
    boolean emailDuplicated(String email, long id);
    CompletableFuture<UserRequest> findById(long id);
    CompletableFuture<UserRequest> findByUsername(String username);
    CompletableFuture<LoginRequest> loginUser(String username);
    CompletableFuture<UserRequest> findByEmail(String email);
    CompletableFuture<List<UserRequest>> findAll();
    CompletableFuture<List<UserRequest>> findActive();
    CompletableFuture<UserRequest> findByIdWithRole(long userI);
    CompletableFuture<UserRequest> create(UserRequest user);
    void update(UserRequest user, long roleId, long branchId);
    void updatePassword(long id, String password);
    void setActiveStatus(long id, boolean active, String modifiedBy, String modifiedOn);
    void softDelete(long id, boolean status);
    void delete(long id);
}
