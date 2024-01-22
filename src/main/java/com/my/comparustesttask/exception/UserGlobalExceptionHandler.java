package com.my.comparustesttask.exception;

import com.my.comparustesttask.dto.ErrorResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class UserGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleInternalServerException(Exception ex, WebRequest request) {
        String errorMessage = "Internal Server Exception Occurred. Original Msg: " + ex.getMessage();
        log.error(errorMessage, ex);
        ErrorResponseData errorResponseData = new ErrorResponseData();
        errorResponseData.setErrorDescription(errorMessage);
        errorResponseData.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return handleExceptionInternal(ex, errorResponseData,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundException(RuntimeException ex, WebRequest request) {
        String errorMessage = "Not Found. Original Msg: " + ex.getMessage();
        log.error(errorMessage, ex);
        ErrorResponseData errorResponseData = new ErrorResponseData();
        errorResponseData.setErrorDescription(errorMessage);
        errorResponseData.setErrorCode(HttpStatus.NOT_FOUND.getReasonPhrase());
        return handleExceptionInternal(ex, errorResponseData,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
