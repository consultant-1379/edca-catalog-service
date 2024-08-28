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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import com.ericsson.oss.edca.catalog.controller.dto.DataCollectorDto;
import com.ericsson.oss.edca.catalog.service.DataCollectorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { DataCollectorController.class })
@AutoConfigureMockMvc
public class DataCollectorControllerTest {

    @MockBean
    private DataCollectorService dataCollectorService;

    @Autowired
    MockMvc mockMvc;

    private DataCollectorDto registeredDataCollectorDto = new DataCollectorDto();

    private List<DataCollectorDto> registeredDataCollectorDtos = new ArrayList<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        registeredDataCollectorDto.setCollectorId(UUID.randomUUID());
        try {
            registeredDataCollectorDto.setControlEndpoint(new URL("http://1.1.1.1:9000/end_point"));
        } catch (final MalformedURLException e) {
            // ignored
        }
        registeredDataCollectorDto.setName("data_collector_1");
        registeredDataCollectorDtos.add(registeredDataCollectorDto);

    }

    @org.junit.jupiter.api.Test
    @Order(1)
    public void testRegisterDataCollectorSuccess() throws Exception {
        registeredDataCollectorDto.setCollectorId(UUID.randomUUID());
        Mockito.when(dataCollectorService.saveDataCollector(Mockito.any(DataCollectorDto.class))).thenReturn(registeredDataCollectorDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/data-collector").contentType(MediaType.APPLICATION_JSON)
                        .content(new String("{\r\n" + "    \"collectorId\": \"" + UUID.randomUUID() + " \",\r\n"
                                + "    \"controlEndpoint\": \"http://1.1.1.1:9000/end_point\",\r\n" + "    \"name\": \"data_collector_1\"\r\n" + "}")
                                        .getBytes()))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.collectorId").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.controlEndpoint").exists()).andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andDo(MockMvcResultHandlers.print());

    }

    @org.junit.jupiter.api.Test
    @Order(2)
    public void testRegisterDataCollectorRequiredParamsNotProvided() throws Exception {
        registeredDataCollectorDto.setControlEndpoint(null);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/data-collector").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(registeredDataCollectorDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists()).andDo(MockMvcResultHandlers.print());
    }

}
