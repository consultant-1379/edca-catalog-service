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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import com.ericsson.oss.edca.catalog.controller.dto.MessageDataTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.service.MessageDataTopicService;
import com.fasterxml.jackson.core.JsonProcessingException;

@WebMvcTest(MessageDataTopicController.class)
@AutoConfigureMockMvc
public class MessageDataTopicControllerTest {

    @MockBean
    private MessageDataTopicService messageDataTopicService;

    @Autowired
    private MockMvc mockMvc;

    private MessageDataTopicDto registeredMessageDataTopicDto = new MessageDataTopicDto();

    private List<MessageDataTopicDto> registeredMessageDataTopicDtos = new ArrayList<>();

    @BeforeEach
    public void setup() {
        registeredMessageDataTopicDto.setId(1);
        registeredMessageDataTopicDto.setMessageBusId(1);
        registeredMessageDataTopicDto.setName("topic1");
        registeredMessageDataTopicDto.setMessageStatusTopicId(1);
        registeredMessageDataTopicDto.setEncoding(MessageEncoding.JSON);
        registeredMessageDataTopicDtos.add(registeredMessageDataTopicDto);
    }

    @Test
    void testCreateMessageDataTopicEntry() throws JsonProcessingException, Exception {
        when(messageDataTopicService.createMessageDataTopic(Mockito.any(MessageDataTopicDto.class))).thenReturn(registeredMessageDataTopicDto);
        String jsonString = "{\"messageBusId\":1,\"name\":\"msg-topic-20\",\"messageStatusTopicId\":1,\"encoding\":\"JSON\"}";
        this.mockMvc.perform(post("/v1/message-data-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isCreated());
    }

    @Test
    void testCreateMessageDataTopicEntryWithFieldMissing() throws JsonProcessingException, Exception {
        String jsonString = "{\"name\":\"msg-topic-20\",\"messageStatusTopicId\":1,\"encoding\":\"JSON\"}";
        this.mockMvc.perform(post("/v1/message-data-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageDataTopicEntryWithMandatoryParamsMissing() throws JsonProcessingException, Exception {
        registeredMessageDataTopicDto.setName(null);
        this.mockMvc.perform(post("/v1/message-data-topic").contentType(MediaType.APPLICATION_JSON).content(registeredMessageDataTopicDto.toString())).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageDataTopicEntryWithMandatoryParamsMissing2() throws JsonProcessingException, Exception {
        registeredMessageDataTopicDto.setMessageBusId(null);
        this.mockMvc.perform(post("/v1/message-data-topic").contentType(MediaType.APPLICATION_JSON).content(registeredMessageDataTopicDto.toString())).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageDataTopicEntryWithDuplicateValue() throws JsonProcessingException, Exception {
        String jsonString = "{\"messageBusId\":1,\"name\":\"msg-topic-20\",\"messageStatusTopicId\":1,\"encoding\":\"JSON\"}";
        this.mockMvc.perform(post("/v1/message-data-topic").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString())).andExpect(status().isConflict());
    }

    @Test
    void testGetMessageDataTopicById() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getMessageDataTopicById(Mockito.anyInt())).thenReturn(registeredMessageDataTopicDto);
        this.mockMvc.perform(get("/v1/message-data-topic/{id}", registeredMessageDataTopicDto.getId())).andExpect(status().isOk());
    }

    @Test
    void testGetUnavailableMessageDataTopicById() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getMessageDataTopicById(Mockito.anyInt())).thenReturn(null);
        this.mockMvc.perform(get("/v1/message-data-topic/{id}", registeredMessageDataTopicDto.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testGetAlltMessageDataTopics() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getAllMessageDataTopics()).thenReturn(java.util.List.of(registeredMessageDataTopicDto));
        this.mockMvc.perform(get("/v1/message-data-topic")).andExpect(status().isOk());
    }

    @Test
    void testGetbyQueryParams() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getMessageDataTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageDataTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageDataTopicDto.getName());
        requestParams.add("messageBusId", Integer.toString(registeredMessageDataTopicDto.getMessageBusId()));
        this.mockMvc.perform(get("/v1/message-data-topic").queryParams(requestParams)).andExpect(status().isOk());
    }

    @Test
    void testGetMessageDataTopicWithNameParamMissing() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getMessageDataTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageDataTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("messageBusId", Integer.toString(registeredMessageDataTopicDto.getMessageBusId()));
        this.mockMvc.perform(get("/v1/message-data-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetMessageDataTopicWithMessageBusIdParamMissing() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getMessageDataTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageDataTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageDataTopicDto.getName());
        this.mockMvc.perform(get("/v1/message-data-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetMessageDataTopicWithExtraParams() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getMessageDataTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(registeredMessageDataTopicDto);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageDataTopicDto.getName());
        requestParams.add("messageBusId", Integer.toString(registeredMessageDataTopicDto.getMessageBusId()));
        requestParams.add("speificationReference", Integer.toString(registeredMessageDataTopicDto.getMessageStatusTopicId()));
        this.mockMvc.perform(get("/v1/message-data-topic").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetbyQueryParamsWithNonExistingValues() throws JsonProcessingException, Exception {
        when(messageDataTopicService.getMessageDataTopicByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(null);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", registeredMessageDataTopicDto.getName());
        requestParams.add("messageBusId", Integer.toString(registeredMessageDataTopicDto.getMessageBusId()));
        this.mockMvc.perform(get("/v1/message-data-topic").queryParams(requestParams)).andExpect(status().isNotFound());
    }

    @Test
    void testbyIdNumberFormatExceptionTest() throws Exception {
        this.mockMvc.perform(get("/v1/message-data-topic/{id}", "test")).andExpect(status().isBadRequest());
    }

}
