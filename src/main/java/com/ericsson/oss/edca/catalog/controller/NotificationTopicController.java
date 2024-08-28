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
import org.springframework.validation.ObjectError;
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
import com.ericsson.oss.edca.catalog.controller.dto.NotificationTopicDto;
import com.ericsson.oss.edca.catalog.service.NotificationTopicService;

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
@RequestMapping(path = "v1/notification-topic")
@Api(value = "NotificationTopic", tags = "/v1/notification-topic")
public class NotificationTopicController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationTopicController.class);

    @Autowired
    private NotificationTopicService notificationTopicService;

    /**
     * This method is intended to handle POST request methods
     *
     * @param NotificationTopicDto
     *            - Notification Topic to be created
     * @param bindResult
     *            - BindingResult to check validation errors
     * @param request
     *            - HttpServletRequest to track request context
     *
     * @return The created Notification Topic
     *
     */

    @ApiOperation(value = "create NotificationTopic details", response = NotificationTopicDto.class, produces = "application/json", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, response = NotificationTopicDto.class, message = "created NotificationTopic details"),
            @ApiResponse(code = 400, message = "Bad Request in values", response = CatalogResponse.class),
            @ApiResponse(code = 409, message = "NotificationTopic details exists already", response = CatalogResponse.class) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @Timed(description = "Time spent registering notification-topic details")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> registerNotificationTopic(
            @ApiParam(name = "NotificationTopic model", value = "NotificationTopic", required = true) @RequestBody @Valid final NotificationTopicDto notificationTopicDto, final BindingResult result,
            final HttpServletRequest request) {

        logger.info("Notification-Topic-Controller :: POST Request:: request body received {}", notificationTopicDto);

        if (result.hasErrors()) {

            final List<ObjectError> errorList = result.getAllErrors();
            final List<String> errorMessageList = new ArrayList<>();
            for (final ObjectError objErr : errorList) {
                errorMessageList.add(objErr.getDefaultMessage());
            }
            Collections.sort(errorMessageList);
            logger.info("Notification-Topic-Controller :: POST Request:: Mandatory fields missing {}", notificationTopicDto);

            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), errorMessageList.toString()));
        }

        final NotificationTopicDto savedNotificationTopicDto = notificationTopicService.createNotificationTopic(notificationTopicDto);

        if (savedNotificationTopicDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNotificationTopicDto);
        }

        return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_CONFLICT_MSG), HttpStatus.CONFLICT);
    }

    /**
     * This method is to get/fetch particular Notification Topic details if query params are given else will get/fetch all notification topics.
     *
     * @param values
     *            - queryParams if present in the request
     *
     * @returnNotifcation Topic/s Details from the DB
     *
     */

    @ApiOperation(value = "get all NotificationTopic details or filter by QueryParams", response = NotificationTopicDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = NotificationTopicDto.class, message = "get NotificationTopic details", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If NotificationTopic details not found", response = CatalogResponse.class) })
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "name", value = "name of the NotificationTopic", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(paramType = "query", name = "messageBusId", value = "messageBusId related with NotificationTopic", dataTypeClass = String.class, required = false) })
    @Timed(description = "Time spent querying notification-topic details")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getAllNotificationTopics(@ApiIgnore @RequestParam final Map<String, String> values) {

        if (values.isEmpty()) {
            logger.info("Notification-Topic-Controller :: GET Request:: Getting all Notification Topics");
            final List<NotificationTopicDto> notificationTopicDtoList = notificationTopicService.getAllNotificationTopics();
            return ResponseEntity.ok().body(notificationTopicDtoList);
        }

        if (!(values.containsKey("name") && values.containsKey("messageBusId")) || values.size() > 2) {
            logger.error("Notification-Topic-Controller :: GET Request:: name or messageBusId is missing in the request");
            return ResponseEntity.badRequest().body(new CatalogResponse(LocalDateTime.now(), CatalogConstants.NT_QPARAMS_ERROR_MSG));
        }

        final String name = values.get("name");
        final int messageBusId = Integer.parseInt(values.get("messageBusId"));

        logger.info("Notification-Topic-Controller :: GET Request:: Details for the name and messageBusId {}, {}", name, messageBusId);
        final NotificationTopicDto notificationTopicDto = notificationTopicService.getNotificationTopicByNameAndMessageBusId(name, messageBusId);
        if (notificationTopicDto == null) {
            logger.error("Notification-Topic-Controller :: GET BY QueryParams :: Notification Topic not found for the given name and messageBusId {},{}", name, messageBusId);
            return new ResponseEntity<>(new CatalogResponse(LocalDateTime.now(), CatalogConstants.COMMON_NOT_FOUND_MSG), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(notificationTopicDto);
    }

    /**
     * * This method is to fetch/get Notification Topic details using Id as a path variable
     *
     * @param id
     *            - id(numeric value) of the Particular Notification Topic entry
     *
     * @return Notification Topic Details from the DB
     *
     */

    @ApiOperation(value = "get NotificationTopic by Id", response = NotificationTopicDto.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, response = NotificationTopicDto.class, message = "get NotificationTopic details"),
            @ApiResponse(code = 400, message = "Bad Request in request values", response = CatalogResponse.class),
            @ApiResponse(code = 404, message = "If NotificationTopic details not found", response = CatalogResponse.class) })
    @ApiImplicitParam(paramType = "path", value = "id uniquely identifies NotificationTopic", name = "id", dataTypeClass = String.class, required = true)
    @Timed(description = "Time spent querying notification-topic detail by identifier")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getNotificationTopicById(@PathVariable final String id) {

        logger.info("Notification-Topic-Controller :: GET BY ID :: Details of id recieved {}", id);

        final int notificationTopicId = Integer.parseInt(id);
        final NotificationTopicDto notificationTopicDto = notificationTopicService.getNotificationTopicById(notificationTopicId);
        if (notificationTopicDto == null) {
            logger.error("Notification-Topic-Controller :: GET BY ID :: Notification Topic not found for the given ID {}", notificationTopicId);
            throw new EntityNotFoundException();
        }

        return ResponseEntity.ok().body(notificationTopicDto);
    }

}
