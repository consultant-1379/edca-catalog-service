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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import com.ericsson.oss.edca.catalog.controller.dto.MessageBusDto;
import com.ericsson.oss.edca.catalog.service.MessageBusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MessageBusController.class)
public class MessageBusControllerTest {

    @MockBean
    private MessageBusService msgService;

    @Autowired
    private MockMvc mockMvc;

    private MessageBusDto msgBusDto;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setupDTO() {
        msgBusDto = new MessageBusDto();
        msgBusDto.setId(101);
        msgBusDto.setName("test");
        msgBusDto.setNameSpace("testSpace");
        msgBusDto.setClusterName("testCluster");
        msgBusDto.setNotificationTopicIds(Stream.of(101, 102).collect(Collectors.toSet()));
        msgBusDto.setMessageStatusTopicIds(Stream.of(101, 102).collect(Collectors.toSet()));
        msgBusDto.setMessageDataTopicIds(Stream.of(101, 102).collect(Collectors.toSet()));
        msgBusDto.setAccessEndpoints(Stream.of("test1", "test2").collect(Collectors.toSet()));
    }

    @Test
    void testCreateMessageBusEntry() throws JsonProcessingException, Exception {
        when(msgService.createMessageBus(Mockito.any(MessageBusDto.class))).thenReturn(msgBusDto);
        this.mockMvc.perform(post("/v1/message-bus").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(msgBusDto))).andExpect(status().isCreated());
    }

    @Test
    void testCreateMessageBusEntryWithMandatoryParamsMissing() throws JsonProcessingException, Exception {
        msgBusDto.setNameSpace(null);
        this.mockMvc.perform(post("/v1/message-bus").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(msgBusDto))).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageBusEntryWithExistingValue() throws JsonProcessingException, Exception {
        when(msgService.createMessageBus(Mockito.any(MessageBusDto.class))).thenThrow(DataIntegrityViolationException.class);
        this.mockMvc.perform(post("/v1/message-bus").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(msgBusDto))).andExpect(status().isConflict());
    }

    @Test
    void testGetMessageBusEntryById() throws JsonProcessingException, Exception {
        when(msgService.getMessageBusById(Mockito.anyInt())).thenReturn(msgBusDto);
        this.mockMvc.perform(get("/v1/message-bus/{id}", msgBusDto.getId())).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(msgBusDto)));
    }

    @Test
    void testGetMessageBusEntryWithNonAvailableId() throws JsonProcessingException, Exception {
        when(msgService.getMessageBusById(Mockito.anyInt())).thenReturn(null);
        this.mockMvc.perform(get("/v1/message-bus/{id}", msgBusDto.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMessageBusEntries() throws JsonProcessingException, Exception {
        when(msgService.getMessageBuses()).thenReturn(java.util.List.of(msgBusDto));
        this.mockMvc.perform(get("/v1/message-bus")).andExpect(status().isOk());
    }

    @Test
    void testGetMessageBusEntriesbyQueryParams() throws JsonProcessingException, Exception {
        when(msgService.getMessageBusByNameAndNamespace(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(msgBusDto));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", msgBusDto.getNameSpace());
        requestParams.add("name", msgBusDto.getName());
        this.mockMvc.perform(get("/v1/message-bus").queryParams(requestParams)).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(List.of(msgBusDto))));
    }

    @Test
    void testGetMessageBusEntriesWithMissingNameParam() throws JsonProcessingException, Exception {
        when(msgService.getMessageBusByNameAndNamespace(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(msgBusDto));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", msgBusDto.getNameSpace());
        this.mockMvc.perform(get("/v1/message-bus").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetMessageBusEntriesWithMissingNameSpaceParam() throws JsonProcessingException, Exception {
        when(msgService.getMessageBusByNameAndNamespace(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(msgBusDto));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", msgBusDto.getName());
        this.mockMvc.perform(get("/v1/message-bus").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetmsgBusWithextraParams() throws JsonProcessingException, Exception {
        when(msgService.getMessageBusByNameAndNamespace(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(msgBusDto));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", msgBusDto.getNameSpace());
        requestParams.add("name", msgBusDto.getName());
        requestParams.add("test", "tabc");
        this.mockMvc.perform(get("/v1/message-bus").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetMessageBusEntryusingQueryParamWithNonExistingValue() throws Exception {
        when(msgService.getMessageBusByNameAndNamespace(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(Collections.emptyList());
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", msgBusDto.getNameSpace());
        requestParams.add("name", msgBusDto.getName());
        this.mockMvc.perform(get("/v1/message-bus").queryParams(requestParams)).andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMessageBusEntryWithNumberFormatExceptionTest() throws Exception {
        this.mockMvc.perform(get("/v1/message-bus/{id}", "test")).andExpect(status().isBadRequest());
    }

}
