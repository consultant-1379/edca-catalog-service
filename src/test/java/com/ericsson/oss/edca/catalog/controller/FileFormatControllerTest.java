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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
import com.ericsson.oss.edca.catalog.controller.dto.BulkDataRepositoryDTO;
import com.ericsson.oss.edca.catalog.controller.dto.DataCollectorDto;
import com.ericsson.oss.edca.catalog.controller.dto.DataProviderTypeDto;
import com.ericsson.oss.edca.catalog.controller.dto.DataSpaceDto;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatDto;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatResponseDto;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.DataEncoding;
import com.ericsson.oss.edca.catalog.service.FileFormatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { FileFormatController.class })
public class FileFormatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileFormatService fileFormatService;

    private final List<FileFormatResponseDto> registeredFileFormats = new ArrayList<>();

    private final ObjectMapper mapper = new ObjectMapper();

    private FileFormatDto fileFormatToRegister = null;

    @BeforeEach
    public void setup() throws MalformedURLException {
        final FileFormatResponseDto fileFormatDto = new FileFormatResponseDto();
        final DataSpaceDto dataSpace = new DataSpaceDto();
        dataSpace.setId(1);
        dataSpace.setName("dataspace_name");
        final BulkDataRepositoryDTO bulkDataRepository = new BulkDataRepositoryDTO();
        bulkDataRepository.setId(1);
        bulkDataRepository.setName("bdr");
        bulkDataRepository.setNameSpace("bdr_ns");
        bulkDataRepository.setClusterName("bdr_cluster");
        bulkDataRepository.setAccessEndpoints(Set.of("bdr_endpoint"));
        bulkDataRepository.setFileFormatIds(Set.of(1));
        fileFormatDto.setBulkDataRepository(bulkDataRepository);
        final DataCollectorDto dataCollector = new DataCollectorDto();
        //dataCollector.setId(1);
        dataCollector.setName("data_collector");
        dataCollector.setCollectorId(UUID.randomUUID());
        dataCollector.setControlEndpoint(new URL("http://1.1.1.1:9000/end_point"));
        fileFormatDto.setDataCollector(dataCollector);
        fileFormatDto.setDataEncoding(DataEncoding.XML);
        final DataProviderTypeDto dataProviderType = new DataProviderTypeDto();
        dataProviderType.setId(1);
        dataProviderType.setDataCategory(DataCategory.CM_EXPORT);
        dataProviderType.setProviderTypeId("pvid_1");
        dataProviderType.setProviderVersion("pv_version_1");
        dataProviderType.setDataSpaceId(1);
        dataProviderType.setFileFormatIds(Set.of(1));
        dataSpace.setDataProviderTypeIds(Set.of(1));
        fileFormatDto.setDataProviderType(dataProviderType);
        fileFormatDto.setId(1);
        fileFormatDto.setReportOutputPeriodList(Set.of(15));
        fileFormatDto.setSpecificationReference("");
        registeredFileFormats.add(fileFormatDto);
        fileFormatToRegister = new FileFormatDto();
        fileFormatToRegister.setDataCollectorId(dataCollector.getCollectorId());
        fileFormatToRegister.setBulkDataRepositoryId(1);
        fileFormatToRegister.setDataProviderTypeId(1);
        fileFormatToRegister.setNotificationTopicId(1);
        fileFormatToRegister.setReportOutputPeriodList(Set.of(15));
        fileFormatToRegister.setSpecificationReference("");
        fileFormatToRegister.setDataEncoding(DataEncoding.XML);
    }

    @Order(1)
    @Test
    public void testRegisterFileFormatSuccess() throws JsonProcessingException, Exception {
        Mockito.when(fileFormatService.saveFileFormat(Mockito.any(FileFormatDto.class))).thenReturn(registeredFileFormats.get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/file-format").content(mapper.writeValueAsString(fileFormatToRegister))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Order(2)
    @Test
    public void testRegisterFileFormatWhenInvalidRequest() throws JsonProcessingException, Exception {
        Mockito.doThrow(new CatalogException(CatalogConstants.FILEFORMAT_DATAPROVIDERTYPE_NOT_FOUND_MSG)).when(fileFormatService)
                .saveFileFormat(Mockito.any(FileFormatDto.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/file-format").content(mapper.writeValueAsString(fileFormatToRegister))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(3)
    @Test
    public void testQueryAllFileFormatSuccess() throws JsonProcessingException, Exception {
        Mockito.when(fileFormatService.getAllFileFormats()).thenReturn(registeredFileFormats);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/file-format").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Order(4)
    @Test
    public void testQueryAllFileFormatByDataSpaceAndDataProviderTypeSuccess() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataProviderType", "pvid_1");
        Mockito.when(fileFormatService.getFileFormatByQueryParams(queryParams.toSingleValueMap())).thenReturn(registeredFileFormats);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/file-format").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Order(5)
    @Test
    public void testQueryAllFileFormatByDataSpaceAndDataCategorySuccess() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        Mockito.when(fileFormatService.getFileFormatByQueryParams(queryParams.toSingleValueMap())).thenReturn(registeredFileFormats);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/file-format").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Order(6)
    @Test
    public void testQueryAllFileFormatByInvalidExtraQueryParam() throws JsonProcessingException, Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("dataSpace", "4G");
        queryParams.add("dataCategory", DataCategory.CM_NOTIFICATIONS.name());
        queryParams.add("extra_param", "extra_param_val");
        Mockito.when(fileFormatService.getFileFormatByQueryParams(queryParams.toSingleValueMap())).thenReturn(registeredFileFormats);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/file-format").queryParams(queryParams).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Order(7)
    @Test
    public void testQueryFileFormatById() throws JsonProcessingException, Exception {

        Mockito.when(fileFormatService.getFileFormatById(Mockito.any(String.class))).thenReturn(registeredFileFormats.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/file-format/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Order(8)
    @Test
    public void testQueryFileFormatByInvalidNonIntegerId() throws JsonProcessingException, Exception {

        Mockito.doThrow(NumberFormatException.class).when(fileFormatService).getFileFormatById(Mockito.anyString());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/file-format/10x").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Order(9)
    @Test
    public void testQueryFileFormatByIdNotExists() throws JsonProcessingException, Exception {

        Mockito.doReturn(null).when(fileFormatService).getFileFormatById(Mockito.anyString());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/file-format/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());
    }

    @Order(10)
    @Test
    public void testRegisterFileFormatWhenMissingRequiredFields() throws JsonProcessingException, Exception {
        fileFormatToRegister.setDataCollectorId(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/file-format").content(mapper.writeValueAsString(fileFormatToRegister))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(11)
    public void testQueryAllFileFormatWhenNullCollectionReturned() throws Exception {
        Mockito.when(fileFormatService.getAllFileFormats()).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-collector").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    public void testQueryAllFileFormatrWhenEmptyCollectionReturned() throws Exception {
        Mockito.when(fileFormatService.getAllFileFormats()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/data-collector").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());
    }

}
