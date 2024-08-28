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
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import com.ericsson.oss.edca.catalog.controller.dto.MessageStatusTopicDto;
import com.ericsson.oss.edca.catalog.service.MessageStatusTopicService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "v1/message-status-topic")
@Api(value = "MessageStatusTopic", tags = "/v1/message-status-topic")
public class MessageStatusTopicController {

    private static final Logger logger = LoggerFactory.getLogger(MessageStatusTopicController.class);

    @Autowired
    private MessageStatusTopicService messageStatusTopicService;

    /**
     * This method is intended to handle creating a message status topic
     *
     * @param MessageStatusTopicDto
     *            - Message Status Topic to be created
     * @param bindResult
     *            - BindingResult to check validation errors
     * @param request
     *            - HttpServletRequest to track request context
     *
     * @return created Message Status Topic
     *
     */

    @ApiOperation(value = "create MessageStatusTopic details", response = MessageStatusTopicDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = MessageStatusTopicDto.class, message = "created MessageStatusTopic details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "MessageStatusTopic details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering message-status-topic details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerMessageStatusTopic(
            @ApiParam(name = "MessageStatusTopic model", value = "MessageStatusTopic", required = true) @RequestBody @Valid final MessageStatusTopicDto messageStatusTopicDto,
            final BindingResult result, final HttpServletRequest request) throws CatalogException {

        logger.info("Message-Status-Topic-Controller :: POST Request:: request body received {}", messageStatusTopicDto);

        if (result.hasErrors()) {

            final List<ObjectError> errorList = result.getAllErrors();
            final List<String> errorMessageList = new ArrayList<>();
            for (final ObjectError objErr : errorList) {
                errorMessageList.add(objErr.getDefaultMessage());
            }

            logger.info("Message-Status-Topic-Controller :: POST Request:: Mandatory fields missing {}", messageStatusTopicDto);

            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), errorMessageList.toString()));
        }

        final MessageStatusTopicDto savedMessageStatusTopicDto = messageStatusTopicService.createMessageStatusTopic(messageStatusTopicDto);

        if (savedMessageStatusTopicDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMessageStatusTopicDto);
        }

        return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_CONFLICT_MSG), HttpStatus.CONFLICT);
    }

    /**
     * This method will fetch Message Status Topic details matching the query params if provided else will return all Message Status topics.
     *
     * @param values
     *            - queryParams if present in the request
     *
     * @return Message Status Topic/s Details from the DB
     *
     */

    @ApiOperation(value = "get all MessageStatusTopic details or filter by QueryParams", response = MessageStatusTopicDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageStatusTopicDto.class, message = "get MessageStatusTopic details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If MessageStatusTopic details not found", response = CatalogResponse.class) })
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "name", value = "name of the MessageStatusTopic", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "messageBusId", value = "messageBusId related with MessageStatusTopic", dataTypeClass = String.class, required = false) })
    @Timed(description = "Time spent querying message-status-topic details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getAllMessageStatusTopics(@ApiIgnore @RequestParam final Map<String, String> values) {

        if (values.isEmpty()) {
            logger.info("Message-Status-Topic-Controller :: GET Request:: Getting all Message Status Topics");
            final List<MessageStatusTopicDto> messageStatusTopicDtoList = messageStatusTopicService.getAllMessageStatusTopics();
            return ResponseEntity.ok().body(messageStatusTopicDtoList);
        }

        if (!(values.containsKey("name") && values.containsKey("messageBusId")) || values.size() > 2) {
            logger.error("Message-Status-Topic-Controller :: GET Request:: name or messageBusId is missing in the request");
            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), CatalogConstants.NT_QPARAMS_ERROR_MSG));
        }

        final String name = values.get("name");
        final int messageBusId = Integer.parseInt(values.get("messageBusId"));

        logger.info("Message-Status-Topic-Controller :: GET Request:: Details for the name and messageBusId {}, {}", name, messageBusId);
        final MessageStatusTopicDto messageStatusTopicDto = messageStatusTopicService.getMessageStatusTopicByNameAndMessageBusId(name, messageBusId);
        if (messageStatusTopicDto == null) {
            logger.error("Message-Status-Topic-Controller :: GET BY QueryParams :: Message Status Topic not found for the given name and messageBusId {},{}", name, messageBusId);
            return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_NOT_FOUND_MSG), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(messageStatusTopicDto);
    }

    /**
     * * This method is to fetch Message Status Topic details for the given ID
     *
     * @param id
     *            - id(numeric value) of the Particular Message Status Topic entry
     *
     * @return Message Status Topic Details from the DB
     *
     */

    @ApiOperation(value = "get MessageStatusTopic by Id", response = MessageStatusTopicDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageStatusTopicDto.class, message = "get MessageStatusTopic details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If MessageStatusTopic details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies MessageStatusTopic", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying message-status-topic detail by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getMessageStatusTopicById(@PathVariable final String id) {

        logger.info("Message-Status-Topic-Controller :: GET BY ID :: Details of id recieved {}", id);

        final int messageStatusTopicId = Integer.parseInt(id);
        final MessageStatusTopicDto messageStatusTopicDto = messageStatusTopicService.getMessageStatusTopicById(messageStatusTopicId);
        if (messageStatusTopicDto == null) {
            logger.error("Message-Status-Topic-Controller :: GET BY ID :: Message Status Topic not found for the given ID {}", messageStatusTopicId);
            throw new EntityNotFoundException();
        }

        return ResponseEntity.ok().body(messageStatusTopicDto);
    }

}
