package com.pbu.attachments.controllers;

import com.pbu.attachments.requests.AttachmentRequest;
import com.pbu.attachments.requests.IdentificationRequest;
import com.pbu.attachments.requests.TypeRequest;
import com.pbu.attachments.services.AttachmentService;
import com.pbu.utils.common.AppLoggerService;
import com.pbu.utils.exceptions.*;
import com.pbu.utils.helpers.Generators;
import com.pbu.utils.exceptions.Error;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("attachments")
public class AttachmentController  {
    public final AppLoggerService logger;
    public final ApplicationExceptionHandler exceptionHandler;
    public final AttachmentService service;
    public  AttachmentController(
            AppLoggerService logger,
            ApplicationExceptionHandler exceptionHandler,
            AttachmentService service)  {
        this.logger = logger;
        this.exceptionHandler = exceptionHandler;
        this.service = service;
    }

    //region attachments
    @Async
    @GetMapping("/findAttachment/{id}")
    public CompletableFuture<ResponseEntity<?>> findAttachment(
            @PathVariable("id") long id,
            HttpServletRequest request){
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {

            CompletableFuture<AttachmentRequest> result = this.service.findAttachmentById(id);
            AttachmentRequest record = result.join();

            //attachment not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("Resource not found! Attachment with ID'%s' not found", id));
                ResponseEntity<Error> error =
                        exceptionHandler.resourceNotFoundExceptionHandler(
                                new NotFoundException("Attachment","ID", id),
                                request);
                future.complete(error);
                return future;
            }

            //return attachment record
            future.complete(new ResponseEntity<>(record, HttpStatus.OK));
            return future;

        } catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'findAttachment'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @GetMapping("/getAttachments/{ownerId}")
    public CompletableFuture<ResponseEntity<?>> getAttachments(
            @PathVariable Long ownerId,
            HttpServletRequest request) {
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
        logger.info(String.format("Retrieving a list of attachments for owner with Owner ID %s", ownerId));

        try{
            CompletableFuture<List<AttachmentRequest>> attachments = service.getAttachments(ownerId);
            List<AttachmentRequest> records = attachments.join();

            future.complete(ResponseEntity.ok(records));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'getAttachments'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @GetMapping("/checkAttachment/{id}")
    public CompletableFuture<ResponseEntity<?>> checkAttachment(
            @PathVariable("id") long id,
            HttpServletRequest request) {
        logger.info(String.format("Check if attachment with id. '%s' exists", id));
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {
            //...check if exists
            CompletableFuture<Boolean> record = this.service.attachmentExists(id);
            boolean result = record.join();

            //return attachment record
            future.complete(new ResponseEntity<>(result, HttpStatus.OK));
            return future;

        } catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'checkAttachment'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @PostMapping("/createAttachments")
    public CompletableFuture<ResponseEntity<?>> createAttachments(
            @RequestBody @Valid AttachmentRequest[] attachments,
            BindingResult bindingResult, HttpServletRequest request) {
        //..creation date
        logger.info("Creating file attachments");
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {
            //Validate request object
            logger.info("Validating attachment record :: ");
            if (bindingResult.hasErrors()) {
                logger.info("Attachment record Validation error....");
                ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
                logger.info(String.format("%s", validationException.getMessage()));
                future.complete(exceptionHandler.validationExceptionHandler(validationException, request));
                return future;
            }

            //...save type
            CompletableFuture<AttachmentRequest[]> records = this.service.createAttachments(attachments);
            AttachmentRequest[] result = records.join();

            //return attachment record
            future.complete(new ResponseEntity<>(result, HttpStatus.OK));
            return future;

        } catch (InterruptedException ex) {
            ResponseEntity<Error> error =
                    exceptionHandler.threadCanceledHandler(new CanceledException(), request);

            future.complete(error);
            //log error
            logger.info("Error creating attachment file(s)");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            return future;
        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'createAttachments'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @PutMapping("/updateAttachment")
    public CompletableFuture<ResponseEntity<?>> updateAttachment(
            @RequestBody @Valid AttachmentRequest attachment,
            BindingResult bindingResult,
            HttpServletRequest request){
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
        logger.info(String.format("Modifying ID Type with name %s",attachment.getAttDescr()));

        //Validate request object
        logger.info("Validating ID Type record :: ");
        if (bindingResult.hasErrors()) {
            logger.info("ID Type record Validation error....");
            ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
            logger.info(String.format("%s", validationException.getMessage()));
            future.complete(exceptionHandler.validationExceptionHandler(validationException, request));
            return future;
        }

        try{
            //check whether type with name is not in use
            logger.info("Checking whether attachment assigned name is not in use...");
            String name = attachment.getAttDescr();
            long agent_id = attachment.getAgentId();
            boolean exists = this.service.checkAttachmentDuplicateNameWithSameIds(name, agent_id);
            if(exists){
                logger.info(String.format("Resource Conflict! Another Attachment with name '%s' exists for owner with ID '%s'", name, agent_id));
                ResponseEntity<Error> error =
                        exceptionHandler.duplicatesResourceExceptionHandler(
                                new DuplicateException("Attachment","Owner", agent_id),
                                request);
                future.complete(error);
                return future;
            }

            //update attachment record in database
            this.service.updateAttachment(attachment);

            //return attachment record
            future.complete(new ResponseEntity<>(attachment, HttpStatus.OK));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'updateType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @DeleteMapping("/softDeleteAttachment/{id}/{isDeleted}")
    public CompletableFuture<ResponseEntity<?>> softDeleteAttachment(
            @PathVariable Long id,
            @PathVariable boolean isDeleted,
            HttpServletRequest request) {
        logger.info(String.format("Deleting attachment record. Attachment with id %s is_deleted set to %s",id, isDeleted ?"True":"False"));

        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.attachmentExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            ResponseEntity<Error> error =
                    exceptionHandler.resourceNotFoundExceptionHandler(
                            new NotFoundException("Attachment", "id",id),
                            request);
            future.complete(error);
            return future;
        }

        //delete Attachment
        service.softDeleteAttachment(id, isDeleted);

        //return result
        future.complete(new ResponseEntity<>("Attachment updated successfully", HttpStatus.OK));
        return future;
    }

    @Async
    @DeleteMapping("/deleteAttachment/{id}")
    public CompletableFuture<ResponseEntity<?>> deleteAttachment(
            @PathVariable Long id,
            HttpServletRequest request) {
        logger.info(String.format("Deleting attachment record. Attachment id %s is being deleted",id));

        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.attachmentExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            ResponseEntity<Error> error =
                    exceptionHandler.resourceNotFoundExceptionHandler(
                            new NotFoundException("Attachment", "id",id),
                            request);
            future.complete(error);
            return future;
        }

        //delete attachment
        service.deleteAttachment(id);

        //return result
        future.complete(new ResponseEntity<>("Attachment deleted successfully", HttpStatus.OK));
        return future;
    }

    //endregion

    //region ID Types
    @Async
    @GetMapping("/ findType/{id}")
    public CompletableFuture<ResponseEntity<?>> findType(
            @PathVariable("id") long id,
            HttpServletRequest request){
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {

            CompletableFuture<TypeRequest> result = this.service.findTypeById(id);
            TypeRequest record = result.join();

            //...identification type not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("Resource not found! Identification type with ID'%s' not found", id));
                ResponseEntity<Error> error =
                        exceptionHandler.resourceNotFoundExceptionHandler(
                                new NotFoundException("Identification Type","ID", id),
                                request);
                future.complete(error);
                return future;
            }

            //return identification record
            future.complete(new ResponseEntity<>(record, HttpStatus.OK));
            return future;

        } catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'findType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @GetMapping("/getTypes")
    public CompletableFuture<ResponseEntity<?>> getTypes(HttpServletRequest request) {
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
        logger.info("Retrieving a list of all identifications types");

        try{
            CompletableFuture<List<TypeRequest>> types = service.getTypes();
            List<TypeRequest> records = types.join();
            future.complete(ResponseEntity.ok(records));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'getTypes'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @GetMapping("/checkType/{id}")
    public CompletableFuture<ResponseEntity<?>> checkType(
            @PathVariable("id") long id,
            HttpServletRequest request) {
        logger.info(String.format("Check if ID type with ID '%s' exists", id));
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {
            //...save type
            CompletableFuture<Boolean> record = this.service.idTypeExists(id);
            boolean result = record.join();

            //return ID types record
            future.complete(new ResponseEntity<>(result, HttpStatus.OK));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'checkType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @PostMapping("/createtype")
    public CompletableFuture<ResponseEntity<?>> createIdType(
            @RequestBody @Valid TypeRequest idType,
            BindingResult bindingResult,
            HttpServletRequest request) {

        //..creation date
        logger.info(String.format("Creating new ID Type %s", idType.getTypeName()));
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {
            //Validate request object
            logger.info("Validating ID Type record :: ");
            if (bindingResult.hasErrors()) {
                logger.info("ID Type record Validation error....");
                ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
                logger.info(String.format("%s", validationException.getMessage()));
                future.complete(exceptionHandler.validationExceptionHandler(validationException, request));
                return future;
            }

            //check for duplicate type names
            logger.info("Checking whether ID type assigned name is not in use...");
            String typeName = idType.getTypeName();
            boolean exists = this.service.checkTypeDuplicateName(typeName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Type with name '%s' exists", typeName));
                ResponseEntity<Error> error =
                        exceptionHandler.duplicatesResourceExceptionHandler(
                                new DuplicateException("ID Type","Name", typeName),
                                request);
                future.complete(error);
                return future;
            }

            //...save type
            CompletableFuture<TypeRequest> record = this.service.createIdType(idType);
            TypeRequest result = record.join();

            //return ID types record
            future.complete(new ResponseEntity<>(result, HttpStatus.OK));
            return future;

        } catch (InterruptedException ex) {
            ResponseEntity<Error> error =
                    exceptionHandler.threadCanceledHandler(new CanceledException(), request);

            future.complete(error);
            //log error
            logger.info("Error creating ID Type");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            return future;
        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'createIdType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @PutMapping("/updateType/")
    public CompletableFuture<ResponseEntity<?>> updateType(
            @RequestBody @Valid TypeRequest idType,
            BindingResult bindingResult,
            HttpServletRequest request){
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
        logger.info(String.format("Modifying ID Type with name %s",idType.getTypeName()));

        //Validate request object
        logger.info("Validating ID Type record :: ");
        if (bindingResult.hasErrors()) {
            logger.info("ID Type record Validation error....");
            ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
            logger.info(String.format("%s", validationException.getMessage()));
            future.complete(exceptionHandler.validationExceptionHandler(validationException, request));
            return future;
        }

        try{
            //check whether type with name is not in use
            logger.info("Checking whether ID type assigned name is not in use...");
            String typeName = idType.getTypeName();
            boolean exists = this.service.checkTypeDuplicateNameWithDifferentIds(typeName, idType.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Type with name '%s' exists", typeName));
                ResponseEntity<Error> error =
                        exceptionHandler.duplicatesResourceExceptionHandler(
                                new DuplicateException("ID Type","Name", typeName),
                                request);
                future.complete(error);
                return future;
            }

            //update ID Type record in database
            this.service.updateIdType(idType);

            //return Type record
            future.complete(new ResponseEntity<>(idType, HttpStatus.OK));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'updateType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @DeleteMapping("/softDeleteType/{id}/{isDeleted}")
    public CompletableFuture<ResponseEntity<?>> softDeleteType(
            @PathVariable Long id,
            @PathVariable boolean isDeleted,
            HttpServletRequest request) {
        logger.info(String.format("Deleting ID Type record. ID Type with id %s is_deleted set to %s",id, isDeleted ?"True":"False"));

        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        //..check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.idTypeExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            ResponseEntity<Error> error =
                    exceptionHandler.resourceNotFoundExceptionHandler(
                            new NotFoundException("ID Type", "id",id),
                            request);
            future.complete(error);
            return future;
        }

        //delete ID Type
        service.softDeleteIdType(id, isDeleted);

        //return result
        future.complete(new ResponseEntity<>("ID Type updated successfully", HttpStatus.OK));
        return future;
    }

    @Async
    @DeleteMapping("/deleteType/{id}")
    public CompletableFuture<ResponseEntity<?>> deleteType(
            @PathVariable Long id,
            HttpServletRequest request) {
        logger.info(String.format("Deleting ID Type record. ID with id %s is being deleted",id));

        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.idTypeExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            ResponseEntity<Error> error =
                    exceptionHandler.resourceNotFoundExceptionHandler(
                            new NotFoundException("ID Type", "id",id),
                            request);
            future.complete(error);
            return future;
        }

        //delete ID type
        service.deleteIdType(id);

        //return result
        future.complete(new ResponseEntity<>("ID Type deleted successfully", HttpStatus.OK));
        return future;
    }

    //endregion

    //region Identifications
    @Async
    @GetMapping("/ findIdentification/{id}")
    public CompletableFuture<ResponseEntity<?>> findIdentification(
            @PathVariable("id") long id,
            HttpServletRequest request){
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {

            CompletableFuture<IdentificationRequest> result = this.service.findIdentificationById(id);
            IdentificationRequest record = result.join();

            //...identification not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("Resource not found! Identification with ID'%s' not found", id));
                ResponseEntity<Error> error =
                        exceptionHandler.resourceNotFoundExceptionHandler(
                                new NotFoundException("Identification","ID", id),
                                request);
                future.complete(error);
                return future;
            }

            //return identification record
            future.complete(new ResponseEntity<>(record, HttpStatus.OK));
            return future;

        } catch(Exception ex){
            //...exception object
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()),
                            request);
            future.complete(error);
            logger.info("An error occurred in method 'findIdentification'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @GetMapping("/getIdentifications/{ownerId}")
    public CompletableFuture<ResponseEntity<?>> getIdentifications(
            @PathVariable Long ownerId,
            HttpServletRequest request) {
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
        logger.info(String.format("Retrieving a list of identifications for owner with Owner ID %s", ownerId));

        try{
            CompletableFuture<List<IdentificationRequest>> identifications = service.getIdentifications(ownerId);
            List<IdentificationRequest> records = identifications.join();
            future.complete(ResponseEntity.ok(records));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'getIdentifications'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @GetMapping("/checkIdentification/{idNumber}")
    public CompletableFuture<ResponseEntity<?>> checkIdentification(@PathVariable("idNumber") String idNumber,
                                                                    HttpServletRequest request) {
        logger.info(String.format("Check if ID document with ID No. '%s' exists", idNumber));
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {
            //...check if exists
            CompletableFuture<Boolean> record = this.service.identificationExists(idNumber);
            boolean result = record.join();

            //return ID document record
            future.complete(new ResponseEntity<>(result, HttpStatus.OK));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'checkIdentification'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @PostMapping("/createid")
    public CompletableFuture<ResponseEntity<?>> createId(
            @RequestBody @Valid IdentificationRequest identification,
            BindingResult bindingResult, HttpServletRequest request) {
        //..creation date
        logger.info(String.format("Creating new ID Document %s", identification.getIdNo()));
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        try {
            //Validate request object
            logger.info("Validating ID document record :: ");
            if (bindingResult.hasErrors()) {
                logger.info("ID document record Validation error....");
                ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
                logger.info(String.format("%s", validationException.getMessage()));
                future.complete(exceptionHandler.validationExceptionHandler(validationException, request));
                return future;
            }

            //...check for duplicate ID No
            logger.info("Checking whether ID type assigned name is not in use...");
            String docNumber = identification.getIdNo();
            CompletableFuture<Boolean> recordExists = this.service.identificationExists(docNumber);
            boolean exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Document with ID NO. '%s' exists", docNumber));
                ResponseEntity<Error> error =
                        exceptionHandler.duplicatesResourceExceptionHandler(
                                new DuplicateException("Identification","ID No", docNumber),
                                request);
                future.complete(error);
                return future;
            }

            //...save ID document
            CompletableFuture<IdentificationRequest> record = this.service.createIdentification(identification);
            IdentificationRequest result = record.join();

            //return ID document record
            future.complete(new ResponseEntity<>(result, HttpStatus.OK));
            return future;

        } catch (InterruptedException ex) {
            ResponseEntity<Error> error =
                    exceptionHandler.threadCanceledHandler(new CanceledException(), request);

            future.complete(error);
            //log error
            logger.info("Error creating ID document");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            return future;
        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'createId'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }

    @Async
    @PutMapping("/updateIdentification/")
    public CompletableFuture<ResponseEntity<?>> updateId(
            @RequestBody @Valid IdentificationRequest identification,
            BindingResult bindingResult,
            HttpServletRequest request){
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
        logger.info(String.format("Modifying Identification Document with no. %s",identification.getIdNo()));

        //Validate request object
        logger.info("Validating ID Document record :: ");
        if (bindingResult.hasErrors()) {
            logger.info("ID Type record Validation error....");
            ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
            logger.info(String.format("%s", validationException.getMessage()));
            future.complete(exceptionHandler.validationExceptionHandler(validationException, request));
            return future;
        }

        try{
            //check whether ID Document with ID No. is not in use
            logger.info("Checking whether ID type assigned name is not in use...");
            String docNumber = identification.getIdNo();
            boolean exists = this.service.checkIdNumberDuplicateWithDifferentIds(docNumber, identification.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Document with ID NO. '%s' exists", docNumber));
                ResponseEntity<Error> error =
                        exceptionHandler.duplicatesResourceExceptionHandler(
                                new DuplicateException("Identification","ID No", docNumber),
                                request);
                future.complete(error);
                return future;
            }

            //update ID Document record in database
            this.service.updateIdentification(identification);

            //return Document record
            future.complete(new ResponseEntity<>(identification, HttpStatus.OK));
            return future;

        }catch(Exception ex){
            ResponseEntity<Error> error =
                    exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
            future.complete(error);
            logger.info("An error occurred in method 'updateId'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return future;
        }
    }


    @Async
    @DeleteMapping("/softDeleteIdentification/{id}/{isDeleted}")
    public CompletableFuture<ResponseEntity<?>> softDeleteIdentification(
            @PathVariable Long id,
            @PathVariable boolean isDeleted,
            HttpServletRequest request) {
        logger.info(String.format("Deleting Identification record. Identification with id %s is_deleted set to %s",id, isDeleted ?"True":"False"));

        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.identificationExistsById(id);
        Boolean exists = existResult.join();
        if (!exists) {
            ResponseEntity<Error> error =
                    exceptionHandler.resourceNotFoundExceptionHandler(
                            new NotFoundException("Identification", "id",id),
                            request);
            future.complete(error);
            return future;
        }

        //delete Identification
        service.softDeleteIdentification(id, isDeleted);

        //return result
        future.complete(new ResponseEntity<>("Identification updated successfully", HttpStatus.OK));
        return future;
    }

    @Async
    @DeleteMapping("/deleteIdentification/{id}")
    public CompletableFuture<ResponseEntity<?>> deleteIdentification(
            @PathVariable Long id,
            HttpServletRequest request) {
        logger.info(String.format("Deleting identification record. Identification id %s is being deleted",id));

        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.identificationExistsById(id);
        Boolean exists = existResult.join();
        if (!exists) {
            ResponseEntity<Error> error =
                    exceptionHandler.resourceNotFoundExceptionHandler(
                            new NotFoundException("Identification", "id", id),
                            request);
            future.complete(error);
            return future;
        }

        //delete Identification
        service.deleteIdentification(id);

        //return result
        future.complete(new ResponseEntity<>("Identification deleted successfully", HttpStatus.OK));
        return future;
    }

    //endregion

    //region test requests
    @GetMapping("/hello")
    public String SayHello(){
        return "Hello from ATTACHMENTS";
    }
    //endregion

}
