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
package com.ericsson.oss.edca.catalog.controller;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogResponse;
import com.ericsson.oss.edca.catalog.controller.dto.DataCollectorDto;
import com.ericsson.oss.edca.catalog.service.DataCollectorService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/data-collector")
@Api(value = "DataCollector", tags = "/v1/data-collector")
public class DataCollectorController {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorController.class);

    @Autowired
    private DataCollectorService dataCollectorService;

    /**
     * This method is to register/save DataCollector details in the postgresSQL database
     *
     * @param dataCollectorDto
     *            - DataCollectorDto
     * @param bindingResult
     *            - BindingResult
     * @return DataCollector details
     *
     */
    @ApiOperation(value = "create DataCollector details", response = DataCollectorDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = DataCollectorDto.class, message = "created DataCollector details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "DataCollector details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering data-collector details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerDataCollector(@RequestBody @Valid final DataCollectorDto dataCollectorDto, final BindingResult bindingResult) {
        CatalogResponse registerDataCollectorErrorResponse = null;
        logger.info("Catalog Service :: registerDataCollector Received request body for DataCollector  : {}", dataCollectorDto);
        if (bindingResult.hasErrors()) {
            logger.error("Catalog Service :: registerDataCollector Required parameter(s) for registering dataCollector may not be available");
            // handle request body incorrect - bad request ( 400)
            registerDataCollectorErrorResponse = new CatalogResponse(LocalDateTime.now(),
                    CatalogConstants.BAD_REQUEST_HTTP_MESSAGE + " - Missing field(s)" + bindingResult.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toSet()));
            return ResponseEntity.badRequest().body(registerDataCollectorErrorResponse);
        }

        final DataCollectorDto registeredDataCollectorDto = dataCollectorService.saveDataCollector(dataCollectorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredDataCollectorDto);
    }

}
