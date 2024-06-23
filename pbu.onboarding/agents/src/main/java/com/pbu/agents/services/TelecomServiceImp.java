package com.pbu.agents.services;

import com.pbu.agents.models.IndividualAgent;
import com.pbu.agents.models.Kin;
import com.pbu.agents.models.Telecom;
import com.pbu.agents.repositories.KinRepository;
import com.pbu.agents.repositories.TelecomRepository;
import com.pbu.agents.requests.KinRequest;
import com.pbu.agents.requests.TelecomRequest;
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
public class TelecomServiceImp implements TelecomService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final TelecomRepository telecoms;

    public TelecomServiceImp(AppLoggerService logger, ModelMapper mapper, TelecomRepository telecoms) {
        this.logger = logger;
        this.mapper = mapper;
        this.telecoms = telecoms;
    }

    @Override
    public CompletableFuture<Boolean> telecomExists(long id) {
        logger.info("Check if telecom exists");
        try{
            return CompletableFuture.completedFuture(telecoms.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'telecomExists' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    @Override
    public CompletableFuture<Boolean> existsByName(String name) {
        logger.info("Check if telecom exists");
        try{
            return CompletableFuture.completedFuture(telecoms.existsByTelecomName(name));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByName' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String name, Long id) {
        logger.info(String.format("Retrieving telecom with name %s and agent Id %s", name, id));
        try{
            return CompletableFuture.completedFuture(telecoms.existsByTelecomNameAndIdNot(name, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNameAndNotId' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<TelecomRequest> findTelecomById(long id) {
        logger.info(String.format("Retrieving telecom with ID %s", id));
        try{

            Telecom record = telecoms.findById(id);
            if (record != null) {
                TelecomRequest request = this.mapper.map(record, TelecomRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findTelecomById' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<TelecomRequest> findTelecomByName(String name) {
        logger.info(String.format("Retrieving telecom with name %s", name));
        try{

            Telecom record = telecoms.findByTelecomName(name);
            if (record != null) {
                TelecomRequest request = this.mapper.map(record, TelecomRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findTelecomByName' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<TelecomRequest>> getAllTelecoms() {
        logger.info("Retrieve all telecoms");
        List<TelecomRequest> records  = new ArrayList<>();
        try{
            //..get telecom records
            List<Telecom>telecomRecords = telecoms.findAll();
            if(!telecomRecords.isEmpty()){
                for (Telecom record : telecomRecords) {
                    records.add(this.mapper.map(record, TelecomRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAllTelecoms' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<TelecomRequest>> getActiveTelecoms() {
        logger.info("Retrieve active telecoms");
        List<TelecomRequest> records  = new ArrayList<>();
        try{
            //..get telecom records
            List<Telecom>telecomRecords = telecoms.findActiveTelecoms();
            if(!telecomRecords.isEmpty()){
                for (Telecom record : telecomRecords) {
                    records.add(this.mapper.map(record, TelecomRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveTelecoms' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<TelecomRequest> create(TelecomRequest telecom) throws InterruptedException {
        logger.info("Adding new telecom");
        try{
            Telecom record = this.mapper.map(telecom, Telecom.class);
            telecoms.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(telecom);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(TelecomRequest telecom) {
        logger.info("update telecom");
        try{
            Telecom record = this.mapper.map(telecom, Telecom.class);
            record.setId(telecom.getId());
            telecoms.updateTelecom(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in TelecomService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete telecom");
        try{
            logger.info(String.format("Telecom is_deleted value set to %s", deleted ? "true": "false"));
            telecoms.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete telecom");
        try{
            logger.info(String.format("Delete telecom with id %s", id));
            telecoms.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'TelecomService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
