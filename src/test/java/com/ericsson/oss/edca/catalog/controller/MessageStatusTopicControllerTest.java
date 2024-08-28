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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

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

import com.ericsson.oss.edca.catalog.controller.dto.MessageStatusTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.service.MessageStatusTopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MessageStatusTopicController.class)
public class MessageStatusTopicControllerTest {

    @MockBean
    private MessageStatusTopicService messageStatusTopicService;

    @Autowired
    private MockMvc mockMvc;

    private MessageStatusTopicDto registeredMessageStatusTopicDto;

    private List<MessageStatusTopicDto> registeredMessageStatusTopicDtos = new ArrayList<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        registeredMessageStatusTopicDto = new MessageStatusTopicDto();
        registeredMessageStatusTopicDto.setId(1);
        registeredMessageStatusTopicDto.setMessageBusId(1);
        registeredMessageStatusTopicDto.setEncoding(MessageEncoding.JSON);
        registeredMessageStatusTopicDto.setName("topic1");
        registeredMessageStatusTopicDto.setSpecificationReference("specref");
        registeredMessageStatusTopicDtos.add(registeredMessageStatusTopicDto);
    }

    @Test
    void testCreateMessageStatusTopicEntry() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.createMessageStatusTopic(Mockito.any(MessageStatusTopicDto.class))).thenReturn(registeredMessageStatusTopicDto);
        String jsonString = "{\r\n" + "    \"messageBusId\": 1,\r\n" + "    \"name\": \"topic\",\r\n" + "    \"encoding\": \"JSON\",\r\n" + "    \"specificationReference\": \"specref1\"\r\n" + "}";
        this.mockMvc.perform(post("/v1/message-status-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isCreated());
    }

    @Test
    void testCreateMessageStatusTopicEntryWithOneFieldMissing() throws JsonProcessingException, Exception {
        String jsonString = "{\r\n" + "    \"name\": \"topic\",\r\n" + "    \"encoding\": \"JSON\",\r\n" + "    \"specificationReference\": \"specref1\"\r\n" + "}";
        this.mockMvc.perform(post("/v1/message-status-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageStatusTopicEntryWithMandatoryParamNameMissing() throws JsonProcessingException, Exception {
        registeredMessageStatusTopicDto.setName(null);
        this.mockMvc.perform(post("/v1/message-status-topic").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registeredMessageStatusTopicDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageStatusTopicEntryWithMandatoryParamMessageBusIdMissing() throws JsonProcessingException, Exception {
        registeredMessageStatusTopicDto.setMessageBusId(null);
        this.mockMvc.perform(post("/v1/message-status-topic").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registeredMessageStatusTopicDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageStatusTopicEntryWithMandatoryParamsMissing() throws JsonProcessingException, Exception {
        registeredMessageStatusTopicDto.setMessageBusId(null);
        registeredMessageStatusTopicDto.setName(null);
        this.mockMvc.perform(post("/v1/message-status-topic").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(registeredMessageStatusTopicDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageStatusTopicEntryWithDuplicateValue() throws JsonProcessingException, Exception {
        String jsonString = "{\r\n" + "    \"messageBusId\": 1,\r\n" + "    \"name\": \"topic\",\r\n" + "    \"encoding\": \"JSON\",\r\n" + "    \"specificationReference\": \"specref1\"\r\n" + "}";
        this.mockMvc.perform(post("/v1/message-status-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isConflict());
    }

    @Test
    void testGetMessageStatusTopicById() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getMessageStatusTopicById(Mockito.anyInt())).thenReturn(registeredMessageStatusTopicDto);
        this.mockMvc.perform(get("/v1/message-status-topic/{id}", registeredMessageStatusTopicDto.getId())).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(registeredMessageStatusTopicDto)));
    }

    @Test
    void testGetUnavailableMessageStatusTopicById() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getMessageStatusTopicById(Mockito.anyInt())).thenReturn(null);
        this.mockMvc.perform(get("/v1/message-status-topic/{id}", registeredMessageStatusTopicDto.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMessageStatusTopics() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getAllMessageStatusTopics()).thenReturn(java.util.List.of(registeredMessageStatusTopicDto));
        this.mockMvc.perform(get("/v1/message-status-topic")).andExpect(status().isOk());
    }

    @Test
    void testGetbyQueryParams() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getMessageStatusTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageStatusTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageStatusTopicDto.getName());
        requestParams.add("messageBusId", Integer.toString(registeredMessageStatusTopicDto.getMessageBusId()));
        this.mockMvc.perform(get("/v1/message-status-topic").queryParams(requestParams)).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(registeredMessageStatusTopicDto)));
    }

    @Test
    void testGetMessageStatusTopicWithNameParamMissing() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getMessageStatusTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageStatusTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("messageBusId", Integer.toString(registeredMessageStatusTopicDto.getMessageBusId()));
        this.mockMvc.perform(get("/v1/message-status-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetMessageStatusTopicWithMessageBusIdParamMissing() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getMessageStatusTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageStatusTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageStatusTopicDto.getName());
        this.mockMvc.perform(get("/v1/message-status-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetMessageStatusTopicWithExtraParams() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getMessageStatusTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageStatusTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageStatusTopicDto.getName());
        requestParams.add("messageBusId", Integer.toString(registeredMessageStatusTopicDto.getMessageBusId()));
        requestParams.add("speificationReference", registeredMessageStatusTopicDto.getSpecificationReference());
        this.mockMvc.perform(get("/v1/message-status-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetbyQueryParamsWithNonExistingValues() throws JsonProcessingException, Exception {
        when(messageStatusTopicService.getMessageStatusTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(null);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageStatusTopicDto.getName());
        requestParams.add("messageBusId", Integer.toString(registeredMessageStatusTopicDto.getMessageBusId()));
        this.mockMvc.perform(get("/v1/message-status-topic").queryParams(requestParams)).andExpect(status().isNotFound());
    }

    @Test
    void testbyIdNumberFormatExceptionTest() throws Exception {
        this.mockMvc.perform(get("/v1/message-status-topic/{id}", "test")).andExpect(status().isBadRequest());
    }

}
