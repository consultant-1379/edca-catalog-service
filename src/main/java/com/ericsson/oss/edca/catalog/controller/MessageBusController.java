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
import java.util.Collections;
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
import com.ericsson.oss.edca.catalog.controller.dto.MessageBusDto;
import com.ericsson.oss.edca.catalog.service.MessageBusService;

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
@RequestMapping("/v1/message-bus")
@Api(value = "MessageBus", tags = "/v1/message-bus")
public class MessageBusController {

    private static final Logger logger = LoggerFactory.getLogger(MessageBusController.class);

    @Autowired
    private MessageBusService messageBusService;

    /**
     * This method is to get MessageBus details based on queryParam/all Message Bus details in the postgresSQL database
     *
     * @param values
     *            - Parameters(name/nameSpace) values
     * @return MessageBus and associated notificationTopicIds details
     */
    @ApiOperation(value = "get all Message Bus details or filter by QueryParams", response = MessageBusDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageBusDto.class, message = "get Message Bus details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If Message Bus details not found", response = CatalogResponse.class) })
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "name", value = "name value of the Message Bus", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "nameSpace", value = "nameSpace value of the Message bus", dataTypeClass = String.class, required = false) })
    @Timed(description = "Time spent querying message-bus details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getMessageBus(@ApiIgnore @RequestParam final Map<String, String> values) {
        if (values.isEmpty()) {
            logger.info("MessageBusController :: GET Request:: Getting all Message Bus details");
            final List<MessageBusDto> msgBusList = messageBusService.getMessageBuses();
            return ResponseEntity.ok().body(msgBusList);
        }
        final String nameSpace = values.get("nameSpace");
        final String name = values.get("name");
        if (name == null || nameSpace == null || name.strip().isEmpty() || nameSpace.strip().isEmpty() || values.size() > 2) {
            logger.error("MessageBusController :: GET Request:: Name/Namespace parameters missing in the request");
            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), CatalogConstants.MSGBUS_QPARAMS_ERROR_MSG));
        }
        logger.info("Catalog Service:: GET Request:: Details for the name & Namespace {}, {}", name, nameSpace);
        final List<MessageBusDto> msgBusDto = messageBusService.getMessageBusByNameAndNamespace(name, nameSpace);
        if (msgBusDto.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return ResponseEntity.ok().body(msgBusDto);
    }

    /**
     * This method is to get Message Bus details based on Id in the postgresSQL database
     *
     * @param messageBusId
     *            - String
     * @return MessageBus and associated notificationTopicIds details
     */
    @ApiOperation(value = "get Message Bus by Id", response = MessageBusDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = MessageBusDto.class, message = "get Message Bus details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If Message Bus details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies Message bus", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying message-bus detail by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getMessageBus(@PathVariable("id") final String id) {
        logger.info("Catalog Service:: MessageBus GET Request:: Details for the Id {}", id);
        final MessageBusDto msgBusDto = messageBusService.getMessageBusById(Integer.parseInt(id));
        if (msgBusDto == null) {
            throw new EntityNotFoundException();
        }

        return ResponseEntity.ok().body(msgBusDto);
    }

    /**
     * This method is to register/save Message Bus details in the postgresSQL database
     *
     * @param messageBusDto
     *            - MessageBusDto
     * @param result
     *            - Binding Result
     * @param request
     *            - HttpServletRequest
     * @return MessageBus and associated notificationTopicIds details
     */

    @ApiOperation(value = "create Message Bus details", response = MessageBusDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = MessageBusDto.class, message = "created Message Bus details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "Message Bus details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering message-bus details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> createMessageBus(@ApiParam(name = "MessageBus model", value = "MessageBus", required = true) @RequestBody @Valid final MessageBusDto msgBusDto,
            final BindingResult result, final HttpServletRequest request) {
        logger.info("MessageBusController :: POST Request:: request body received {}", msgBusDto);
        if (result.hasErrors()) {
            final List<FieldError> errors = result.getFieldErrors();
            final List<String> message = new ArrayList<>();
            for (final FieldError e : errors) {
                message.add(e.getDefaultMessage());
            }
            Collections.sort(message);
            logger.error("MessageBusController :: POST Request:: Mandatory fields missing {}", message);

            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), message.toString()));
        }
        logger.info("MessageBusController :: POST Request:: Saving MessageBus Entry {}", msgBusDto);
        final MessageBusDto savedMsgBusDto = messageBusService.createMessageBus(msgBusDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMsgBusDto);

    }

}
