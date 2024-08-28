/*******************************************************************************
* COPYRIGHT Ericsson 2021
*
* The copyright to the computer program(s) herein is the property of
*
* Ericsson Inc. The programs may be used and/or copied only with written
*
* permission from Ericsson Inc. or in accordance with the terms and
*
* conditions stipulated in the agreement/contract under which the
*
* program(s) have been supplied.
*
******************************************************************************/
package com.ericsson.oss.edca.catalog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
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
import com.ericsson.oss.edca.catalog.common.CatalogResponse;
import com.ericsson.oss.edca.catalog.controller.dto.BulkDataRepositoryDTO;
import com.ericsson.oss.edca.catalog.service.BulkDataRepositoryService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1/bulk-data-repository")
@Api(value = "Bulkdatarepository", tags = "/v1/bulk-data-repository")
public class BulkDataRepositoryController {

    private static final Logger logger = LoggerFactory.getLogger(BulkDataRepositoryController.class);

    @Autowired
    private BulkDataRepositoryService bdrService;

    /**
     * This method is to register/save Bulk Data Repository details in the postgresSQL database
     *
     * @param bdrDto
     *            - BulkDataRepositoryDTO
     * @param result
     *            -
     * @param request
     * @return Saved BDR Path
     */

    @ApiOperation(value = "create BulkDataRepositoryDetails", response = BulkDataRepositoryDTO.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = BulkDataRepositoryDTO.class, message = "created BulkDataRepository details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "BulkDataRepository details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering bulk-data-repository details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerBDR(@ApiParam(name = "BulkDataRepository model", value = "BulkDataRepository", required = true) @RequestBody @Valid final BulkDataRepositoryDTO bdrDto,
            final BindingResult result, final HttpServletRequest request) {
        logger.info("Bulk-Data-Repository-Controller :: POST Request:: request body received {}", bdrDto);
        if (result.hasErrors()) {
            final List<FieldError> errors = result.getFieldErrors();
            final List<String> message = new ArrayList<>();
            for (final FieldError e : errors) {
                message.add(e.getDefaultMessage());
            }
            logger.error("Bulk-Data-Repository-Controller :: POST Request:: Mandatory fields missing {}", message);

            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), message.toString()));
        }
        logger.info("Bulk-Data-Repository-Controller :: POST Request:: Saving BDR Entry {}", bdrDto);
        final BulkDataRepositoryDTO createdDto = bdrService.saveBDR(bdrDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);

    }

    /**
     * This method is to fetch/get Bulk Data Repository details from the postgresSQL database, it handles queryParam fetch type and default as per request
     *
     * @param values
     *            - queryParams if present in the request
     * @return - Bulk Data Repository Details from the DB
     */

    @ApiOperation(value = "get all BulkDataRepository details or filter by QueryParams", response = BulkDataRepositoryDTO.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = BulkDataRepositoryDTO.class, message = "show BulkDataRepository details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "If Parameter values(nameSpace/name) is missing", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If BulkDataRepository detail is not found", response = CatalogResponse.class) })
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "name", value = "name value of the Bulk data repository", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "nameSpace", value = "nameSpace value of the Bulk data repository", dataTypeClass = String.class, required = false) })
    @Timed(description = "Time spent querying bulk-data-repository details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> queryAllBDR(@ApiIgnore @RequestParam final Map<String, String> values) {
        if (values.isEmpty()) {
            logger.info("Bulk-Data-Repository-Controller :: GET Request:: Getting all BDR details");
            final List<BulkDataRepositoryDTO> bdrList = bdrService.getAllBDR();
            return ResponseEntity.ok().body(bdrList);
        }
        final String nameSpace = values.get("nameSpace");
        final String name = values.get("name");
        if (name == null || nameSpace == null || name.strip().isEmpty() || nameSpace.strip().isEmpty() || values.size() > 2) {
            logger.error("Bulk-Data-Repository-Controller :: GET Request:: Name/Namespace parameters missing in the request");
            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), CatalogConstants.BDR_QPARAMS_ERROR_MSG));
        }
        logger.info("Bulk-Data-Repository-Controller :: GET Request:: Details for the name & Namespace {}, {}", name, nameSpace);
        final List<BulkDataRepositoryDTO> bdrDto = bdrService.getBDRByNameSpaceandName(nameSpace, name);
        if (bdrDto.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return ResponseEntity.ok().body(bdrDto);
    }

    /**
     * * This method is to fetch/get Bulk Data Repository details from the postgresSQL database using Id as a path variable in request
     *
     * @param id
     *            - id(numeric value) of the Particular Bulk Data Repository entry
     * @return - Bulk Data Repository Details from the DB
     *
     */

    @ApiOperation(value = "get BulkDataRepositoryDetails by id", response = BulkDataRepositoryDTO.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = BulkDataRepositoryDTO.class, message = "get BulkDataRepositoryDetails by id"),
            @ApiResponse(code = 400, message = "If Parameter values(nameSpace/name) is missing", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If BulkDataRepository detail is not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies BulkDataRepository", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying bulk-data-repository detail by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> queryBDRById(@PathVariable final String id) {
        logger.info("Bulk-Data-Repository-Controller :: GET Request:: Details for the Id {}", id);
        final BulkDataRepositoryDTO bdrDto = bdrService.getBDRById(Integer.parseInt(id));
        if (bdrDto == null) {
            throw new EntityNotFoundException();
        }

        return ResponseEntity.ok().body(bdrDto);
    }

}
