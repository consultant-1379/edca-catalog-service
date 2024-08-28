/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.edca.catalog.common;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

/**
 * This class is a global exception handler which can handle exceptions common across the application layers
 *
 */

@RestControllerAdvice
public class CatalogExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger exceptionLogger = LoggerFactory.getLogger(CatalogExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        exceptionLogger.error("No Resource found {}", request.getContextPath());
        final CatalogResponse exceptionResponse = new CatalogResponse(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        exceptionLogger.error("Requested Method could not be Handled {}", request.getContextPath());
        final CatalogResponse exceptionResponse = new CatalogResponse(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        exceptionLogger.error("Requested Uri could not be processed {}", request.getContextPath());
        final CatalogResponse exceptionResponse = new CatalogResponse(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        exceptionLogger.error("Bad Request on Request body ", ex.getMostSpecificCause());
        CatalogResponse exceptionResponse;
        if (ex.getCause() instanceof UnrecognizedPropertyException) {
            final String responseMsg = ((UnrecognizedPropertyException) ex.getRootCause()).getPropertyName() + " is not a valid field ";
            exceptionResponse = new CatalogResponse(LocalDateTime.now(), responseMsg);
        } else {
            exceptionResponse = new CatalogResponse(LocalDateTime.now(), CatalogConstants.BAD_REQUEST_HTTP_MESSAGE);
        }
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(final EntityNotFoundException ex) {
        exceptionLogger.error("Requested Resource not found {}", ex.getMessage());
        final CatalogResponse exceptionResponse = new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_NOT_FOUND_MSG);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleResourceExistsException(final DataIntegrityViolationException ex) {
        exceptionLogger.error("Requested Resource exists already {}", ex.getMessage());
        final CatalogResponse exceptionResponse = new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_CONFLICT_MSG);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleResourceIdNotIntegerException(final NumberFormatException ex) {
        exceptionLogger.error("Requested Resource cannot be queried by provided id {}", ex.getMessage());
        final CatalogResponse exceptionResponse = new CatalogResponse(LocalDateTime.now(), CatalogConstants.BAD_REQUEST_HTTP_MESSAGE);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleCommonExceptions(final Exception ex, final WebRequest request) {
        exceptionLogger.error("Internal Server Error : {}", ex.getMessage());
        if (ex.getMessage() != null) {
            if (ex.getMessage().equals(CatalogConstants.DATAPROVIDERTYPE_DATASPACE_NOT_FOUND_MSG)) {
                // custom handling of application exception i.e CatalogException when dataspace not found for dataProviderType
                exceptionLogger.error("Catalog Service :: handleCommonExceptions Dataspace not found for dataProviderType");
                final CatalogResponse dataspaceNotFoundForDataProviderTypeResponse = new CatalogResponse(LocalDateTime.now(), CatalogConstants.DATAPROVIDERTYPE_DATASPACE_NOT_FOUND_MSG);
                return ResponseEntity.badRequest().body(dataspaceNotFoundForDataProviderTypeResponse);
            } else if (ex.getMessage().equals(CatalogConstants.MESSAGESTATUSTOPIC_MESSAGEBUS_NOT_FOUND_MSG)) {
                exceptionLogger.error("Catalog Service :: handleCommonExceptions messageBus not found for messageStatusTopic");
                final CatalogResponse messageBusNotFoundForMessageStatusTopic = new CatalogResponse(LocalDateTime.now(), CatalogConstants.MESSAGESTATUSTOPIC_MESSAGEBUS_NOT_FOUND_MSG);
                return ResponseEntity.badRequest().body(messageBusNotFoundForMessageStatusTopic);
            } else if (ex.getMessage().equals(CatalogConstants.MESSAGEDATATOPIC_MESSAGEBUS_NOT_FOUND_MSG)
                    || ex.getMessage().equals(CatalogConstants.MESSAGEDATATOPIC_MESSAGESTATUSTOPIC_NOT_FOUND_MSG)) {
                exceptionLogger.error("Catalog Service :: handleCommonExceptions required dependants not found for messageDataTopic");
                final CatalogResponse notFoundErrorForMessageDataTopic = new CatalogResponse(LocalDateTime.now(), ex.getMessage());
                return ResponseEntity.badRequest().body(notFoundErrorForMessageDataTopic);
            } else if (ex.getMessage().contains(CatalogConstants.MESSAGESCHEMA_DATACOLLECTOR_NOT_FOUND_MSG) || ex.getMessage().contains(CatalogConstants.MESSAGESCHEMA_DATAPROVIDERTYPE_NOT_FOUND_MSG)
                    || ex.getMessage().contains(CatalogConstants.MESSAGESCHEMA_MESSAGEDATATOPIC_NOT_FOUND)) {
                exceptionLogger.error("Catalog Service :: handleCommonExceptions required dependants not found for messageSchema");
                final CatalogResponse notFoundErrorForMessageSchema = new CatalogResponse(LocalDateTime.now(), ex.getMessage());
                return ResponseEntity.badRequest().body(notFoundErrorForMessageSchema);
            } else if (ex.getMessage().contains(CatalogConstants.FILEFORMAT_BULK_DATA_REPO_NOT_FOUND_MSG) || ex.getMessage().contains(CatalogConstants.FILEFORMAT_DATACOLLECTOR_NOT_FOUND_MSG)
                    || ex.getMessage().contains(CatalogConstants.FILEFORMAT_DATAPROVIDERTYPE_NOT_FOUND_MSG) || ex.getMessage().contains(CatalogConstants.FILEFORMAT_NOTIFICATION_TOPIC_NOT_FOUND_MSG)) {
                // custom handling of application exception i.e CatalogException when associated entities unavailable for registering fileFormat
                exceptionLogger.error("Catalog Service :: handleCommonExceptions Required associated entities unavailable for registering fileFormat");
                final CatalogResponse registerFileFormatErrorResponse = new CatalogResponse(LocalDateTime.now(), ex.getMessage());
                return ResponseEntity.badRequest().body(registerFileFormatErrorResponse);
            }
        }
        exceptionLogger.error("Catalog Service :: handleCommonExceptions Application error");
        final CatalogResponse exceptionResponse = new CatalogResponse(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
