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
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.common.CatalogResponse;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatDto;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatResponseDto;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.service.FileFormatService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/v1/file-format")
@Api(value = "FileFormat", tags = "/v1/file-format")
public class FileFormatController {

    private static final Logger logger = LoggerFactory.getLogger(FileFormatController.class);

    @Autowired
    private FileFormatService fileFormatService;

    /**
     * This method is to register/save FileFormat details in the postgresSQL database
     *
     * @param fileformatDto
     *            - FileFormatDto
     * @param bindingResult
     *            - BindingResult
     * @return FileFormat and associated details
     */
    @ApiOperation(value = "create FileFormat details", response = FileFormatDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = FileFormatDto.class, message = "created FileFormat details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "FileFormat details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering file-format details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerFileFormat(@RequestBody @Valid final FileFormatDto fileFormatDto, final BindingResult bindingResult) throws CatalogException {
        if (!bindingResult.hasErrors()) {
            final FileFormatResponseDto registeredFileFormatDto = fileFormatService.saveFileFormat(fileFormatDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredFileFormatDto);
        }
        logger.error("Catalog Service :: registerFileFormat Required parameter(s) for registering fileFormat may not be available");
        final CatalogResponse registerFileFormatErrorResponse = new CatalogResponse(LocalDateTime.now(),
                CatalogConstants.BAD_REQUEST_HTTP_MESSAGE + " - Missing field(s)" + bindingResult.getFieldError().getField());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerFileFormatErrorResponse);

    }

    /**
     * Method to query FileFormat details from the postgresSQL database maintaining following search criteria : 1. All file formats for a dataspace and/or data category (e.g. all stats files for 5G,
     * all stats files, all 5G file formats) 2 .All file formats for a dataspace and a data provider type (e.g. 5G CTR)
     *
     * @param queryParams
     *            - query parameters for fetching dataProviderTypes
     * @return FileFormat and associated details
     */
    @ApiOperation(value = "get all fileformat details or filter by QueryParams", notes = "Search Criteria: \r\n" + "\r\n"
            + "All file formats for a dataspace and/or data category (e.g. all stats files for 5G, all stats files, all 5G file formats)\r\n"
            + "All file formats for a dataspace and a data provider type (e.g. 5G CTR)", response = FileFormatDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = FileFormatDto.class, message = "get FileFormat details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "FileFormat details not found", response = CatalogResponse.class) })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "dataSpace", value = "Dataspace name to which the dataProviderType is associated", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "dataProvider", value = "DataProviderType Identifier to which the dataSpace is associated", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "dataCategory", value = "DataCategory to which the dataProviderType is associated", dataTypeClass = DataCategory.class, required = false)

    })
    @Timed(description = "Time spent querying file-format details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> queryAllFileFormatByParams(@ApiIgnore @RequestParam(required = false) final Map<String, String> queryParams) {

        logger.info("Catalog Service :: queryFileFormatByParams validateQueryParam() : {}", isValidQueryParam(queryParams));
        if (!isValidQueryParam(queryParams)) {
            final CatalogResponse invalidDataspaceQueryParamResponseMessage = new CatalogResponse(LocalDateTime.now(), CatalogConstants.FILEFORMAT_QPARAMS_ERROR_MSG);
            return ResponseEntity.badRequest().body(invalidDataspaceQueryParamResponseMessage);
        }
        if (!queryParams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(fileFormatService.getFileFormatByQueryParams(queryParams));
        }
        return queryAllFileFormats();
    }

    /**
     * Method to query FileFormat details from the postgresSQL database for fileFormat filtered by fileFormat id
     *
     * @param id
     * @return FileFormat and associated details
     * @throws Exception
     */
    @ApiOperation(value = "get fileFormat details by id", response = FileFormatDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = FileFormatDto.class, message = "get fileFormat details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "fileFormat details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies fileFormat", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying file-format by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> queryFileFormatById(@PathVariable final String id) {
        FileFormatResponseDto queriedFileFormatDtoById = null;
        queriedFileFormatDtoById = fileFormatService.getFileFormatById(id);
        if (queriedFileFormatDtoById == null) {
            throw new EntityNotFoundException();
        }
        return ResponseEntity.ok(queriedFileFormatDtoById);

    }

    private ResponseEntity<Object> queryAllFileFormats() {
        final List<FileFormatResponseDto> queriedFormatDtos = fileFormatService.getAllFileFormats();
        return ResponseEntity.ok(queriedFormatDtos);
    }

    private boolean isValidQueryParam(final Map<String, String> queryParams) {
        logger.info("Catalog Service :: FileFormat :: isValidQueryParam queryParams {} ", queryParams);
        final Set<String> validQueryParams = Set.of("dataSpace", "dataProviderType", "dataCategory");
        if (!queryParams.isEmpty()) {
            // if invalid query params provided with valid , then consider as BAD request ( Business constraint )
            return validQueryParams.containsAll(queryParams.keySet());
        }
        return true;
    }

}
