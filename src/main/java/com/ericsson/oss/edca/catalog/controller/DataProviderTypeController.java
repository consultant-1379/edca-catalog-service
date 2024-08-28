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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.common.CatalogResponse;
import com.ericsson.oss.edca.catalog.controller.dto.DataProviderTypeDto;
import com.ericsson.oss.edca.catalog.service.DataProviderTypeService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/v1/data-provider-type")
@Api(value = "DataProviderType", tags = "/v1/data-provider-type")
public class DataProviderTypeController {

    private static final Logger logger = LoggerFactory.getLogger(DataProviderTypeController.class);

    @Autowired
    private DataProviderTypeService dataProviderTypeService;

    /**
     * This method is to register/save DataProviderType details in the postgresSQL database
     *
     * @param dataProviderTypeDto
     *            - DataProviderTypeDto
     * @param bindingResult
     *            - BindingResult
     * @return DataProviderType and associated dataSpace details
     * @throws CatalogException
     */
    @ApiOperation(value = "create DataProviderType details", response = DataProviderTypeDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = DataProviderTypeDto.class, message = "created DataProviderType details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "DataProviderType details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering data-provider-type details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerDataProviderType(@RequestBody @Valid final DataProviderTypeDto dataProviderTypeDto, final BindingResult bindingResult) throws CatalogException {
        CatalogResponse registerDataProviderTypeErrorResponse = null;
        logger.info("Catalog Service :: registerDataProviderType Received request body for DataProviderType  : {}", dataProviderTypeDto);
        if (bindingResult.hasErrors()) {
            logger.error("Catalog Service :: registerDataProviderType Required parameter(s) for registering dataProviderType may not be available");
            // handle request body incorrect - bad request ( 400)
            registerDataProviderTypeErrorResponse = new CatalogResponse(LocalDateTime.now(),
                    CatalogConstants.BAD_REQUEST_HTTP_MESSAGE + " - Missing field(s)" + bindingResult.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toSet()));
            return ResponseEntity.badRequest().body(registerDataProviderTypeErrorResponse);
        }

        final DataProviderTypeDto registeredDataProviderTypeDto = dataProviderTypeService.saveDataProviderType(dataProviderTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredDataProviderTypeDto);
    }

    /**
     * Method to query DataProviderType details from the postgresSQL database for all dataProviderType OR dataProviderType optionally filtered by query parameter dataspace
     *
     * @param queryParams
     *            - query parameters for fetching dataProviderTypes
     * @return DataProviderType and associated dataSpace details
     */
    @ApiOperation(value = "get all dataProviderType details or filter by QueryParams", response = DataProviderTypeDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = DataProviderTypeDto.class, message = "get dataProviderType details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "dataProviderType details not found", response = CatalogResponse.class) })
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "dataspace", value = "Dataspace to which the dataProviderType is associated", dataTypeClass = String.class, required = false) })
    @Timed(description = "Time spent querying data-provider-type details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> queryAllDataProviderTypesByQueryParams(@ApiIgnore @RequestParam(required = false) final Map<String, String> queryParams) {
        List<DataProviderTypeDto> queriedDataProviderTypeDtos = null;
        // invalid query parameter provided - if control comes here
        logger.info("Catalog Service :: queryAllDataProviderTypes validateQueryParam() : {}", isValidQueryParam(queryParams));
        if (!isValidQueryParam(queryParams)) {
            final CatalogResponse invalidDataProviderTypeQueryParamResponseMessage = new CatalogResponse(LocalDateTime.now(), CatalogConstants.DATAPROVIDERTYPE_QPARAMS_ERROR_MSG);
            return ResponseEntity.badRequest().body(invalidDataProviderTypeQueryParamResponseMessage);
        }
        if (queryParams.containsKey("dataSpace")) {
            final String dataspaceQueryName = queryParams.get("dataSpace");
            queriedDataProviderTypeDtos = dataProviderTypeService.getAllDataProviderTypesByDataspace(dataspaceQueryName);
            if (queriedDataProviderTypeDtos == null || queriedDataProviderTypeDtos.isEmpty()) {
                throw new EntityNotFoundException();
            }
            return ResponseEntity.status(HttpStatus.OK).body(queriedDataProviderTypeDtos);
        }
        return queryAllDataProviderTypes();
    }

    /**
     * Method to query DataProviderType details from the postgresSQL database for dataProviderType filtered by dataProviderType id
     *
     * @param dataProviderTypeId
     * @return DataProviderType and associated dataSpace details
     * @throws Exception
     */
    @ApiOperation(value = "get dataProviderType details by id", response = DataProviderTypeDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = DataProviderTypeDto.class, message = "get dataProviderType details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "dataProviderType details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies dataProviderType", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying data-provider-type detail by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> queryDataProviderTypeById(@PathVariable(value = "id") final String dataProviderTypeId) {
        DataProviderTypeDto queriedDataProviderTypeDtoById = null;
        queriedDataProviderTypeDtoById = dataProviderTypeService.getDataProviderTypeById(dataProviderTypeId);
        if (queriedDataProviderTypeDtoById == null) {
            throw new EntityNotFoundException();
        }
        return ResponseEntity.ok(queriedDataProviderTypeDtoById);
    }

    private ResponseEntity<Object> queryAllDataProviderTypes() {
        final List<DataProviderTypeDto> queriedDataProviderTypeDtos = dataProviderTypeService.getAllDataProviderTypes();
        return ResponseEntity.ok(queriedDataProviderTypeDtos);
    }

    private boolean isValidQueryParam(final Map<String, String> queryParams) {
        logger.info("Catalog Service :: DataProviderType isValidQueryParam queryParams {} ", queryParams);
        final Set<String> validQueryParams = Set.of("dataSpace");
        if (!queryParams.isEmpty()) {
            // if invalid query params provided with valid , then consider as BAD request ( Business constraint )
            return validQueryParams.containsAll(queryParams.keySet());
        }
        return true;
    }

}
