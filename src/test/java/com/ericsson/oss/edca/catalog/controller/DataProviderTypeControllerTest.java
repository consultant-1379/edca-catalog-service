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

import java.util.*;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.controller.dto.DataProviderTypeDto;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.service.DataProviderTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { DataProviderTypeController.class })
@AutoConfigureMockMvc
public class DataProviderTypeControllerTest {

    @MockBean
    private DataProviderTypeService dataProviderTypeService;

    @Autowired
    private MockMvc mockMvc;

    private DataProviderTypeDto registeredDataProviderTypeDto = new DataProviderTypeDto();

    private List<DataProviderTypeDto> registeredDataProviderTypeDtos = new ArrayList<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        registeredDataProviderTypeDto.setDataCategory(DataCategory.CM_NOTIFICATIONS);
        registeredDataProviderTypeDto.setDataSpaceId(1);
        registeredDataProviderTypeDto.setProviderTypeId("provider_type_id_1");
        registeredDataProviderTypeDto.setProviderVersion("provider_type_version_1");
        registeredDataProviderTypeDto.setFileFormatIds(new HashSet<>());
        registeredDataProviderTypeDto.setMessageSchemaIds(new HashSet<>());
        registeredDataProviderTypeDtos.add(registeredDataProviderTypeDto);
    }

    @Test
    public void testRegisterDataProviderTypeSuccess() throws Exception {
        registeredDataProviderTypeDto.setId(1);
        Mockito.when(dataProviderTypeService.saveDataProviderType(Mockito.any(DataProviderTypeDto.class))).thenReturn(registeredDataProviderTypeDto);
        String jsonString = "{\r\n" + "\"providerVersion\": \"pv1\",\r\n" + "\"dataCategory\": \"CM_EXPORT\",\r\n" + "\"providerTypeId\": \"pvid_1\",\r\n" + "\"dataSpaceId\": 1\r\n" + "\r\n" + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/data-provider-type").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString()))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(1)
    public void testRegisterDataProviderTypeMandatoryFieldsNull() throws Exception {
        registeredDataProviderTypeDto.setProviderTypeId(null);
        registeredDataProviderTypeDto.setProviderVersion(null);
        registeredDataProviderTypeDto.setDataCategory(null);
        Mockito.when(dataProviderTypeService.saveDataProviderType(Mockito.any(DataProviderTypeDto.class))).thenReturn(registeredDataProviderTypeDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/data-provider-type").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataProviderTypeDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue())).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(2)
    public void testRegisterDataProviderTypeDataspaceNotFound() throws Exception {
        Mockito.doThrow(new HttpMessageNotReadableException(CatalogConstants.DATAPROVIDERTYPE_DATASPACE_NOT_FOUND_MSG)).when(dataProviderTypeService)
                .saveDataProviderType(Mockito.any(DataProviderTypeDto.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/data-provider-type").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataProviderTypeDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue())).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(3)
    public void testRegisterDataProviderTypeDataProviderTypeExists() throws Exception {
        Mockito.doThrow(DataIntegrityViolationException.class).when(dataProviderTypeService).saveDataProviderType(Mockito.any(DataProviderTypeDto.class));
        String jsonString = "{\r\n" + "\"providerVersion\": \"pv1\",\r\n" + "\"dataCategory\": \"CM_EXPORT\",\r\n" + "\"providerTypeId\": \"pvid_1\",\r\n" + "\"dataSpaceId\": 1\r\n" + "\r\n" + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/data-provider-type").contentType(MediaType.APPLICATION_JSON).content(new JSONObject(jsonString).toString()))
                .andExpect(MockMvcResultMatchers.status().isConflict()).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue())).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(4)
    public void testQueryDataProviderTypeWhenIdNotExists() throws Exception {
        Mockito.when(dataProviderTypeService.getDataProviderTypeById(Mockito.anyString())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-provider-type/2").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataProviderTypeDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue())).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(5)
    public void testQueryDataProviderTypeWhenInvalidIdentifier() throws Exception {
        Mockito.doThrow(NumberFormatException.class).when(dataProviderTypeService).getDataProviderTypeById(Mockito.anyString());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-provider-type/1x").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataProviderTypeDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue())).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(6)
    public void testQueryDataProviderTypeWhenDataspaceNotExists() throws Exception {
        Mockito.when(dataProviderTypeService.getAllDataProviderTypesByDataspace(Mockito.anyString())).thenReturn(null);
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("dataSpace", "dataspace_not_exists");
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/data-provider-type").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataProviderTypeDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue())).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(7)
    public void testQueryDataProviderTypeWhenInvalidQueryParamProvided() throws Exception {
        Mockito.when(dataProviderTypeService.getAllDataProviderTypesByDataspace(Mockito.anyString())).thenReturn(null);
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("invalid-dataprovider-type-param", "param-val");
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/data-provider-type").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataProviderTypeDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp", Matchers.notNullValue())).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(8)
    public void testQueryAllDataProviderTypeWhenNullCollectionReturned() throws Exception {
        Mockito.when(dataProviderTypeService.getAllDataProviderTypes()).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-provider-type").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(9)
    public void testQueryAllDataProviderTypeWhenEmptyCollectionReturned() throws Exception {
        Mockito.when(dataProviderTypeService.getAllDataProviderTypes()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-provider-type").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(10)
    public void testQueryAllDataProviderTypeUsingValidQueryParams() throws Exception {
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("dataSpace", "test");
        Mockito.when(dataProviderTypeService.getAllDataProviderTypesByDataspace(Mockito.any(String.class))).thenReturn(Arrays.asList(registeredDataProviderTypeDto));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-provider-type").queryParams(queryParams).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(11)
    public void testQueryAllDataProviderTypeData() throws Exception {
        Mockito.when(dataProviderTypeService.getAllDataProviderTypes()).thenReturn(Arrays.asList(registeredDataProviderTypeDto));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-provider-type").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(12)
    public void testQueryAllDataProviderTypeUsingExistingId() throws Exception {
        Mockito.when(dataProviderTypeService.getDataProviderTypeById(Mockito.any(String.class))).thenReturn(registeredDataProviderTypeDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-provider-type/1").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
