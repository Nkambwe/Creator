package com.pbu.sam.services;

import com.pbu.sam.models.ArchiveLog;
import com.pbu.sam.repositories.ArchiveLogRepository;
import com.pbu.sam.requests.ArchiveLogRequest;
import com.pbu.utils.common.AppLoggerService;
import com.pbu.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ArchivedLogServiceImp implements ArchivedLogService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final ArchiveLogRepository archives;

    public ArchivedLogServiceImp(AppLoggerService logger, ModelMapper mapper, ArchiveLogRepository archives) {
        this.mapper = mapper;
        this.archives = archives;
        this.logger = logger;
    }

    @Override
    public CompletableFuture<List<ArchiveLogRequest>> findAll() {
        logger.info("Retrieving all affiliations");
        List<ArchiveLogRequest> records  = new ArrayList<>();
        try{
            List<ArchiveLog> aRecords = archives.findAll();
            if(!aRecords.isEmpty()){
                for (ArchiveLog record : aRecords) {
                    records.add(this.mapper.map(record, ArchiveLogRequest.class));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAll' in 'ArchivedLogService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<ArchiveLogRequest>> findAll(LocalDateTime logDate) {
        logger.info("Retrieving all affiliations for a specific date");
        List<ArchiveLogRequest> records  = new ArrayList<>();
        try{
            List<ArchiveLog> aRecords = archives.findAllByArchivedOn(logDate);
            if(!aRecords.isEmpty()){
                for (ArchiveLog record : aRecords) {
                    records.add(this.mapper.map(record, ArchiveLogRequest.class));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAll' in 'ArchivedLogService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ArchiveLogRequest> create(ArchiveLogRequest log) throws InterruptedException {
        logger.info("Create new affiliation organization");
        try{
            //map record
            ArchiveLog record = this.mapper.map(log, ArchiveLog.class);

            //save record
            archives.save(record);

            //set identification type id
            log.setId(record.getId());
            return CompletableFuture.completedFuture(log);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'ArchivedLogService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete archive record");
        try{
            logger.info(String.format("Archive with id %s is being deleted", id));
            archives.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'ArchivedLogService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
