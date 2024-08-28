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
import java.util.Map;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ericsson.oss.edca.catalog.common.CatalogResponse;
import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaResponseDto;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.MessageSchema;
import com.ericsson.oss.edca.catalog.service.MessageSchemaService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/v1/message-schema")
@Api(value = "MessageSchema", tags = "/v1/message-schema")
public class MessageSchemaController {

    private static final Logger logger = LoggerFactory.getLogger(MessageSchemaController.class);

    @Autowired
    private MessageSchemaService messageSchemaService;

    /**
     * This method is to register MessageSchema details for a data topic
     *
     * @param messageSchemaDto
     *            - MessageSchemaDto
     * @param bindingResult
     *            - BindingResult
     * @return MessageSchema and associated details
     */
    @ApiOperation(value = "create MessageSchema details", response = MessageSchemaDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = MessageSchemaDto.class, message = "created MessageSchema details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "MessageSchema details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering message-schema details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerMessageSchema(@RequestBody @Valid final MessageSchemaDto messageSchemaDto, final BindingResult bindingResult) throws CatalogException {
        if (!bindingResult.hasErrors()) {
            final MessageSchemaResponseDto registeredMessageSchemaDto = messageSchemaService.saveMessageSchema(messageSchemaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredMessageSchemaDto);
        }
        logger.error("Catalog Service :: registerMessageSchema Required parameter(s) for registering messageSchema may not be available");
        final CatalogResponse registerMessageSchemaErrorResponse = new CatalogResponse(LocalDateTime.now(),
                CatalogConstants.BAD_REQUEST_HTTP_MESSAGE + " - Missing field(s)" + bindingResult.getFieldError().getField());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerMessageSchemaErrorResponse);

    }

    /**
     * This method is used to fetch Message Schema details if query params are provided else will fetch all registered Message Schemas
     *
     * @param queryParams
     *            - query parameters for fetching messageSchema
     * @return MessageSchema and associated details
     */
    @ApiOperation(value = "get all messageSchema details or filter by QueryParams", response = MessageSchema.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageSchema.class, message = "get MessageSchema details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "MessageSchema details not found", response = CatalogResponse.class) })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "dataSpace", value = "Dataspace name to which the dataProviderType is associated", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "dataProvider", value = "DataProviderType Identifier to which the dataSpace is associated", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "dataCategory", value = "DataCategory to which the dataProviderType is associated", dataTypeClass = DataCategory.class, required = false)

    })
    @Timed(description = "Time spent querying message-schema details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> queryAllMessageSchemaByParams(@ApiIgnore @RequestParam(required = false) final Map<String, String> queryParams) {

        logger.info("Catalog Service :: queryMessageSchemaByParams validateQueryParam() : {}", isValidQueryParam(queryParams));
        if (!isValidQueryParam(queryParams)) {
            final CatalogResponse invalidDataspaceQueryParamResponseMessage = new CatalogResponse(LocalDateTime.now(), CatalogConstants.MESSAGESCHEMA_QPARAMS_ERROR_MSG);
            return ResponseEntity.badRequest().body(invalidDataspaceQueryParamResponseMessage);
        }
        if (queryParams.isEmpty()) {
            logger.info("Message-Schema-Controller :: GET Request:: Getting all Message Schemas");
            final List<MessageSchemaResponseDto> queriedSchemaDtos = messageSchemaService.getAllMessageSchema();
            return ResponseEntity.ok(queriedSchemaDtos);
        }

        String topic = queryParams.get("topic");
        int countChar = StringUtils.countOccurrencesOf(topic, ":");
        try {
            if (((queryParams.containsKey("dataSpace") && queryParams.containsKey("dataCategory")) || (queryParams.containsKey("dataSpace"))
                    || (queryParams.containsKey("dataCategory") && !queryParams.containsKey("dataProviderType"))) && queryParams.size() < 3) {
                @SuppressWarnings("unchecked")
                final List<MessageSchemaResponseDto> messageSchemaResponseDto = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByQueryParams(queryParams);
                if (messageSchemaResponseDto.isEmpty()) {
                    logger.error("Message-Schema-Controller :: GET BY QueryParams :: Message Schema not found for the given queryParams {}", queryParams);
                    return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_NOT_FOUND_MSG), HttpStatus.NOT_FOUND);
                }
                return ResponseEntity.ok(messageSchemaResponseDto);
            } else if (queryParams.containsKey("topic") && countChar == 2) {
                final List<MessageSchemaResponseDto> messageSchemaResponseDto = messageSchemaService.getMessageSchemaByTopic(queryParams);
                if (messageSchemaResponseDto.isEmpty()) {
                    logger.error("Message-Schema-Controller :: GET BY topic :: Message Schema not found for the given topic {}", queryParams);
                    return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_NOT_FOUND_MSG), HttpStatus.NOT_FOUND);
                }
                return ResponseEntity.ok(messageSchemaResponseDto);
            } else {
                logger.error("Message-Schema-Controller :: GET Request:: dataSpace or dataCategory or dataProviderType or topic is missing in the request");
                return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), CatalogConstants.MESSAGESCHEMA_QPARAMS_ERROR_MSG));
            }
        } catch (Exception e) {
            logger.info("Message-Schema-Controller :: GET Request:: query is not valid {}", e.getMessage());
            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), CatalogConstants.MESSAGESCHEMA_QPARAMS_ERROR_MSG));
        }

    }

    /**
     * Method to fetch MessageSchema details for the provided ID
     *
     * @param id
     * @return MessageSchema and associated details
     * @throws Entity
     *             Not Found Exception
     */
    @ApiOperation(value = "get messageSchema details by id", response = MessageSchemaDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageSchemaDto.class, message = "get messageSchema details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "messageSchema details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies messageSchema", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying message-schema by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> queryMessageSchemaById(@PathVariable final String id) {
        MessageSchemaResponseDto queriedMessageSchemaDtoById = null;
        queriedMessageSchemaDtoById = messageSchemaService.getMessageSchemaById(id);
        if (queriedMessageSchemaDtoById == null) {
            throw new EntityNotFoundException();
        }
        return ResponseEntity.ok(queriedMessageSchemaDtoById);

    }

    private boolean isValidQueryParam(final Map<String, String> queryParams) {
        logger.info("Catalog Service :: MessageSchema :: isValidQueryParam queryParams {} ", queryParams);
        final Set<String> validQueryParams = Set.of("dataSpace", "dataProviderType", "dataCategory", "topic");
        if (!queryParams.isEmpty()) {
            return validQueryParams.containsAll(queryParams.keySet());
        }
        return true;
    }

}