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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import com.ericsson.oss.edca.catalog.controller.dto.DataSpaceDto;
import com.ericsson.oss.edca.catalog.service.DataspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { DataspaceController.class })
@AutoConfigureMockMvc
public class DataspaceControllerTest {

    @MockBean
    private DataspaceService dataspaceService;

    @Autowired
    MockMvc mockMvc;

    private DataSpaceDto registeredDataspaceDto = new DataSpaceDto();

    private List<DataSpaceDto> registeredDataSpaceDtos = new ArrayList<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        registeredDataspaceDto.setId(1);
        registeredDataspaceDto.setName("dataspace_name_1");
        registeredDataspaceDto.setDataProviderTypeIds(Stream.of(1).collect(Collectors.toSet()));
        registeredDataSpaceDtos.add(registeredDataspaceDto);

    }

    @org.junit.jupiter.api.Test
    @Order(1)
    public void testRegisterDataspaceSuccess() throws Exception {
        Mockito.when(dataspaceService.saveDataspace(Mockito.any(DataSpaceDto.class))).thenReturn(registeredDataspaceDto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/data-space").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataspaceDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    public void testRegisterDataspaceRequiredParamsNotProvided() throws Exception {
        registeredDataspaceDto.setName(null);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/data-space").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(registeredDataspaceDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(3)
    public void testQueryAllDataspacesSuccess() throws Exception {
        registeredDataspaceDto.setName("dataspace_name_1");
        Mockito.when(dataspaceService.getAllDataSpaces()).thenReturn(registeredDataSpaceDtos);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[ {\"id\": 1, \"name\": \"dataspace_name_1\",\"dataProviderTypeIds\": [1]} ]")).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(4)
    public void testQueryDataspaceByNameSuccess() throws Exception {
        Mockito.when(dataspaceService.getDataSpaceByName(Mockito.anyString())).thenReturn(registeredDataspaceDto);
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("name", "dataspace_name_1");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").queryParams(queryParams).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(5)
    public void testQueryDataspaceByIdSuccess() throws Exception {
        Mockito.when(dataspaceService.getDataSpaceById(Mockito.anyString())).thenReturn(registeredDataspaceDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space/1").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(6)
    public void testQueryDataspaceByIdNotExists() throws Exception {
        Mockito.when(dataspaceService.getDataSpaceById(Mockito.anyString())).thenReturn(null);
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("id", "2");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").queryParams(queryParams).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(7)
    public void testQueryDataspaceByNameNotExists() throws Exception {
        Mockito.when(dataspaceService.getDataSpaceByName(Mockito.anyString())).thenReturn(null);
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("name", "dataspace_name_not_exists");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").queryParams(queryParams).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(8)
    public void testQueryDataspaceByInvalidNonIntegerIdentifier() throws Exception {
        Mockito.doThrow(NumberFormatException.class).when(dataspaceService).getDataSpaceById(Mockito.anyString());
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("id", "10x");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").queryParams(queryParams).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(9)
    public void testQueryDataspaceByInvalidQueryParam() throws Exception {
        Mockito.doThrow(NumberFormatException.class).when(dataspaceService).getDataSpaceByName(Mockito.anyString());
        final LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("unknown_query_field", "unknown_query_val");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").queryParams(queryParams).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(10)
    public void testQueryAllDataSpaceTypeWhenNullCollectionReturned() throws Exception {
        Mockito.when(dataspaceService.getAllDataSpaces()).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(11)
    public void testQueryAllDataSpaceWhenEmptyCollectionReturned() throws Exception {
        Mockito.when(dataspaceService.getAllDataSpaces()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    public void testQueryDataspaceByNotExistsIdException() throws Exception {
        Mockito.when(dataspaceService.getDataSpaceById(Mockito.anyString())).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-space/2").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}
