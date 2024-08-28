/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.edca.catalog.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import com.ericsson.oss.edca.catalog.controller.dto.NotificationTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.service.NotificationTopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(NotificationTopicController.class)
public class NotificationTopicControllerTest {

    @MockBean
    private NotificationTopicService notificationTopicService;

    @Autowired
    private MockMvc mockMvc;

    private NotificationTopicDto notificationTopicDTO;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setupDTO() {
        notificationTopicDTO = new NotificationTopicDto();
        notificationTopicDTO.setId(101);
        notificationTopicDTO.setEncoding(MessageEncoding.JSON);
        notificationTopicDTO.setMessageBusId(1);
        notificationTopicDTO.setSpecificationReference("specRef1");
        notificationTopicDTO.setName("topic1");
        notificationTopicDTO.setFileFormatIds(Stream.of(101, 102).collect(Collectors.toSet()));

    }

    @Test
    void testSaveNotificationTopic() throws JsonProcessingException, Exception {
        when(notificationTopicService.createNotificationTopic(Mockito.any(NotificationTopicDto.class))).thenReturn(notificationTopicDTO);
        String jsonString = "{\r\n" + "    \"messageBusId\": 1,\r\n" + "    \"name\": \"topic5\",\r\n" + "    \"encoding\": \"JSON\",\r\n" + "    \"specificationReference\": \"specref1\"\r\n" + "}";
        this.mockMvc.perform(post("/v1/notification-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isCreated()).andDo(print());
    }

    @Test
    void testSaveNotificationTopicWithMandatoryParamsMissing() throws JsonProcessingException, Exception {
        notificationTopicDTO.setName(null);
        this.mockMvc.perform(post("/v1/notification-topic").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(notificationTopicDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void testSaveNotificationTopicWithExistingValue() throws JsonProcessingException, Exception {
        String jsonString = "{\r\n" + "    \"messageBusId\": 1,\r\n" + "    \"name\": \"topic5\",\r\n" + "    \"encoding\": \"JSON\",\r\n" + "    \"specificationReference\": \"specref1\"\r\n" + "}";
        this.mockMvc.perform(post("/v1/notification-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isConflict());
    }

    @Test
    void testGetNotificationTopicById() throws JsonProcessingException, Exception {
        when(notificationTopicService.getNotificationTopicById(Mockito.anyInt())).thenReturn(notificationTopicDTO);
        this.mockMvc.perform(get("/v1/notification-topic/{id}", notificationTopicDTO.getId())).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(notificationTopicDTO)));
    }

    @Test
    void testGetNotificationWithNonAvailableId() throws JsonProcessingException, Exception {
        when(notificationTopicService.getNotificationTopicById(Mockito.anyInt())).thenReturn(null);
        this.mockMvc.perform(get("/v1/notification-topic/{id}", notificationTopicDTO.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testGetAlltGetNotifications() throws JsonProcessingException, Exception {
        when(notificationTopicService.getAllNotificationTopics()).thenReturn(java.util.List.of(notificationTopicDTO));
        this.mockMvc.perform(get("/v1/notification-topic")).andExpect(status().isOk());
    }

    @Test
    void testGetbyQueryParams() throws JsonProcessingException, Exception {
        when(notificationTopicService.getNotificationTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(notificationTopicDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", notificationTopicDTO.getName());
        requestParams.add("messageBusId", Integer.toString(notificationTopicDTO.getMessageBusId()));
        this.mockMvc.perform(get("/v1/notification-topic").queryParams(requestParams)).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(notificationTopicDTO)));
    }

    @Test
    void testGetNotificationTopicWithNameParamMissing() throws JsonProcessingException, Exception {
        //when(notificationTopicService.getNotificationTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt()))
        // .thenReturn(notificationTopicDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("messageBusId", Integer.toString(notificationTopicDTO.getMessageBusId()));
        this.mockMvc.perform(get("/v1/notification-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetNotificationTopicWithMsgBusIdParamMissing() throws JsonProcessingException, Exception {
        //when(notificationTopicService.getNotificationTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt()))
        //.thenReturn(notificationTopicDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", notificationTopicDTO.getName());
        this.mockMvc.perform(get("/v1/notification-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetmsgBusWithextraParams() throws JsonProcessingException, Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", notificationTopicDTO.getName());
        requestParams.add("messageBusId", Integer.toString(notificationTopicDTO.getMessageBusId()));
        requestParams.add("test", "xyz");
        this.mockMvc.perform(get("/v1/notification-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testNonExistingNotificationTopicEntryByNameAndMsgBusIdTest() throws Exception {
        when(notificationTopicService.getNotificationTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(null);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", notificationTopicDTO.getName());
        requestParams.add("messageBusId", Integer.toString(notificationTopicDTO.getMessageBusId()));
        this.mockMvc.perform(get("/v1/notification-topic").queryParams(requestParams)).andExpect(status().isNotFound());
    }

}
