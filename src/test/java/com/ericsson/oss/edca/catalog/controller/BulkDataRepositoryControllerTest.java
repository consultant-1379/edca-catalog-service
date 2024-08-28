package com.ericsson.oss.edca.catalog.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.ericsson.oss.edca.catalog.controller.dto.BulkDataRepositoryDTO;
import com.ericsson.oss.edca.catalog.service.BulkDataRepositoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BulkDataRepositoryController.class)
public class BulkDataRepositoryControllerTest {

    @MockBean
    private BulkDataRepositoryService bdrService;

    @Autowired
    private MockMvc mockMvc;

    private BulkDataRepositoryDTO bdrDTO;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setupDTO() {
        bdrDTO = new BulkDataRepositoryDTO();
        bdrDTO.setId(101);
        bdrDTO.setName("test");
        bdrDTO.setNameSpace("testSpace");
        bdrDTO.setClusterName("testCluster");
        bdrDTO.setFileFormatIds(Stream.of(101, 102).collect(Collectors.toSet()));
        bdrDTO.setAccessEndpoints(Stream.of("test1", "test2").collect(Collectors.toSet()));
    }

    @Test
    void testSaveBDR() throws JsonProcessingException, Exception {
        when(bdrService.saveBDR(Mockito.any(BulkDataRepositoryDTO.class))).thenReturn(bdrDTO);
        this.mockMvc.perform(post("/v1/bulk-data-repository").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bdrDTO))).andExpect(status().isCreated());
    }

    @Test
    void testSaveBDRWithMandatoryParamsMissing() throws JsonProcessingException, Exception {
        bdrDTO.setNameSpace(null);
        this.mockMvc.perform(post("/v1/bulk-data-repository").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bdrDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void testSaveBDRWithExistingValue() throws JsonProcessingException, Exception {
        when(bdrService.saveBDR(Mockito.any(BulkDataRepositoryDTO.class))).thenThrow(DataIntegrityViolationException.class);
        this.mockMvc.perform(post("/v1/bulk-data-repository").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bdrDTO))).andExpect(status().isConflict());
    }

    @Test
    void testGetBDRById() throws JsonProcessingException, Exception {
        when(bdrService.getBDRById(Mockito.anyInt())).thenReturn(bdrDTO);
        this.mockMvc.perform(get("/v1/bulk-data-repository/{id}", bdrDTO.getId())).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(bdrDTO)));
    }

    @Test
    void testGetBDRWithNonAvailableId() throws JsonProcessingException, Exception {
        when(bdrService.getBDRById(Mockito.anyInt())).thenReturn(null);
        this.mockMvc.perform(get("/v1/bulk-data-repository/{id}", bdrDTO.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testGetAllBDR() throws JsonProcessingException, Exception {
        when(bdrService.getAllBDR()).thenReturn(java.util.List.of(bdrDTO));
        this.mockMvc.perform(get("/v1/bulk-data-repository")).andExpect(status().isOk());
    }

    @Test
    void testGetBDRbyQueryParams() throws JsonProcessingException, Exception {
        when(bdrService.getBDRByNameSpaceandName(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(bdrDTO));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", bdrDTO.getNameSpace());
        requestParams.add("name", bdrDTO.getName());
        this.mockMvc.perform(get("/v1/bulk-data-repository").queryParams(requestParams)).andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(List.of(bdrDTO))));
    }

    @Test
    void testGetBDRWithMissingNameParamMissing() throws JsonProcessingException, Exception {
        when(bdrService.getBDRByNameSpaceandName(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(bdrDTO));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", bdrDTO.getNameSpace());
        this.mockMvc.perform(get("/v1/bulk-data-repository").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetBDRWithExtraUnkownParams() throws JsonProcessingException, Exception {
        when(bdrService.getBDRByNameSpaceandName(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(bdrDTO));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", bdrDTO.getNameSpace());
        this.mockMvc.perform(get("/v1/bulk-data-repository").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetBDRWithMissingNameSpaceParamMissing() throws JsonProcessingException, Exception {
        when(bdrService.getBDRByNameSpaceandName(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(bdrDTO));
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", bdrDTO.getNameSpace());
        requestParams.add("name", bdrDTO.getName());
        requestParams.add("test", "tabc");
        this.mockMvc.perform(get("/v1/bulk-data-repository").queryParams(requestParams)).andExpect(status().isBadRequest());
    }

    @Test
    void testNonExistingBDREntryByNamsepaceAndNameTest() throws Exception {
        when(bdrService.getBDRByNameSpaceandName(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(Collections.emptyList());
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("nameSpace", bdrDTO.getNameSpace());
        requestParams.add("name", bdrDTO.getName());
        this.mockMvc.perform(get("/v1/bulk-data-repository").queryParams(requestParams)).andExpect(status().isNotFound());
    }

    @Test
    void testbyIdNumberFormatExceptionTest() throws Exception {
        this.mockMvc.perform(get("/v1/bulk-data-repository/{id}", "test")).andExpect(status().isBadRequest());
    }

    //    @Test
    //    void testsaveBDRwithInvalidBucketNameTest() throws Exception {
    //        bdrDTO.setName("testBDR");
    //        this.mockMvc.perform(post("/v1/bulk-data-repository").contentType(MediaType.APPLICATION_JSON)
    //                .content(mapper.writeValueAsString(bdrDTO))).andExpect(status().isBadRequest());
    //    }

}
