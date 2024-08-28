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
import com.ericsson.oss.edca.catalog.controller.dto.MessageDataTopicDto;
import com.ericsson.oss.edca.catalog.service.MessageDataTopicService;

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
@RequestMapping(path = "v1/message-data-topic")
@Api(value = "MessageDataTopic", tags = "/v1/message-data-topic")
public class MessageDataTopicController {

    private static final Logger logger = LoggerFactory.getLogger(MessageDataTopicController.class);

    @Autowired
    private MessageDataTopicService messageDataTopicService;

    /**
     * This method is intended to handle create messageDataTopic
     *
     * @param MessageDataTopicDto
     *            - Message Data Topic to be created
     * @param bindResult
     *            - BindingResult to check validation errors
     * @param request
     *            - HttpServletRequest to track request context
     *
     * @return created Message Data Topic
     *
     */

    @ApiOperation(value = "create MessageDataTopic details", response = MessageDataTopicDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = MessageDataTopicDto.class, message = "created MessageDataTopic details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "MessageDataTopic details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering message-data-topic details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerMessageDataTopic(
            @ApiParam(name = "MessageDataTopic model", value = "MessageDataTopic", required = true) @RequestBody @Valid final MessageDataTopicDto messageDataTopicDto, final BindingResult result,
            final HttpServletRequest request) throws CatalogException {

        logger.info("Message-data-Topic-Controller :: POST Request:: request body received {}", messageDataTopicDto);

        if (result.hasErrors()) {

            final List<ObjectError> errorList = result.getAllErrors();
            final List<String> errorMessageList = new ArrayList<>();
            for (final ObjectError objErr : errorList) {
                errorMessageList.add(objErr.getDefaultMessage());
            }

            logger.info("Message-Data-Topic-Controller :: POST Request:: Mandatory fields missing {}", messageDataTopicDto);

            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), errorMessageList.toString()));
        }

        final MessageDataTopicDto savedMessageDataTopicDto = messageDataTopicService.createMessageDataTopic(messageDataTopicDto);

        if (savedMessageDataTopicDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMessageDataTopicDto);
        }

        return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_CONFLICT_MSG), HttpStatus.CONFLICT);
    }

    /**
     * This method will fetch Message Data Topic details matching the query params if provided else will return all Message Data topics.
     *
     * @param values
     *            - queryParams if present in the request
     *
     * @return Message Data Topic/s Details from the DB
     *
     */

    @ApiOperation(value = "get all MessageDataTopic details or filter by QueryParams", response = MessageDataTopicDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageDataTopicDto.class, message = "get MessageDataTopic details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If MessageDataTopic details not found", response = CatalogResponse.class) })
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "name", value = "name of the MessageDataTopic", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "messageBusId", value = "messageBusId related with MessageDataTopic", dataTypeClass = String.class, required = false) })
    @Timed(description = "Time spent querying message-data-topic details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getAllMessageDataTopics(@ApiIgnore @RequestParam final Map<String, String> values) {

        if (values.isEmpty()) {
            logger.info("Message-Data-Topic-Controller :: GET Request:: Getting all Message Data Topics");
            final List<MessageDataTopicDto> messageDataTopicDtoList = messageDataTopicService.getAllMessageDataTopics();
            return ResponseEntity.ok().body(messageDataTopicDtoList);
        }

        if (!(values.containsKey("name") && values.containsKey("messageBusId")) || values.size() > 2) {
            logger.error("Message-Data-Topic-Controller :: GET Request:: name or messageBusId is missing in the request");
            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), CatalogConstants.NT_QPARAMS_ERROR_MSG));
        }

        final String name = values.get("name");
        final int messageBusId = Integer.parseInt(values.get("messageBusId"));

        logger.info("Message-Data-Topic-Controller :: GET Request:: Details for the name and messageBusId {}, {}", name, messageBusId);
        final MessageDataTopicDto messageDataTopicDto = messageDataTopicService.getMessageDataTopicByNameAndMessageBusId(name, messageBusId);
        if (messageDataTopicDto == null) {
            logger.error("Message-Data-Topic-Controller :: GET BY QueryParams :: Message Data Topic not found for the given name and messageBusId {},{}", name, messageBusId);
            return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_NOT_FOUND_MSG), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(messageDataTopicDto);
    }

    /**
     * * This method will fetch Message Data Topic details for the provided ID
     *
     * @param id
     *            - id(numeric value) of the Particular Message Data Topic entry
     *
     * @return Message Data Topic Details from the DB
     *
     */

    @ApiOperation(value = "get MessageDataTopic by Id", response = MessageDataTopicDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageDataTopicDto.class, message = "get MessageDataTopic details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If MessageDataTopic details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies MessageDataTopic", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying message-data-topic detail by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getMessageDataTopicById(@PathVariable final String id) {

        logger.info("Message-Data-Topic-Controller :: GET BY ID :: Details of id recieved {}", id);

        final int messageDataTopicId = Integer.parseInt(id);
        final MessageDataTopicDto messageDataTopicDto = messageDataTopicService.getMessageDataTopicById(messageDataTopicId);
        if (messageDataTopicDto == null) {
            logger.error("Message-Data-Topic-Controller :: GET BY ID :: Message Status Topic not found for the given ID {}", messageDataTopicId);
            throw new EntityNotFoundException();
        }

        return ResponseEntity.ok().body(messageDataTopicDto);
    }

}
