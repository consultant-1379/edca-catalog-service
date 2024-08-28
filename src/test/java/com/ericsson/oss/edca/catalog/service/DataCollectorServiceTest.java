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
package com.ericsson.oss.edca.catalog.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ericsson.oss.edca.catalog.controller.dto.DataCollectorDto;
import com.ericsson.oss.edca.catalog.domain.DataCollector;
import com.ericsson.oss.edca.catalog.domain.utility.DataCollectorMapper;
import com.ericsson.oss.edca.catalog.repository.DataCollectorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class DataCollectorServiceTest {

    @Mock
    protected DataCollectorRepository dataCollectorRepository;

    @InjectMocks
    private DataCollectorService dataCollectorService;

    private DataCollector persistedDataCollectorEntity = new DataCollector();

    private List<DataCollector> persistedDataCollectorEntities = new ArrayList<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        persistedDataCollectorEntity.setId(1);
        persistedDataCollectorEntity.setCollectorId(UUID.randomUUID().toString());
        try {
            persistedDataCollectorEntity.setControlEndpoint(new URL("http://1.1.1.1:9000/end_point"));
        } catch (final MalformedURLException e) {
            // ignored
        }
        persistedDataCollectorEntity.setName("data_collector_1");
        persistedDataCollectorEntities.add(persistedDataCollectorEntity);
    }

    @Test
    public void testSaveDataCollectorSuccess() throws Exception {
        Mockito.when(dataCollectorRepository.save(Mockito.any(DataCollector.class))).thenReturn(persistedDataCollectorEntity);
        
        assertNotNull(dataCollectorService.saveDataCollector(DataCollectorMapper.INSTANCE.destinationToSource(persistedDataCollectorEntity)));
    }

    @Test
    public void testGetAllDataCollectorSuccess() throws Exception {
        Mockito.when(dataCollectorRepository.findAll()).thenReturn(persistedDataCollectorEntities);
        assertNotNull(dataCollectorService.getAllDataCollector());
        dataCollectorService.getAllDataCollector().forEach((final DataCollectorDto dataCollectorDto) -> {
            assertNotNull(dataCollectorDto.getCollectorId());
            assertNotNull(dataCollectorDto.getName());
            assertNotNull(dataCollectorDto.getControlEndpoint());
        });
    }

}
