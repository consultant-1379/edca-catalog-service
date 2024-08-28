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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaResponseDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageDataTopicDto;
import com.ericsson.oss.edca.catalog.controller.dto.DataCollectorDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageBusDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageStatusTopicDto;
import com.ericsson.oss.edca.catalog.controller.dto.DataSpaceDto;
import com.ericsson.oss.edca.catalog.controller.dto.DataProviderTypeDto;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.service.MessageSchemaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { MessageSchemaController.class })
class MessageSchemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageSchemaService messageSchemaService;

    private final List<MessageSchemaResponseDto> persistedMessageSchemas = new ArrayList<>();

    private final ObjectMapper mapper = new ObjectMapper();

    private MessageSchemaDto messageSchemaToRegister = null;

    private MessageSchemaResponseDto messageSchemaDto;

    @BeforeEach
    public void setup() throws MalformedURLException {

        messageSchemaDto = new MessageSchemaResponseDto();
        messageSchemaDto.setId(1);
        messageSchemaDto.setSpecificationReference("sr");

        final DataSpaceDto dataSpace = new DataSpaceDto();
        dataSpace.setId(1);
        dataSpace.setName("dataspace_name");

        final MessageBusDto messageBus = new MessageBusDto();
        messageBus.setId(1);
        messageBus.setName("messageBus100");
        messageBus.setClusterName("clusterName1");
        messageBus.setNameSpace("NameSpace1");
        messageBus.setAccessEndpoints(Set.of("msg_bus_access_endpoint"));

        final DataCollectorDto dataCollector = new DataCollectorDto();
        dataCollector.setId(1);
        dataCollector.setName("data_collector");
        dataCollector.setCollectorId(UUID.randomUUID());
        dataCollector.setControlEndpoint(new URL("http://1.1.1.1:9000/end_point"));
        messageSchemaDto.setDataCollector(dataCollector);

        final DataProviderTypeDto dataProviderType = new DataProviderTypeDto();
        dataProviderType.setId(1);
        dataProviderType.setDataCategory(DataCategory.CM_EXPORT);
        dataProviderType.setProviderTypeId("pvid_1");
        dataProviderType.setProviderVersion("pv_version_1");
        dataProviderType.setDataSpace(dataSpace);
        messageSchemaDto.setDataProviderType(dataProviderType);

        final MessageStatusTopicDto messageStatusTopic = new MessageStatusTopicDto();
        messageStatusTopic.setEncoding(MessageEncoding.JSON);
        messageStatusTopic.setId(1);
        messageStatusTopic.setMessageBus(messageBus);
        messageStatusTopic.setName("status-topic");
        messageStatusTopic.setSpecificationReference("spec-ref");

        final MessageDataTopicDto messageDataTopic = new MessageDataTopicDto();
        messageDataTopic.setEncoding(MessageEncoding.JSON);
        messageDataTopic.setId(101);
        messageDataTopic.setName("topic1");
        messageDataTopic.setMessageStatusTopic(messageStatusTopic);
        messageDataTopic.setMessageBus(messageBus);
        messageDataTopic.setMessageStatusTopic(messageStatusTopic);
        messageSchemaDto.setMessageDataTopic(messageDataTopic);

        persistedMessageSchemas.add(messageSchemaDto);

        messageSchemaToRegister = new MessageSchemaDto();
        messageSchemaToRegister.setDataCollectorId(dataCollector.getCollectorId());
        messageSchemaToRegister.setDataProviderTypeId(1);
        messageSchemaToRegister.setMessageDataTopicId(1);
        messageSchemaToRegister.setSpecificationReference("");
    }

    @Order(1)
    @Test
    public void testRegisterMessageSchemaSuccess() throws JsonProcessingException, Exception {
        Mockito.when(messageSchemaService.saveMessageSchema(Mockito.any(MessageSchemaDto.class))).thenReturn(persistedMessageSchemas.get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/message-schema").content(mapper.writeValueAsString(messageSchemaToRegister)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Order(2)
    @Test
    public void testRegisterMessageSchemaWhenInvalidRequest() throws JsonProcessingException, Exception {
        Mockito.doThrow(new CatalogException(CatalogConstants.MESSAGESCHEMA_DATAPROVIDERTYPE_NOT_FOUND_MSG)).when(messageSchemaService).saveMessageSchema(Mockito.any(MessageSchemaDto.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/message-schema").content(mapper.writeValueAsString(messageSchemaToRegister)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Order(3)
    @Test
    public void testQueryAllMessageSchemaSuccess() throws JsonProcessingException, Exception {
        Mockito.when(messageSchemaService.getAllMessageSchema()).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Order(4)
    @Test
    public void testQueryAllMessageSchemaByDataSpaceAndDataProviderTypeSuccess() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataProviderType", "pvid_1");
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(5)
    @Test
    public void testQueryAllMessageSchemaByDataSpaceAndDataCategorySuccess() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(6)
    @Test
    public void testQueryAllMessageSchemaByDataProviderTypeAndDataCategoryInvalid() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataProviderType", "pvid_1");
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(7)
    @Test
    public void testQueryAllMessageSchemaByTopic() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("topic", "::CM_EXPORT");
        Mockito.when(messageSchemaService.getMessageSchemaByTopic(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(8)
    @Test
    public void testQueryAllMessageSchemaByInvalidTopic() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("topic", ":::");
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(9)
    @Test
    public void testQueryAllMessageSchemaByInvalidExtraQueryParam() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        queryParams.add("extra_param", "extra_param_val");
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(10)
    @Test
    public void testQueryMessageSchemaById() throws JsonProcessingException, Exception {

        Mockito.when(messageSchemaService.getMessageSchemaById(Mockito.any(String.class))).thenReturn(persistedMessageSchemas.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema/1").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(11)
    @Test
    public void testQueryMessageSchemaByInvalidNonIntegerId() throws JsonProcessingException, Exception {

        Mockito.doThrow(NumberFormatException.class).when(messageSchemaService).getMessageSchemaById(Mockito.anyString());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema/10x").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(12)
    @Test
    public void testQueryMessageSchemaByIdNotExists() throws JsonProcessingException, Exception {

        Mockito.doReturn(null).when(messageSchemaService).getMessageSchemaById(Mockito.anyString());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema/10").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(13)
    @Test
    public void testRegisterMessageSchemaWhenMissingRequiredFields() throws JsonProcessingException, Exception {
        messageSchemaToRegister.setDataCollectorId(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/message-schema").content(mapper.writeValueAsString(messageSchemaToRegister)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(14)
    public void testQueryAllMessageSchemaWhenNullCollectionReturned() throws Exception {
        Mockito.when(messageSchemaService.getAllMessageSchema()).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-collector").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(15)
    public void testQueryAllMessageSchemaWhenEmptyCollectionReturned() throws Exception {
        Mockito.when(messageSchemaService.getAllMessageSchema()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-collector").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(16)
    public void testInvalidMultipleParams() throws Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        queryParams.add("dataProviderType", "pvid_1");
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(17)
    @Test
    void testGetUnavailableMessageSchemaTopicByQueryParams() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "2G");
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(new ArrayList<MessageSchemaResponseDto>());
        this.mockMvc.perform(get("/v1/message-schema").queryParams(queryParams)).andExpect(status().isNotFound());
    }

    @Order(18)
    @Test
    void testGetByQueryParamsUsingInvalidDataCategory() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataCategory", "dataCatefory");
        this.mockMvc.perform(get("/v1/message-schema").queryParams(queryParams)).andExpect(status().isBadRequest());
    }

    @Order(19)
    @Test
    public void testQueryAllMessageSchemaByDataSpaceSuccess() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(20)
    @Test
    public void testQueryAllMessageSchemaByDataCategorySuccess() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(persistedMessageSchemas);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(21)
    @Test
    public void testQueryUnavailableMessageSchemaByTopic() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("topic", "pvid_1:pv1:PM_STATS");
        Mockito.when(messageSchemaService.getMessageSchemaByQueryParams(queryParams.toSingleValueMap())).thenReturn(new ArrayList<MessageSchemaResponseDto>());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/message-schema").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
