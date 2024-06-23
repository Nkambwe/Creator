package com.pbu.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class Name: ServerException
 * Extends : System.RuntimeException class
 * Description: Class handles errors related to internal service operations
 * Created By: Nkambwe mark
 */

public class ServerException extends RuntimeException {
    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    public static final long serialVersionId = 6L;
    public ServerException(String message){
        super(message);
    }
}
