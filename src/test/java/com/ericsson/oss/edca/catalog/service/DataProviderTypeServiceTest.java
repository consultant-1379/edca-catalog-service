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
package com.ericsson.oss.edca.catalog.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.DataProviderTypeDto;
import com.ericsson.oss.edca.catalog.domain.*;
import com.ericsson.oss.edca.catalog.repository.DataProviderTypeRepository;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;

@ExtendWith(MockitoExtension.class)
public class DataProviderTypeServiceTest {

    @Mock
    private DataProviderTypeRepository dataProviderTypeRepository;

    @Mock
    private DataSpaceRepository dataSpaceRepository;

    @InjectMocks
    private DataProviderTypeService dataProviderTypeService;

    private DataProviderType persistedDataProviderTypeEntity = new DataProviderType();

    private DataProviderTypeDto persistedDataProviderTypeEntityDto = new DataProviderTypeDto();

    private List<DataProviderType> persistedDataProviderTypeEntities = new ArrayList<>();

    @BeforeEach
    public void setup() {
        persistedDataProviderTypeEntity.setId(1);
        final DataSpace dataSpace = new DataSpace();
        dataSpace.setId(1);
        dataSpace.setName("data_space_name");
        persistedDataProviderTypeEntity.setDataSpace(dataSpace);
        persistedDataProviderTypeEntity.setDataCategory(DataCategory.CM_EXPORT);
        persistedDataProviderTypeEntity.setProviderTypeId("provider_type_id_1");
        persistedDataProviderTypeEntity.setProviderVersion("provider_type_vesion_1");
        persistedDataProviderTypeEntity.setFileFormats(new HashSet<>());
        persistedDataProviderTypeEntity.setMessageSchemas(new HashSet<>());
        persistedDataProviderTypeEntities.add(persistedDataProviderTypeEntity);
        persistedDataProviderTypeEntityDto.setId(1);
        persistedDataProviderTypeEntityDto.setDataCategory(DataCategory.CM_EXPORT);
        persistedDataProviderTypeEntityDto.setProviderTypeId("pv1");
        persistedDataProviderTypeEntityDto.setProviderVersion("pv");
        persistedDataProviderTypeEntityDto.setDataSpaceId(1);
    }

    @Test
    public void testSaveDataProviderTypeSuccess() throws Exception {
        Mockito.when(dataProviderTypeRepository.save(Mockito.any(DataProviderType.class))).thenReturn(persistedDataProviderTypeEntity);
        Mockito.when(dataSpaceRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedDataProviderTypeEntity.getDataSpace()));
        final DataProviderTypeDto receivedDataProviderTypeDto = new DataProviderTypeDto();
        receivedDataProviderTypeDto.setDataCategory(DataCategory.CM_EXPORT);
        receivedDataProviderTypeDto.setDataSpaceId(1);
        receivedDataProviderTypeDto.setProviderTypeId("provider_type_id_1");
        receivedDataProviderTypeDto.setProviderVersion("provider_type_vesion_1");
        //convertEntityToModel(persistedDataProviderTypeEntity, receivedDataProviderTypeDto);
        assertNotNull(dataProviderTypeService.saveDataProviderType(receivedDataProviderTypeDto));
    }

    @Test
    public void testAGetAllDataProviderTypesSuccess() throws Exception {

        Mockito.when(dataProviderTypeRepository.findAll()).thenReturn(persistedDataProviderTypeEntities);
        dataProviderTypeService.getAllDataProviderTypes().forEach((final DataProviderTypeDto dataProviderTypeDto) -> {
            assertTrue(Arrays.asList(DataCategory.values()).contains(dataProviderTypeDto.getDataCategory()));
            assertNotNull(dataProviderTypeDto.getDataSpaceId());
            assertNotNull(dataProviderTypeDto.getFileFormatIds());
            assertNotNull(dataProviderTypeDto.getMessageSchemaIds());
            assertNotNull(dataProviderTypeDto.getId());
            assertNotNull(dataProviderTypeDto.getProviderTypeId());
            assertNotNull(dataProviderTypeDto.getProviderVersion());
        });
    }

    @Test
    public void testGetAllDataProviderTypesByDataspaceSuccess() throws Exception {
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(Mockito.anyString())).thenReturn(persistedDataProviderTypeEntities);
        dataProviderTypeService.getAllDataProviderTypesByDataspace("data_space_name").forEach((final DataProviderTypeDto dataProviderTypeDto) -> {
            assertTrue(Arrays.asList(DataCategory.values()).contains(dataProviderTypeDto.getDataCategory()));
            assertTrue(dataProviderTypeDto.getDataSpaceId() != null && dataProviderTypeDto.getDataSpaceId().equals(persistedDataProviderTypeEntity.getDataSpaceId()));
            assertNotNull(dataProviderTypeDto.getFileFormatIds());
            assertNotNull(dataProviderTypeDto.getMessageSchemaIds());
            assertNotNull(dataProviderTypeDto.getId());
            assertNotNull(dataProviderTypeDto.getProviderTypeId());
            assertNotNull(dataProviderTypeDto.getProviderVersion());
        });
    }

    @Test
    public void testGetAllDataProviderTypesByDataspaceNotFound() throws Exception {
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(Mockito.anyString())).thenReturn(List.of());
        assertTrue(dataProviderTypeService.getAllDataProviderTypesByDataspace("data_space_name").size() == 0);
    }

    @Test
    public void testGetDataProviderTypeByIdSuccess() throws Exception {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedDataProviderTypeEntity));
        assertNotNull(dataProviderTypeService.getDataProviderTypeById("1"));
    }

    @Test
    public void testGetDataProviderTypeByInvalidNonIntegerId() throws Exception {
        assertThrows(NumberFormatException.class, () -> {
            dataProviderTypeService.getDataProviderTypeById("10x");
        });
    }

    @Test
    public void testPostDataProviderTypeByNonExistingdataSpace() throws Exception {
        Exception exception = assertThrows(CatalogException.class, () -> {
            dataProviderTypeService.saveDataProviderType(persistedDataProviderTypeEntityDto);
        });
        assertEquals("dataSpace not found for dataProviderType", exception.getMessage());
    }

}
