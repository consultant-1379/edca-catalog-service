/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ericsson.oss.edca.catalog.controller.dto.DataSpaceDto;
import com.ericsson.oss.edca.catalog.domain.DataSpace;
import com.ericsson.oss.edca.catalog.domain.utility.DataSpaceMapper;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;

@ExtendWith(MockitoExtension.class)
public class DataspaceServiceTest {

    @Mock
    protected DataSpaceRepository dataspaceRespository;

    @InjectMocks
    protected DataspaceService dataspaceService;

    private DataSpace persistedDataspaceEntity = new DataSpace();

    private List<DataSpace> persistedDataspaceEntities = new ArrayList<>();

    @BeforeEach
    public void setup() {
        persistedDataspaceEntity.setId(1);
        persistedDataspaceEntity.setName("dataspace_name_1");
        persistedDataspaceEntities.add(persistedDataspaceEntity);
    }

    @Test
    public void testSaveDataspaceSuccess() throws Exception {
        Mockito.when(dataspaceRespository.save(Mockito.any(DataSpace.class))).thenReturn(persistedDataspaceEntity);
        assertNotNull(dataspaceService.saveDataspace(DataSpaceMapper.INSTANCE.destinationToSource(persistedDataspaceEntity)));
    }

    @Test
    public void testGetAllDataSpacesSuccess() throws Exception {
        Mockito.when(dataspaceRespository.findAll()).thenReturn(persistedDataspaceEntities);
        assertNotNull(dataspaceService.getAllDataSpaces());
        dataspaceService.getAllDataSpaces().forEach((final DataSpaceDto dataspaceDto) -> {
            assertNotNull(dataspaceDto.getId());
            assertNotNull(dataspaceDto.getName());
            assertNotNull(dataspaceDto.getDataProviderTypeIds());
        });
    }

    @Test
    public void testGetDataspaceByIdSuccess() throws Exception {
        Mockito.when(dataspaceRespository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedDataspaceEntity));
        assertNotNull(dataspaceService.getDataSpaceById("1"));
    }

    @Test
    public void testGetDataspaceByNameSuccess() throws Exception {
        Mockito.when(dataspaceRespository.findDistinctDataSpaceByName(Mockito.anyString())).thenReturn(List.of(persistedDataspaceEntity));
        assertNotNull(dataspaceService.getDataSpaceByName("dataspace_name_1"));
    }

    @Test
    public void testGetDataspaceByInvalidId() throws Exception {
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            dataspaceService.getDataSpaceById("1xa");
        });
        assertEquals("Invalid identifier provided for querying dataspace", exception.getMessage());
    }

}
