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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatDto;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatResponseDto;
import com.ericsson.oss.edca.catalog.domain.DataSpace;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;
import com.ericsson.oss.edca.catalog.domain.DataCollector;
import com.ericsson.oss.edca.catalog.domain.FileFormat;
import com.ericsson.oss.edca.catalog.domain.BulkDataRepository;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.DataEncoding;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.NotificationTopic;
import com.ericsson.oss.edca.catalog.domain.utility.BulkDataRepositoryMapper;
import com.ericsson.oss.edca.catalog.domain.utility.DataProviderTypeMapper;
import com.ericsson.oss.edca.catalog.domain.utility.NotificationTopicMapper;
import com.ericsson.oss.edca.catalog.domain.utility.DataCollectorMapper;
import com.ericsson.oss.edca.catalog.repository.DataCollectorRepository;
import com.ericsson.oss.edca.catalog.repository.DataProviderTypeRepository;
import com.ericsson.oss.edca.catalog.repository.BulkDataRepositoryRepo;
import com.ericsson.oss.edca.catalog.repository.FileFormatRepository;
import com.ericsson.oss.edca.catalog.repository.NotificationTopicRepository;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;

@ExtendWith(MockitoExtension.class)
public class FileFormatServiceTest {

    @InjectMocks
    private FileFormatService fileFormatService;

    @Mock
    private DataProviderTypeRepository dataProviderTypeRepository;

    @Mock
    private DataCollectorRepository dataCollectorRepository;

    @Mock
    private NotificationTopicRepository notificationTopicRepository;

    @Mock
    private BulkDataRepositoryRepo bulkDataRepositoryRepo;

    @Mock
    private FileFormatRepository fileFormatRepository;

    @Mock
    private DataSpaceRepository dataSpaceRepository;

    @Mock
    private DataProviderTypeMapper dataProviderTypeMapper;

    @Mock
    private DataCollectorMapper dataCollectorMapper;

    @Mock
    private BulkDataRepositoryMapper bulkDataRepositoryMapper;

    @Mock
    private NotificationTopicMapper notificationTopicMapper;

    private List<FileFormat> persistedFileFormats = new ArrayList<>();

    private FileFormatDto fileFormatToPersist = null;

    private DataProviderTypeMapper dataProviderTypeMapperLocal = DataProviderTypeMapper.INSTANCE;

    private NotificationTopicMapper notificationTopicMapperLocal = NotificationTopicMapper.INSTANCE;

    private BulkDataRepositoryMapper bulkDataRepositoryMapperLocal = BulkDataRepositoryMapper.MAPPER_INSTANCE;

    private DataCollectorMapper DataCollectorMapperLocal = DataCollectorMapper.INSTANCE;

    @BeforeEach
    public void setup() throws MalformedURLException {
        final DataSpace dataSpace = new DataSpace();
        dataSpace.setId(1);
        dataSpace.setName("dataspace_name");
        final FileFormat persistedFileFormat = new FileFormat();

        persistedFileFormat.setReportOutputPeriodList(Set.of(15));
        persistedFileFormat.setId(1);
        persistedFileFormat.setSpecificationReference("");
        final BulkDataRepository bulkDataRepository = new BulkDataRepository();
        bulkDataRepository.setId(1);
        bulkDataRepository.setName("bdr");
        bulkDataRepository.setNameSpace("bdr_ns");
        bulkDataRepository.setClusterName("bdr_cluster");
        bulkDataRepository.setAccessEndpoints(Set.of("bdr_endpoint"));
        bulkDataRepository.setFileFormat(Set.of(persistedFileFormat));
        persistedFileFormat.setBulkDataRepository(bulkDataRepository);
        final DataCollector dataCollector = new DataCollector();
        dataCollector.setId(1);
        dataCollector.setName("data_collector");
        dataCollector.setCollectorId(UUID.randomUUID().toString());
        dataCollector.setControlEndpoint(new URL("http://1.1.1.1:9000/end_point"));
        persistedFileFormat.setDataCollector(dataCollector);
        persistedFileFormat.setDataEncoding(DataEncoding.XML);
        final DataProviderType dataProviderType = new DataProviderType();
        dataProviderType.setId(1);
        dataProviderType.setDataCategory(DataCategory.CM_EXPORT);
        dataProviderType.setProviderTypeId("pvid_1");
        dataProviderType.setProviderVersion("pv_version_1");
        dataProviderType.setDataSpace(dataSpace);
        dataProviderType.addFileFormat(persistedFileFormat);
        dataSpace.setDataProviderTypes(Set.of(dataProviderType));
        persistedFileFormat.setDataProviderType(dataProviderType);

        final NotificationTopic notificationTopic = new NotificationTopic();
        notificationTopic.setId(1);
        notificationTopic.setName("notification_topic");
        notificationTopic.setEncoding(MessageEncoding.JSON);
        notificationTopic.setSpecificationReference("");
        final MessageBus messageBus = new MessageBus();
        messageBus.setId(1);
        messageBus.setName("msg_bus");
        messageBus.setNameSpace("msg_bus_ns");
        messageBus.setClusterName("msg_bus_cluster");
        messageBus.setAccessEndpoints(Set.of("msg_bus_access_endpoint"));
        messageBus.setNotificationTopics(Set.of(notificationTopic));
        notificationTopic.setMessageBus(messageBus);
        persistedFileFormat.setNotificationTopic(notificationTopic);
        persistedFileFormats.add(persistedFileFormat);
        fileFormatToPersist = new FileFormatDto();
        fileFormatToPersist.setDataCollectorId(UUID.fromString(dataCollector.getCollectorId()));
        fileFormatToPersist.setBulkDataRepositoryId(1);
        fileFormatToPersist.setDataProviderTypeId(1);
        fileFormatToPersist.setNotificationTopicId(1);
        fileFormatToPersist.setReportOutputPeriodList(Set.of(15));
        fileFormatToPersist.setSpecificationReference("");
        fileFormatToPersist.setDataEncoding(DataEncoding.XML);

    }

    @Test
    public void testSaveFileFormat() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(List.of(persistedFileFormats.get(0).getDataCollector()));
        Mockito.when(notificationTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getNotificationTopic()));
        Mockito.when(bulkDataRepositoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getBulkDataRepository()));

        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataCollector()));

        Mockito.when(notificationTopicMapper.entityToDto(Mockito.any(NotificationTopic.class)))
                .thenReturn(notificationTopicMapperLocal.entityToDto(persistedFileFormats.get(0).getNotificationTopic()));

        Mockito.when(bulkDataRepositoryMapper.entityToDTO(Mockito.any(BulkDataRepository.class)))
                .thenReturn(bulkDataRepositoryMapperLocal.entityToDTO(persistedFileFormats.get(0).getBulkDataRepository()));

        Mockito.when(fileFormatRepository.save(Mockito.any(FileFormat.class))).thenReturn(persistedFileFormats.get(0));
        final FileFormatResponseDto fileFormatDtoPersisted = fileFormatService.saveFileFormat(fileFormatToPersist);
        assertNotNull(fileFormatService.saveFileFormat(fileFormatToPersist));
        final List<Object> declaredFields = Arrays.asList(fileFormatDtoPersisted.getClass().getDeclaredFields());
        declaredFields.forEach((final Object field) -> {
            try {
                ((Field) field).setAccessible(true);
                assertTrue(((Field) field).get(fileFormatDtoPersisted) != null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // ignored
            }
        });

    }

    @Test
    public void testSaveFileFormatWhenDataProviderNotExists() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(List.of(persistedFileFormats.get(0).getDataCollector()));
        Mockito.when(notificationTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getNotificationTopic()));
        Mockito.when(bulkDataRepositoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getBulkDataRepository()));
        assertThrows(CatalogException.class, () -> {
            fileFormatService.saveFileFormat(fileFormatToPersist);
        }, CatalogConstants.FILEFORMAT_DATAPROVIDERTYPE_NOT_FOUND_MSG);

    }

    @Test
    public void testSaveFileFormatWhenDataCollectorNotExists() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(notificationTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getNotificationTopic()));
        Mockito.when(bulkDataRepositoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getBulkDataRepository()));
        assertThrows(CatalogException.class, () -> {
            fileFormatService.saveFileFormat(fileFormatToPersist);
        }, CatalogConstants.FILEFORMAT_DATACOLLECTOR_NOT_FOUND_MSG);

    }

    @Test
    public void testSaveFileFormatWhenDataCollectorNull() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(null);
        Mockito.when(notificationTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getNotificationTopic()));
        Mockito.when(bulkDataRepositoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getBulkDataRepository()));
        assertThrows(CatalogException.class, () -> {
            fileFormatService.saveFileFormat(fileFormatToPersist);
        }, CatalogConstants.FILEFORMAT_DATACOLLECTOR_NOT_FOUND_MSG);

    }

    @Test
    public void testSaveFileFormatWhenNotificationTopicNotExists() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(notificationTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(bulkDataRepositoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getBulkDataRepository()));
        assertThrows(CatalogException.class, () -> {
            fileFormatService.saveFileFormat(fileFormatToPersist);
        }, CatalogConstants.FILEFORMAT_NOTIFICATION_TOPIC_NOT_FOUND_MSG);

    }

    @Test
    public void testSaveFileFormatBdrNotExists() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(notificationTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0).getNotificationTopic()));
        Mockito.when(bulkDataRepositoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(CatalogException.class, () -> {
            fileFormatService.saveFileFormat(fileFormatToPersist);
        }, CatalogConstants.FILEFORMAT_BULK_DATA_REPO_NOT_FOUND_MSG);

    }

    @Test
    public void testGetAllFileFormatsSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataCollector()));

        Mockito.when(notificationTopicMapper.entityToDto(Mockito.any(NotificationTopic.class)))
                .thenReturn(notificationTopicMapperLocal.entityToDto(persistedFileFormats.get(0).getNotificationTopic()));

        Mockito.when(bulkDataRepositoryMapper.entityToDTO(Mockito.any(BulkDataRepository.class)))
                .thenReturn(bulkDataRepositoryMapperLocal.entityToDTO(persistedFileFormats.get(0).getBulkDataRepository()));
        Mockito.when(fileFormatRepository.findAll()).thenReturn(persistedFileFormats);
        fileFormatService.getAllFileFormats().forEach((final FileFormatResponseDto fileFormatDto) -> {
            final List<Object> declaredFields = Arrays.asList(fileFormatDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(fileFormatDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllFileFormatsByDataspaceAndDataProviderTypeSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataCollector()));

        Mockito.when(notificationTopicMapper.entityToDto(Mockito.any(NotificationTopic.class)))
                .thenReturn(notificationTopicMapperLocal.entityToDto(persistedFileFormats.get(0).getNotificationTopic()));

        Mockito.when(bulkDataRepositoryMapper.entityToDTO(Mockito.any(BulkDataRepository.class)))
                .thenReturn(bulkDataRepositoryMapperLocal.entityToDTO(persistedFileFormats.get(0).getBulkDataRepository()));

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataProviderType", "pvid_1");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedFileFormats.get(0).getDataProviderType().getDataSpace()));
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(fileFormatRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedFileFormats);
        final List<FileFormatResponseDto> fileFormatDtos = fileFormatService.getFileFormatByQueryParams(queryParams);
        assertTrue(fileFormatDtos != null && fileFormatDtos.size() > 0);
        fileFormatDtos.forEach((final FileFormatResponseDto fileFormatDto) -> {
            final List<Object> declaredFields = Arrays.asList(fileFormatDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(fileFormatDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllFileFormatsByDataspaceAndDataProviderTypeWhenDataSpaceNotExist() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_not_exists");
        queryParams.put("dataProviderType", "pvid_1");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(null);
        assertTrue(fileFormatService.getFileFormatByQueryParams(queryParams).isEmpty());
    }

    @Test
    public void testGetAllFileFormatsByDataspaceAndDataProviderTypeWhenDataProviderTypeNotExist() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataProviderType", "data_provider_type_id_not_exists");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedFileFormats.get(0).getDataProviderType().getDataSpace()));

        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(null);
        assertTrue(fileFormatService.getFileFormatByQueryParams(queryParams).isEmpty());
    }

    @Test
    public void testGetAllFileFormatsByDataspaceAndDataProviderTypeWhenDataProviderTypeIdNotMatch() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataProviderType", "data_provider_type_id_not_exists");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedFileFormats.get(0).getDataProviderType().getDataSpace()));
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedFileFormats.get(0).getDataProviderType()));

        assertTrue(fileFormatService.getFileFormatByQueryParams(queryParams).isEmpty());
    }

    @Test
    public void testGetAllFileFormatsByOnlyDataspaceSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataCollector()));

        Mockito.when(notificationTopicMapper.entityToDto(Mockito.any(NotificationTopic.class)))
                .thenReturn(notificationTopicMapperLocal.entityToDto(persistedFileFormats.get(0).getNotificationTopic()));

        Mockito.when(bulkDataRepositoryMapper.entityToDTO(Mockito.any(BulkDataRepository.class)))
                .thenReturn(bulkDataRepositoryMapperLocal.entityToDTO(persistedFileFormats.get(0).getBulkDataRepository()));

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(fileFormatRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedFileFormats);
        final List<FileFormatResponseDto> fileFormatDtos = fileFormatService.getFileFormatByQueryParams(queryParams);
        assertTrue(fileFormatDtos != null && fileFormatDtos.size() > 0);
        fileFormatDtos.forEach((final FileFormatResponseDto fileFormatDto) -> {
            final List<Object> declaredFields = Arrays.asList(fileFormatDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(fileFormatDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllFileFormatsByOnlyDataCategorySuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataCollector()));

        Mockito.when(notificationTopicMapper.entityToDto(Mockito.any(NotificationTopic.class)))
                .thenReturn(notificationTopicMapperLocal.entityToDto(persistedFileFormats.get(0).getNotificationTopic()));

        Mockito.when(bulkDataRepositoryMapper.entityToDTO(Mockito.any(BulkDataRepository.class)))
                .thenReturn(bulkDataRepositoryMapperLocal.entityToDTO(persistedFileFormats.get(0).getBulkDataRepository()));

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataCategory", DataCategory.CM_EXPORT.name());
        Mockito.when(dataProviderTypeRepository.findByDataCategory(DataCategory.CM_EXPORT)).thenReturn(List.of(persistedFileFormats.get(0).getDataProviderType()));
        Mockito.when(fileFormatRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedFileFormats);
        final List<FileFormatResponseDto> fileFormatDtos = fileFormatService.getFileFormatByQueryParams(queryParams);
        assertTrue(fileFormatDtos != null && fileFormatDtos.size() > 0);
        fileFormatDtos.forEach((final FileFormatResponseDto fileFormatDto) -> {
            final List<Object> declaredFields = Arrays.asList(fileFormatDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(fileFormatDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetFileFormatByIdSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedFileFormats.get(0).getDataCollector()));

        Mockito.when(notificationTopicMapper.entityToDto(Mockito.any(NotificationTopic.class)))
                .thenReturn(notificationTopicMapperLocal.entityToDto(persistedFileFormats.get(0).getNotificationTopic()));

        Mockito.when(bulkDataRepositoryMapper.entityToDTO(Mockito.any(BulkDataRepository.class)))
                .thenReturn(bulkDataRepositoryMapperLocal.entityToDTO(persistedFileFormats.get(0).getBulkDataRepository()));

        Mockito.when(fileFormatRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedFileFormats.get(0)));
        final FileFormatResponseDto queriedFileFormatDto = fileFormatService.getFileFormatById("1");
        assertTrue(queriedFileFormatDto != null);

        final List<Object> declaredFields = Arrays.asList(queriedFileFormatDto.getClass().getDeclaredFields());
        declaredFields.forEach((final Object field) -> {
            try {
                ((Field) field).setAccessible(true);
                assertTrue(((Field) field).get(queriedFileFormatDto) != null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // ignored
            }
        });
    }

    @Test
    public void testGetFileFormatByNonIntegerId() throws CatalogException {
        assertThrows(NumberFormatException.class, () -> {
            fileFormatService.getFileFormatById("10x");
        });
    }

    @Test
    public void testGetFileFormatByNullId() throws CatalogException {
        assertNull(fileFormatService.getFileFormatById(null));

    }

    @Test
    public void testGetFileFormatByEmptyStringId() throws CatalogException {
        assertNull(fileFormatService.getFileFormatById(""));

    }

    @Test
    public void testGetFileFormatByIdNotExists() throws CatalogException {
        Mockito.when(fileFormatRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertNull(fileFormatService.getFileFormatById("10"));

    }

    @Test
    public void testGetAllFileFormatsByOnlyDataSpaceNotExists() throws CatalogException {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_not_exists");
        assertTrue(fileFormatService.getFileFormatByQueryParams(queryParams).size() == 0);
    }

}
