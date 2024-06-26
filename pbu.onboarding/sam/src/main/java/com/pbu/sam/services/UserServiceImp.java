package com.pbu.sam.services;

import com.pbu.sam.models.App;
import com.pbu.sam.models.Branch;
import com.pbu.sam.models.Role;
import com.pbu.sam.models.User;
import com.pbu.sam.repositories.BranchRepository;
import com.pbu.sam.repositories.RoleRepository;
import com.pbu.sam.repositories.UserRepository;
import com.pbu.sam.requests.LoginRequest;
import com.pbu.sam.requests.UserRequest;
import com.pbu.utils.common.AppLoggerService;
import com.pbu.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImp implements UserService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final UserRepository users;
    private final RoleRepository roles;
    private final BranchRepository branches;

    public UserServiceImp(AppLoggerService logger, ModelMapper mapper, UserRepository users, RoleRepository roles,BranchRepository branches) {
        this.logger = logger;
        this.mapper = mapper;
        this.users = users;
        this.roles = roles;
        this.branches = branches;
    }

    @Override
    public boolean exists(long id) {
        logger.info(String.format("Checking whether user with id '%s' exists",id));
        try{
            return users.existsById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'exists' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean pfNoTaken(String pfNo) {
        logger.info(String.format("Checking whether pf no '%s' is in use",pfNo));
        try{
            return users.existsByPfNoIgnoreCase(pfNo);
        } catch(Exception ex){
            logger.info("An error occurred in method 'pfNoTaken' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean pfNoDuplicated(String pfNo, long id) {
        logger.info(String.format("Checking whether pf no '%s' is duplicated",pfNo));
        try{
            return users.existsByPfNoAndIdNot(pfNo, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'pfNoDuplicated' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean usernameTaken(String username) {
        logger.info(String.format("Checking whether username '%s' is in use",username));
        try{
            return users.existsByUsernameIgnoreCase(username);
        } catch(Exception ex){
            logger.info("An error occurred in method 'usernameTaken' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean emailTaken(String email) {
        logger.info(String.format("Checking whether email '%s' is in use",email));
        try{
            return users.existsByEmail(email);
        } catch(Exception ex){
            logger.info("An error occurred in method 'email' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean usernameDuplicated(String username, long id) {
        logger.info(String.format("Checking whether username '%s' is duplicated",username));
        try{
            return users.existsByUsernameAndIdNot(username, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'usernameDuplicated' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean emailDuplicated(String email, long id) {
        logger.info(String.format("Checking whether email '%s' is duplicated",email));
        try{
            return users.existsByEmailAndIdNot(email, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'emailTakenDuplicated' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<UserRequest> findById(long userId) {
        logger.info(String.format("Retrieving user with ID %s", userId));
        try{
            UserRequest request = null;
            Optional<User> user = users.findById(userId);
            if(user.isPresent()){
                request = this.mapper.map(user.get(), UserRequest.class);
            }
            return CompletableFuture.completedFuture(request);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<UserRequest> findByUsername(String username) {
        logger.info(String.format("Retrieving user with username %s", username));
        try{
            UserRequest request = null;
            User user = users.findByUsernameIgnoreCase(username);
            if(user != null){
                request = this.mapper.map(user, UserRequest.class);
                Role role = user.getRole();
                if(role != null){
                    request.setRoleId(role.getId());
                }
            }
            return CompletableFuture.completedFuture(request);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<LoginRequest> loginUser(String username){
        logger.info(String.format("Retrieving user login details for user with username %s", username));
        try{
            LoginRequest request = null;
            User user = users.findByUsernameIgnoreCase(username);
            if(user != null){
                request = new LoginRequest();
                request.setId(user.getId());
                request.setUsername(user.getUsername());
                request.setFirstname(user.getFirstname());
                request.setLastname(user.getLastname());
                request.setPfNo(user.getPfNo());
                request.setEmail(user.getEmail());
                request.setPassword(user.getPassword());
                request.setLoggedIn(user.isLoggedIn());
                request.setActive(user.isActive());
                request.setVerified(user.isVerified());
                request.setLastPasswordChange(user.getLastPasswordChange());

                //...map role
                Role role = user.getRole();
                if(role != null){
                    request.setRoleId(role.getId());
                    request.setRoleName(role.getName());
                }

                //...map branch
                Branch branch = user.getBranch();
                if(branch != null){
                    request.setBranchId(branch.getId());
                    request.setBranchSolId(branch.getSolId());
                    request.setBranchName(branch.getBranchName());
                }

                //...map apps
                List<App> apps = user.getApps();
                if(apps == null){
                    apps = new ArrayList<>();
                }

                if(!apps.isEmpty()){
                    List<String> appRecords = new ArrayList<>();
                    for(App app : apps){
                        appRecords.add(app.getName());
                    }
                    request.setApps(appRecords);
                }

            }
            return CompletableFuture.completedFuture(request);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    //User findByIdAndRole_Id(Long userId, Long roleId);
    @Override
    public CompletableFuture<UserRequest> findByIdWithRole(long userId) {
        logger.info(String.format("Retrieving user and their role with email address %s", userId));
        try{
            UserRequest request = null;
            User user = users.findByIdWithRole(userId);
            if(user != null){
                request = this.mapper.map(user, UserRequest.class);
                Role role = user.getRole();
                if(role != null){
                    request.setRoleId(role.getId());
                }
            }
            return CompletableFuture.completedFuture(request);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByIdWithRole' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<UserRequest> findByEmail(String email) {
        logger.info(String.format("Retrieving user with email address %s", email));
        try{
            UserRequest request = null;
            User user = users.findByEmail(email);
            if(user != null){
                request = this.mapper.map(user, UserRequest.class);
                Role role = user.getRole();
                if(role != null){
                    request.setRoleId(role.getId());
                }
            }
            return CompletableFuture.completedFuture(request);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByEmail' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<UserRequest>> findAll() {
        List<UserRequest> records  = new ArrayList<>();
        try{
            //..get user records
            List<User> userRecords = users.findAll();
            if(!userRecords.isEmpty()){
                for (User record : userRecords) {
                    records.add(this.mapper.map(record, UserRequest.class));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex) {
            logger.info("An error occurred in method 'findAll' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
   }

    @Override
    public CompletableFuture<List<UserRequest>> findActive() {
        List<UserRequest> records  = new ArrayList<>();
        try{
            //..get user records
            List<User> userRecords = users.findByisDeletedFalse();
            if(!userRecords.isEmpty()){
                for (User record : userRecords) {
                    records.add(this.mapper.map(record, UserRequest.class));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex) {
            logger.info("An error occurred in method 'findActive' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<UserRequest> create(UserRequest user) {
        logger.info("update user record");
        try{
            Role role = roles.findById(user.getRoleId());
            if(role == null){
                return CompletableFuture.completedFuture(null);
            }

            Optional<Branch> oBranch = branches.findById(user.getBranchId());
            Branch branch = null;
            if(oBranch.isPresent()){
                branch = oBranch.get();
            }

            if(branch == null){
                return CompletableFuture.completedFuture(null);
            }

            User record = this.mapper.map(user, User.class);
            record.setRole(role);
            record.setBranch(branch);
            users.save(record);

            user.setId(record.getId());
            return CompletableFuture.completedFuture(user);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(UserRequest user, long roleId, long branchId) {
        logger.info("update user record");
        try{
            User record = this.mapper.map(user, User.class);
            users.update(record, roleId, branchId);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    public void updatePassword(long id, String password){
        logger.info("update user password");
        try{
            users.updatePasswordById(password,id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'updatePasswordById' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    public void setActiveStatus(long id, boolean active, String modifiedBy, String modifiedOn){
        logger.info("update user password");
        try{
            users.updateIsActiveById(active, modifiedBy, modifiedOn, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateIsActiveById' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    @Override
    public void softDelete(long id, boolean isDeleted) {
        logger.info("Soft delete user record");
        try{
            logger.info(String.format("User property 'isDeleted' value is set to %s", isDeleted ? "true": "false"));
            users.markAsDeleted(id, isDeleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Complete delete user record");
        try{
            users.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'activate' in 'UserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
