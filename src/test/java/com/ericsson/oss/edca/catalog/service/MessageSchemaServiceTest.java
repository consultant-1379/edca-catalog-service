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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaResponseDto;
import com.ericsson.oss.edca.catalog.domain.DataSpace;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;
import com.ericsson.oss.edca.catalog.domain.DataCollector;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.MessageSchema;
import com.ericsson.oss.edca.catalog.domain.MessageStatusTopic;
import com.ericsson.oss.edca.catalog.domain.MessageDataTopic;
import com.ericsson.oss.edca.catalog.domain.utility.DataProviderTypeMapper;
import com.ericsson.oss.edca.catalog.domain.utility.DataCollectorMapper;
import com.ericsson.oss.edca.catalog.domain.utility.MessageDataTopicMapper;
import com.ericsson.oss.edca.catalog.repository.DataCollectorRepository;
import com.ericsson.oss.edca.catalog.repository.MessageDataTopicRepository;
import com.ericsson.oss.edca.catalog.repository.MessageSchemaRepository;
import com.ericsson.oss.edca.catalog.repository.DataProviderTypeRepository;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;


@ExtendWith(MockitoExtension.class)
class MessageSchemaServiceTest {
    @InjectMocks
    private MessageSchemaService messageSchemaService;

    @Mock
    private DataProviderTypeRepository dataProviderTypeRepository;

    @Mock
    private DataCollectorRepository dataCollectorRepository;

    @Mock
    private MessageDataTopicRepository messageDataTopicRepository;

    @Mock
    private DataSpaceRepository dataSpaceRepository;

    @Mock
    private MessageSchemaRepository messageSchemaRepository;

    @Mock
    private DataProviderTypeMapper dataProviderTypeMapper;

    @Mock
    private DataCollectorMapper dataCollectorMapper;

    @Mock
    private MessageDataTopicMapper messageDataTopicMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private DataProviderTypeMapper dataProviderTypeMapperLocal = DataProviderTypeMapper.INSTANCE;

    private DataCollectorMapper DataCollectorMapperLocal = DataCollectorMapper.INSTANCE;

    private MessageDataTopicMapper messageDataTopicMapperLocal = MessageDataTopicMapper.INSTANCE;

    private MessageSchemaDto messageSchemaDto;

    private MessageSchema messageSchema;

    private List<MessageSchema> persistedMessageSchemas = new ArrayList<>();

    @BeforeEach
    public void setup() throws MalformedURLException {

        messageSchemaDto = new MessageSchemaDto();
        messageSchemaDto.setId(1);
        messageSchemaDto.setDataProviderTypeId(1);
        messageSchemaDto.setMessageDataTopicId(1);
        messageSchemaDto.setSpecificationReference("sr");

        final DataSpace dataSpace = new DataSpace();
        dataSpace.setId(1);
        dataSpace.setName("dataspace_name");

        final MessageBus messageBus = new MessageBus();
        messageBus.setId(1);
        messageBus.setName("messageBus100");
        messageBus.setClusterName("clusterName1");
        messageBus.setNameSpace("NameSpace1");
        messageBus.setAccessEndpoints(Set.of("msg_bus_access_endpoint"));

        final DataCollector dataCollector = new DataCollector();
        dataCollector.setId(1);
        dataCollector.setName("data_collector");
        dataCollector.setCollectorId(UUID.randomUUID().toString());
        dataCollector.setControlEndpoint(new URL("http://1.1.1.1:9000/end_point"));
        messageSchemaDto.setDataCollectorId(UUID.fromString(dataCollector.getCollectorId()));

        final DataProviderType dataProviderType = new DataProviderType();
        dataProviderType.setId(1);
        dataProviderType.setDataCategory(DataCategory.CM_EXPORT);
        dataProviderType.setProviderTypeId("pvid_1");
        dataProviderType.setProviderVersion("pv_version_1");
        dataProviderType.setDataSpace(dataSpace);

        final MessageStatusTopic messageStatusTopic = new MessageStatusTopic();
        messageStatusTopic.setEncoding(MessageEncoding.JSON);
        messageStatusTopic.setId(1);
        messageStatusTopic.setMessageBus(messageBus);
        messageStatusTopic.setName("status-topic");
        messageStatusTopic.setSpecificationReference("spec-ref");

        final MessageDataTopic messageDataTopic = new MessageDataTopic();
        messageDataTopic.setEncoding(MessageEncoding.JSON);
        messageDataTopic.setId(101);
        messageDataTopic.setName("topic1");
        messageDataTopic.setMessageStatusTopic(messageStatusTopic);
        messageDataTopic.setMessageBus(messageBus);

        messageStatusTopic.setMessageDataTopics(Set.of(messageDataTopic));

        messageSchema = new MessageSchema();
        messageSchema.setId(1);
        messageSchema.setDataCollector(dataCollector);
        messageSchema.setDataProviderType(dataProviderType);
        messageSchema.setMessageDataTopic(messageDataTopic);
        messageSchema.setSpecificationReference("sr");
        messageDataTopic.setMessageSchemas(Set.of(messageSchema));
        persistedMessageSchemas.add(messageSchema);
    }

    @Test
    public void testSaveMessageSchema() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(List.of(persistedMessageSchemas.get(0).getDataCollector()));
        Mockito.when(messageDataTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataTopic()));
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));
        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));
        Mockito.when(messageSchemaRepository.save(Mockito.any(MessageSchema.class))).thenReturn(persistedMessageSchemas.get(0));
        MessageSchemaResponseDto checkResponse = (messageSchemaService.saveMessageSchema(messageSchemaDto));
        assertNotNull(messageSchemaService.saveMessageSchema(messageSchemaDto));
        final List<Object> declaredFields = Arrays.asList(checkResponse.getClass().getDeclaredFields());
        declaredFields.forEach((final Object field) -> {
            try {
                ((Field) field).setAccessible(true);
                assertTrue(((Field) field).get(checkResponse) != null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // ignored
            }
        });
    }

    @Test
    public void testSaveMessageSchemaWhenDataCollectorNotExists() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(messageDataTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataTopic()));
        assertThrows(CatalogException.class, () -> {
            messageSchemaService.saveMessageSchema(messageSchemaDto);
        }, CatalogConstants.MESSAGESCHEMA_DATACOLLECTOR_NOT_FOUND_MSG);
    }

    @Test
    public void testSaveMessageSchemaWhenDataCollectorNotExistsTwo() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(null);
        Mockito.when(messageDataTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataTopic()));
        assertThrows(CatalogException.class, () -> {
            messageSchemaService.saveMessageSchema(messageSchemaDto);
        }, CatalogConstants.MESSAGESCHEMA_DATACOLLECTOR_NOT_FOUND_MSG);
    }

    @Test
    public void testSaveMessageSchemaWhenDataProviderTypeNotExists() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(List.of(persistedMessageSchemas.get(0).getDataCollector()));
        Mockito.when(messageDataTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataTopic()));
        assertThrows(CatalogException.class, () -> {
            messageSchemaService.saveMessageSchema(messageSchemaDto);
        }, CatalogConstants.MESSAGESCHEMA_DATAPROVIDERTYPE_NOT_FOUND_MSG);
    }

    @Test
    public void testSaveMessageSchemaWhenMessageDataTopicNotExists() throws CatalogException {
        Mockito.when(dataProviderTypeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(dataCollectorRepository.findDistinctDataCollectorByCollectorId(Mockito.anyString())).thenReturn(List.of(persistedMessageSchemas.get(0).getDataCollector()));
        Mockito.when(messageDataTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(CatalogException.class, () -> {
            messageSchemaService.saveMessageSchema(messageSchemaDto);
        }, CatalogConstants.MESSAGESCHEMA_MESSAGEDATATOPIC_NOT_FOUND);
    }

    @Test
    public void testGetAllMessageSchemasSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));

        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));

        Mockito.when(messageSchemaRepository.findAll()).thenReturn(persistedMessageSchemas);
        messageSchemaService.getAllMessageSchema().forEach((final MessageSchemaResponseDto messageSchemaDto) -> {
            final List<Object> declaredFields = Arrays.asList(messageSchemaDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(messageSchemaDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataProviderTypeSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));

        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataProviderType", "pvid_1");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType().getDataSpace()));
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        @SuppressWarnings("unchecked")
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByQueryParams(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);
        messageSchemaDtos.forEach((final MessageSchemaResponseDto messageSchemaDto) -> {
            final List<Object> declaredFields = Arrays.asList(messageSchemaDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(messageSchemaDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataCategorySuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));

        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataCategory", DataCategory.CM_EXPORT.name());
        Mockito.when(dataProviderTypeRepository.findByDataSpaceNameAndDataCategory(DataCategory.CM_EXPORT, queryParams.get("dataSpace")))
                .thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        @SuppressWarnings("unchecked")
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByQueryParams(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);
        messageSchemaDtos.forEach((final MessageSchemaResponseDto messageSchemaDto) -> {
            final List<Object> declaredFields = Arrays.asList(messageSchemaDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(messageSchemaDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataCategoryEmpty() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataCategory", DataCategory.CM_EXPORT.name());
        Mockito.when(dataProviderTypeRepository.findByDataSpaceNameAndDataCategory(DataCategory.CM_EXPORT, queryParams.get("dataSpace"))).thenReturn(new ArrayList<>());
        @SuppressWarnings("unchecked")
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByQueryParams(queryParams);
        assertTrue(messageSchemaDtos == null || messageSchemaDtos.size() == 0);

    }

    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataCategoryNull() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataCategory", DataCategory.CM_EXPORT.name());
        Mockito.when(dataProviderTypeRepository.findByDataSpaceNameAndDataCategory(DataCategory.CM_EXPORT, queryParams.get("dataSpace"))).thenReturn(null);
        @SuppressWarnings("unchecked")
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByQueryParams(queryParams);
        assertTrue(messageSchemaDtos == null || messageSchemaDtos.size() == 0);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataProviderTypeWhenDataSpaceNotExist() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_not_exists");
        queryParams.put("dataProviderType", "pvid_1");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(null);
        assertTrue(((List<MessageSchema>) messageSchemaService.getMessageSchemaByQueryParams(queryParams)).isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataProviderTypeWhenDataSpaceMultiple() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_not_exists");
        queryParams.put("dataProviderType", "pvid_1");
        List<DataSpace> dataSpaces = new ArrayList<>();
        dataSpaces.add(persistedMessageSchemas.get(0).getDataProviderType().getDataSpace());
        dataSpaces.add(persistedMessageSchemas.get(0).getDataProviderType().getDataSpace());
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(dataSpaces);
        assertTrue(((List<MessageSchema>) messageSchemaService.getMessageSchemaByQueryParams(queryParams)).isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataProviderTypeWhenDataProviderTypeNotExist() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataProviderType", "data_provider_type_id_not_exists");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType().getDataSpace()));
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(null);
        assertTrue(((List<MessageSchema>) messageSchemaService.getMessageSchemaByQueryParams(queryParams)).isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataProviderTypeWhenDataProviderTypeListEmpty() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataProviderType", "data_provider_type_id_not_exists");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType().getDataSpace()));
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(new ArrayList<DataProviderType>());
        assertTrue(((List<MessageSchema>) messageSchemaService.getMessageSchemaByQueryParams(queryParams)).isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllMessageSchemasByDataspaceAndDataProviderTypeWhenDataProviderTypeIdNotMatch() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        queryParams.put("dataProviderType", "data_provider_type_id_not_exists");
        Mockito.when(dataSpaceRepository.findDistinctDataSpaceByName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType().getDataSpace()));
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));

        assertTrue(((List<MessageSchema>) messageSchemaService.getMessageSchemaByQueryParams(queryParams)).isEmpty());
    }

    @Test
    public void testGetAllMessageSchemasByOnlyDataspaceSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));

        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataSpace", "dataspace_name");
        Mockito.when(dataProviderTypeRepository.findByDataspaceName(queryParams.get("dataSpace"))).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        @SuppressWarnings("unchecked")
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByQueryParams(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);
        messageSchemaDtos.forEach((final MessageSchemaResponseDto messageSchemaDto) -> {
            final List<Object> declaredFields = Arrays.asList(messageSchemaDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(messageSchemaDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllMessageSchemaDtosByOnlyDataCategorySuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));

        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("dataCategory", DataCategory.CM_EXPORT.name());
        Mockito.when(dataProviderTypeRepository.findByDataCategory(DataCategory.CM_EXPORT)).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        @SuppressWarnings("unchecked")
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByQueryParams(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);
        messageSchemaDtos.forEach((final MessageSchemaResponseDto messageSchemaDto) -> {
            final List<Object> declaredFields = Arrays.asList(messageSchemaDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(messageSchemaDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithDataCategory() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));
        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", "::CM_EXPORT");
        Mockito.when(dataProviderTypeRepository.findByDataCategory(DataCategory.CM_EXPORT)).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);
        messageSchemaDtos.forEach((final MessageSchemaResponseDto messageSchemaDto) -> {
            final List<Object> declaredFields = Arrays.asList(messageSchemaDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(messageSchemaDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithOnlyProviderTypeId() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", "pvid_1::");
        Mockito.when(dataProviderTypeRepository.findByProviderTypeId("pvid_1")).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);

    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithOnlyProviderTypeVersion() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", ":pv_version_1:");
        Mockito.when(dataProviderTypeRepository.findByProviderVersion("pv_version_1")).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);

    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithOnlyDataCategoryUnavailable() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", "pvid_1:pv_version_1:");
        Mockito.when(dataProviderTypeRepository.findByProviderTypeIdAndProviderVersion("pvid_1", "pv_version_1")).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);

    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithOnlyProviderVersionUnavailable() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", "pvid_1::CM_EXPORT");
        Mockito.when(dataProviderTypeRepository.findByProviderTypeIdAndDataCategory("pvid_1", DataCategory.CM_EXPORT)).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);

    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithOnlyProviderTypeIdUnvailable() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", ":pv_version_1:CM_EXPORT");
        Mockito.when(dataProviderTypeRepository.findByProviderVersionAndDataCategory("pv_version_1", DataCategory.CM_EXPORT)).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);

    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithAllParamsAvailable() throws CatalogException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", "pvid_1:pv_version_1:CM_EXPORT");
        Mockito.when(dataProviderTypeRepository.findByProviderTypeIdAndProviderVersionAndDataCategory("pvid_1", "pv_version_1", DataCategory.CM_EXPORT))
                .thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);

    }

    @Test
    public void testGetAllMessageSchemaDtosByTopicWithoutValue() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));
        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));
        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));
        Mockito.when(messageSchemaRepository.findByDataProviderType(Mockito.anyInt())).thenReturn(persistedMessageSchemas);
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("topic", "::");
        Mockito.when(dataProviderTypeRepository.findAll()).thenReturn(List.of(persistedMessageSchemas.get(0).getDataProviderType()));
        final List<MessageSchemaResponseDto> messageSchemaDtos = (List<MessageSchemaResponseDto>) messageSchemaService.getMessageSchemaByTopic(queryParams);
        assertTrue(messageSchemaDtos != null && messageSchemaDtos.size() > 0);
        messageSchemaDtos.forEach((final MessageSchemaResponseDto messageSchemaDto) -> {
            final List<Object> declaredFields = Arrays.asList(messageSchemaDto.getClass().getDeclaredFields());
            declaredFields.forEach((final Object field) -> {
                try {
                    ((Field) field).setAccessible(true);
                    assertTrue(((Field) field).get(messageSchemaDto) != null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // ignored
                }
            });
        });
    }

    @Test
    public void testGetMessageSchemaByIdSuccess() throws CatalogException {
        Mockito.when(dataProviderTypeMapper.destinationToSource(Mockito.any(DataProviderType.class)))
                .thenReturn(dataProviderTypeMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataProviderType()));

        Mockito.when(dataCollectorMapper.destinationToSource(Mockito.any(DataCollector.class)))
                .thenReturn(DataCollectorMapperLocal.destinationToSource(persistedMessageSchemas.get(0).getDataCollector()));

        Mockito.when(messageDataTopicMapper.entityToDto(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopicMapperLocal.entityToDto(persistedMessageSchemas.get(0).getDataTopic()));

        Mockito.when(messageSchemaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(persistedMessageSchemas.get(0)));
        final MessageSchemaResponseDto queriedMessageSchemaDto = messageSchemaService.getMessageSchemaById("1");
        assertTrue(queriedMessageSchemaDto != null);

        final List<Object> declaredFields = Arrays.asList(queriedMessageSchemaDto.getClass().getDeclaredFields());
        declaredFields.forEach((final Object field) -> {
            try {
                ((Field) field).setAccessible(true);
                assertTrue(((Field) field).get(queriedMessageSchemaDto) != null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // ignored
            }
        });
    }

    @Test
    public void testGetMessageSchemaByNonIntegerId() throws CatalogException {
        assertThrows(NumberFormatException.class, () -> {
            messageSchemaService.getMessageSchemaById("10x");
        });
    }

    @Test
    public void testGetMessageSchemaByNullId() throws CatalogException {
        assertNull(messageSchemaService.getMessageSchemaById(null));

    }

    @Test
    public void testGetMessageSchemaByEmptyStringId() throws CatalogException {
        assertNull(messageSchemaService.getMessageSchemaById(""));

    }

    @Test
    public void testGetMessageSchemaByIdNotExists() throws CatalogException {
        Mockito.when(messageSchemaRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertNull(messageSchemaService.getMessageSchemaById("10"));

    }
}