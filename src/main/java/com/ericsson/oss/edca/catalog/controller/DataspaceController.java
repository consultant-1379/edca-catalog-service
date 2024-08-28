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

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogResponse;
import com.ericsson.oss.edca.catalog.controller.dto.DataSpaceDto;
import com.ericsson.oss.edca.catalog.service.DataspaceService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1/data-space")
@Api(value = "Dataspace", tags = "/v1/data-space")
public class DataspaceController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DataspaceController.class);

    @Autowired
    private DataspaceService dataspaceService;

    /**
     * This method is to register/save Dataspace details in the postgresSQL database
     *
     * @param dataSpaceDto
     *            - DataSpaceDto
     * @param result
     *            -
     * @param request
     * @return Dataspace and associated dataProviderType details
     */
    @ApiOperation(value = "create dataspace details", response = DataSpaceDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = DataSpaceDto.class, message = "created dataspace details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "dataspace details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering dataspace details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerDataspace(@ApiParam(name = "dataspace model", value = "dataspace", required = true) @RequestBody @Valid final DataSpaceDto dataSpaceDto,
            final BindingResult bindingResult, final HttpServletRequest httpServletRequest) throws Exception {
        CatalogResponse registerDataspaceErrorResponse = null;
        if (!bindingResult.hasErrors()) {
            // call service layer method to get the id
            final DataSpaceDto registeredDataspace = dataspaceService.saveDataspace(dataSpaceDto);
            logger.info("Catalog Service :: registerDataspace Registered dataspace : {} ", registeredDataspace);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredDataspace);
        }
        logger.error("Catalog Service :: registerDataspace Required parameter(s) for registering dataspace may not be available");
        registerDataspaceErrorResponse = new CatalogResponse(LocalDateTime.now(), CatalogConstants.BAD_REQUEST_HTTP_MESSAGE + " - Missing field(s)" + bindingResult.getFieldError().getField());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerDataspaceErrorResponse);
    }

    /**
     * Method to query Dataspace details from the postgresSQL database for all dataspaces OR dataspace optionally filtered by quyer paramter name
     *
     * @param queryParams
     *            - query parameters for fetching dataspaces
     * @return Dataspace and associated DataProviderType details
     */
    @ApiOperation(value = "get all dataspace details or filter by QueryParams", response = DataSpaceDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = DataSpaceDto.class, message = "get dataspace details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "dataspace details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "query", value = "name of a dataspace", name = "name", dataTypeClass = String.class, required = false)
    @Timed(description = "Time spent querying dataspace details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> queryDataspaceByParams(@ApiIgnore @RequestParam(required = false) final Map<String, String> queryParams) throws Exception {

        DataSpaceDto queriedDataspaceDtoByName = null;
        // invalid query parameter provided - if control comes here
        logger.info("Catalog Service :: queryDataspaceByParams validateQueryParam() : {}", isValidQueryParam(queryParams));
        if (!isValidQueryParam(queryParams)) {
            final CatalogResponse invalidDataspaceQueryParamResponseMessage = new CatalogResponse(LocalDateTime.now(), CatalogConstants.DATASPACE_QPARAMS_ERROR_MSG);
            return ResponseEntity.badRequest().body(invalidDataspaceQueryParamResponseMessage);
        }
        if (queryParams.containsKey("name")) {
            final String queryDataspaceName = queryParams.get("name");
            queriedDataspaceDtoByName = dataspaceService.getDataSpaceByName(queryDataspaceName);
            if (queriedDataspaceDtoByName == null) {
                throw new EntityNotFoundException();
            }
            return ResponseEntity.status(HttpStatus.OK).body(queriedDataspaceDtoByName);
        }
        return queryAllDataspaces();
    }

    /**
     * Method to query Dataspace details from the postgresSQL database for dataspace filtered by dataspace id
     *
     * @param id
     * @return Dataspace and associated DataProviderType details
     * @throws Exception
     */
    @ApiOperation(value = "get dataspace details by id", response = DataSpaceDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = DataSpaceDto.class, message = "get dataspace details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "dataspace details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies dataspace", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying dataspace detail by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> queryDataspaceById(@PathVariable(required = false, name = "id") final String id) throws Exception {
        DataSpaceDto queriedDataspaceDtoById = null;
        queriedDataspaceDtoById = dataspaceService.getDataSpaceById(id);
        if (queriedDataspaceDtoById == null) {
            throw new EntityNotFoundException();
        }
        return ResponseEntity.ok(queriedDataspaceDtoById);

    }

    private ResponseEntity<Object> queryAllDataspaces() throws Exception {
        final List<DataSpaceDto> queriedDataSpaceDtos = dataspaceService.getAllDataSpaces();
        return ResponseEntity.ok(queriedDataSpaceDtos);
    }

    private boolean isValidQueryParam(final Map<String, String> queryParams) {
        logger.info("Catalog Service :: DataSpace isValidQueryParam queryParams {} ", queryParams);
        final Set<String> validQueryParams = Set.of("name");
        if (!queryParams.isEmpty()) {
            // if invalid query params provided with valid , then consider as BAD request ( Business constraint )
            return validQueryParams.containsAll(queryParams.keySet());
        }
        return true;
    }

}
