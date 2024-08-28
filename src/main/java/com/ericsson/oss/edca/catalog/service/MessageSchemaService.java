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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaResponseDto;
import com.ericsson.oss.edca.catalog.domain.MessageDataTopic;
import com.ericsson.oss.edca.catalog.domain.MessageSchema;
import com.ericsson.oss.edca.catalog.domain.DataCollector;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.DataSpace;
import com.ericsson.oss.edca.catalog.repository.MessageDataTopicRepository;
import com.ericsson.oss.edca.catalog.repository.DataCollectorRepository;
import com.ericsson.oss.edca.catalog.repository.DataProviderTypeRepository;
import com.ericsson.oss.edca.catalog.repository.MessageSchemaRepository;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;
import com.ericsson.oss.edca.catalog.domain.utility.MessageDataTopicMapper;
import com.ericsson.oss.edca.catalog.domain.utility.MessageSchemaMapper;
import com.ericsson.oss.edca.catalog.domain.utility.DataCollectorMapper;
import com.ericsson.oss.edca.catalog.domain.utility.DataProviderTypeMapper;

@Service
public class MessageSchemaService {

    private static final Logger logger = LoggerFactory.getLogger(MessageSchemaService.class);

    private static final MessageSchemaMapper messageSchemaMapper = MessageSchemaMapper.INSTANCE;

    @Autowired
    private DataProviderTypeMapper dataProviderTypeMapper;

    @Autowired
    private DataCollectorMapper dataCollectorMapper;

    @Autowired
    private MessageDataTopicMapper messageDataTopicMapper;

    @Autowired
    private DataProviderTypeRepository dataProviderTypeRepository;

    @Autowired
    private DataCollectorRepository dataCollectorRepository;

    @Autowired
    private MessageDataTopicRepository messageDataTopicRepository;

    @Autowired
    private MessageSchemaRepository messageSchemaRepository;

    @Autowired
    private DataSpaceRepository dataSpaceRepository;

    /**
     * This Service method is used to interact with the repository layer to persist the MessageSchema entities/details in the DB
     *
     * @param messageSchemaDtoToRegister
     *            - MessageSchema Detail( MessageSchemaDto )
     * @return saved/persisted Message Schema Entry in the DB
     *
     */
    public MessageSchemaResponseDto saveMessageSchema(final MessageSchemaDto messageSchemaDtoToRegister) throws CatalogException {
        final MessageSchema messageSchemaToPersist = new MessageSchema();
        messageSchemaToPersist.setSpecificationReference(messageSchemaDtoToRegister.getSpecificationReference());
        final Map<String, String> propertyErrorMessageMap = validateMessageSchema(messageSchemaDtoToRegister, messageSchemaToPersist);
        logger.info("MessageSchema to Persist : {}", messageSchemaToPersist);
        if (propertyErrorMessageMap.size() > 0) {
            // prepare exception message with all properties which are non-existent entities
            final String messageSchemaValidationErrorMsg = String.join(",", new ArrayList<String>(propertyErrorMessageMap.values()));
            throw new CatalogException(messageSchemaValidationErrorMsg);
        }
        final MessageSchema persistedMessageSchema = messageSchemaRepository.save(messageSchemaToPersist);
        logger.info("Persisted MessageSchema : {}", persistedMessageSchema);
        final MessageSchemaResponseDto persistedMessageSchemaDto = messageSchemaMapper.destinationToSource(persistedMessageSchema);
        mapEntityToResponse(persistedMessageSchema, persistedMessageSchemaDto);
        logger.info("Persisted MessageSchemaDto : {}", persistedMessageSchemaDto);
        return persistedMessageSchemaDto;
    }

    private void mapEntityToResponse(final MessageSchema messageSchema, final MessageSchemaResponseDto messageSchemaResponseDto) {
        messageSchemaResponseDto.setDataCollector(dataCollectorMapper.destinationToSource(messageSchema.getDataCollector()));
        messageSchemaResponseDto.setDataProviderType(dataProviderTypeMapper.destinationToSource(messageSchema.getDataProviderType()));
        messageSchemaResponseDto.setMessageDataTopic(messageDataTopicMapper.entityToDto(messageSchema.getDataTopic()));
    }

    /**
     * Utility method is used to interact with the repository layer to validate the messageSchema request details before persisting
     *
     * @param messageSchemaDto
     *            - messageSchema request details
     * @param messageSchemaToPersist
     *            - messageSchema to persist
     * @return
     *
     */
    private Map<String, String> validateMessageSchema(final MessageSchemaDto messageSchemaDto, final MessageSchema messageSchemaToPersist) {
        final Map<String, String> propertyErrorMessageMap = new HashMap<>();
        final Optional<DataProviderType> isDataProviderTypeAvailable = dataProviderTypeRepository.findById(messageSchemaDto.getDataProviderTypeId());
        if (!isDataProviderTypeAvailable.isPresent()) {
            logger.error("Catalog Service :: validateMessageSchema :: DataProviderType with id {} not found", messageSchemaDto.getDataProviderTypeId());
            propertyErrorMessageMap.put("dataProviderTypeId", CatalogConstants.MESSAGESCHEMA_DATAPROVIDERTYPE_NOT_FOUND_MSG);
        } else {
            final DataProviderType associatedDataProviderType = isDataProviderTypeAvailable.get();
            messageSchemaToPersist.setDataProviderType(associatedDataProviderType);

        }
        final List<DataCollector> isDataCollectorAvailable = dataCollectorRepository.findDistinctDataCollectorByCollectorId(messageSchemaDto.getDataCollectorId().toString());
        if (isDataCollectorAvailable == null || isDataCollectorAvailable.isEmpty()) {
            logger.error("Catalog Service :: validateMessageSchema :: DataCollector with id {} not found", messageSchemaDto.getDataCollectorId());
            propertyErrorMessageMap.put("dataCollectorId", CatalogConstants.MESSAGESCHEMA_DATACOLLECTOR_NOT_FOUND_MSG);
        } else {
            // Each dataCollector is registered with unique collectorId
            final DataCollector associatedDataCollector = isDataCollectorAvailable.get(0);
            messageSchemaToPersist.setDataCollector(associatedDataCollector);

        }
        final Optional<MessageDataTopic> isMessageDataTopicAvailable = messageDataTopicRepository.findById(messageSchemaDto.getMessageDataTopicId());
        if (!isMessageDataTopicAvailable.isPresent()) {
            logger.error("Catalog Service :: validateMessageSchema :: MessageDataTopic with Id {} not found", messageSchemaDto.getMessageDataTopicId());
            propertyErrorMessageMap.put("messageDataTopicId", CatalogConstants.MESSAGESCHEMA_MESSAGEDATATOPIC_NOT_FOUND);
        } else {
            final MessageDataTopic associatedMessageDataTopic = isMessageDataTopicAvailable.get();
            messageSchemaToPersist.setMessageDataTopic(associatedMessageDataTopic);
        }
        return propertyErrorMessageMap;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular MessageSchema entries from the DB, Using query params
     *
     * @param queryParams
     *            - Supported Query Criteria: 1. All MessageSchemas for a dataspace and/or data category (e.g. all stats files for 5G, all stats files, all 5G MessageSchema) 2 .All MessageSchemas for
     *            a dataspace and a data provider type (e.g. 5G CTR) 3.All MessageSchemas for a topic(dataProviderTypeId:dataProviderTypeVersion:dataCategory)
     * @return List of MessageSchema entries matching criteria else emptyList
     */
    public Object getMessageSchemaByQueryParams(final Map<String, String> queryParams) {
        final List<MessageSchemaResponseDto> selectedMessageSchemas = new ArrayList<>();
        final String queryParamDataspace = queryParams.getOrDefault("dataSpace", "");
        final String queryParamDataProviderType = queryParams.getOrDefault("dataProviderType", "");
        final String queryParamDataCategory = queryParams.getOrDefault("dataCategory", "");

        /*
         * Supported Query Criteria:
         *
         * Following combinations are supported: 1. All message schemas for a dataspace and/or data category 2 .All message schemas for a dataspace and a data provider type
         */

        if (!queryParamDataspace.isEmpty() && !queryParamDataProviderType.isEmpty()) {
            // Find all message schemas for a dataspace and a data provider type
            getMessageSchemaByDataSpaceAndProviderType(queryParamDataspace, queryParamDataProviderType, selectedMessageSchemas);
        } else if ((!queryParamDataspace.isEmpty() && !queryParamDataCategory.isEmpty()) || (!queryParamDataspace.isEmpty() || (!queryParamDataCategory.isEmpty()))) {
            // Find all message schemas for a dataspace and/or data category
            getMessageSchemaByDataSpaceAndOrDataCategory(queryParamDataspace, queryParamDataCategory, selectedMessageSchemas);
        }
        return selectedMessageSchemas;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular MessageSchema entries from the DB, Using query params
     *
     * @param queryParams
     *            - All MessageSchemas for topic(dataProviderTypeId:dataProviderTypeVersion:dataCategory)
     * @return List of MessageSchema entries matching criteria else emptyList
     */

    public List<MessageSchemaResponseDto> getMessageSchemaByTopic(final Map<String, String> queryParams) {
        final List<MessageSchemaResponseDto> selectedMessageSchemas = new ArrayList<>();
        List<DataProviderType> selectedDataProviderTypeEntities = new ArrayList<>();
        final String[] queryParamTopic = queryParams.get("topic").split(":");
        int counter = (int) Arrays.stream(queryParamTopic).filter(paramEmpty -> !paramEmpty.isEmpty()).count();
        if (counter == 3) {
            selectedDataProviderTypeEntities = dataProviderTypeRepository.findByProviderTypeIdAndProviderVersionAndDataCategory(queryParamTopic[0], queryParamTopic[1],
                    DataCategory.valueOf(queryParamTopic[2]));
        }
        if (counter == 2) {
            if (queryParamTopic[0].isEmpty())
                selectedDataProviderTypeEntities = dataProviderTypeRepository.findByProviderVersionAndDataCategory(queryParamTopic[1], DataCategory.valueOf(queryParamTopic[2]));
            else if (queryParamTopic[1].isEmpty())
                selectedDataProviderTypeEntities = dataProviderTypeRepository.findByProviderTypeIdAndDataCategory(queryParamTopic[0], DataCategory.valueOf(queryParamTopic[2]));
            else if (!queryParamTopic[0].isEmpty() && !queryParamTopic[1].isEmpty())
                selectedDataProviderTypeEntities = dataProviderTypeRepository.findByProviderTypeIdAndProviderVersion(queryParamTopic[0], queryParamTopic[1]);
        }
        if (counter == 1) {
            if (!queryParamTopic[0].isEmpty())
                selectedDataProviderTypeEntities = dataProviderTypeRepository.findByProviderTypeId(queryParamTopic[0]);
            else if (!queryParamTopic[1].isEmpty())
                selectedDataProviderTypeEntities = dataProviderTypeRepository.findByProviderVersion(queryParamTopic[1]);
            else if (!queryParamTopic[2].isEmpty())
                selectedDataProviderTypeEntities = dataProviderTypeRepository.findByDataCategory(DataCategory.valueOf(queryParamTopic[2]));
        }
        if (counter == 0) {
            selectedDataProviderTypeEntities = (List<DataProviderType>) dataProviderTypeRepository.findAll();
        }

        selectedDataProviderTypeEntities.forEach((final DataProviderType dataProviderType) -> {
            final List<MessageSchema> selectedmessageSchema = messageSchemaRepository.findByDataProviderType(dataProviderType.getId());
            selectedmessageSchema.forEach((final MessageSchema messageSchema) -> {
                final MessageSchemaResponseDto selectedMessageSchemaDto = messageSchemaMapper.destinationToSource(messageSchema);
                mapEntityToResponse(messageSchema, selectedMessageSchemaDto);
                selectedMessageSchemas.add(selectedMessageSchemaDto);
            });
        });
        logger.info("Catalog Service :: getMessageSchemaByTopic :: Selected messageSchema by Topic : {}", selectedMessageSchemas);
        return selectedMessageSchemas;

    }

    private void getMessageSchemaByDataSpaceAndOrDataCategory(final String queryParDataspace, final String queryParDataCategory, final List<MessageSchemaResponseDto> selectedMessageSchemas) {
        final List<DataProviderType> selectedDataProviderTypeEntities = new ArrayList<>();
        if (!queryParDataCategory.isEmpty() && !queryParDataspace.isEmpty()) {
            final List<DataProviderType> dataProviderTypeByDataSpaceAndCategory = dataProviderTypeRepository.findByDataSpaceNameAndDataCategory(DataCategory.valueOf(queryParDataCategory),
                    queryParDataspace);
            if (dataProviderTypeByDataSpaceAndCategory != null && !dataProviderTypeByDataSpaceAndCategory.isEmpty()) {
                selectedDataProviderTypeEntities.addAll(dataProviderTypeByDataSpaceAndCategory);
            }
        } else if (!queryParDataCategory.isEmpty() || !queryParDataspace.isEmpty()) {
            List<DataProviderType> dataProviderTypesByDataSpaceOrDataCategory = null;
            if (!queryParDataCategory.isEmpty()) {
                dataProviderTypesByDataSpaceOrDataCategory = dataProviderTypeRepository.findByDataCategory(DataCategory.valueOf(queryParDataCategory));
            } else if (!queryParDataspace.isEmpty()) {
                dataProviderTypesByDataSpaceOrDataCategory = dataProviderTypeRepository.findByDataspaceName(queryParDataspace);
            }
            if (dataProviderTypesByDataSpaceOrDataCategory != null && !dataProviderTypesByDataSpaceOrDataCategory.isEmpty()) {
                selectedDataProviderTypeEntities.addAll(dataProviderTypesByDataSpaceOrDataCategory);
            }
        }
        selectedDataProviderTypeEntities.forEach((final DataProviderType dataProviderType) -> {
            final List<MessageSchema> selectedmessageSchema = messageSchemaRepository.findByDataProviderType(dataProviderType.getId());
            selectedmessageSchema.forEach((final MessageSchema messageSchema) -> {
                final MessageSchemaResponseDto selectedMessageSchemaDto = messageSchemaMapper.destinationToSource(messageSchema);
                mapEntityToResponse(messageSchema, selectedMessageSchemaDto);
                selectedMessageSchemas.add(selectedMessageSchemaDto);
            });
        });
        logger.info("Catalog Service :: getMessageSchemaByDataSpaceAndOrDataCategory :: Selected messageSchema by DataSpace AND/OR DataCategory : {}", selectedMessageSchemas);
    }

    private void getMessageSchemaByDataSpaceAndProviderType(final String queryParamDataspace, final String queryParamDataProviderType, final List<MessageSchemaResponseDto> selectedMessageSchemas) {
        DataProviderType selectedDataProviderType = null;
        final List<DataSpace> isDataSpaceAvail = dataSpaceRepository.findDistinctDataSpaceByName(queryParamDataspace);
        // each Dataspace has unique name , so at most one entity returned
        if (isDataSpaceAvail != null && isDataSpaceAvail.size() == 1) {
            // find the associated dataProviderType for the dataSpace
            final List<DataProviderType> relatedDataProviderTypes = dataProviderTypeRepository.findByDataspaceName(queryParamDataspace);
            if (relatedDataProviderTypes != null && !relatedDataProviderTypes.isEmpty()) {
                for (final DataProviderType dataProviderType : relatedDataProviderTypes) {
                    // select the dataProviderType matching the provided dataProviderTypeId in the
                    // query
                    if (dataProviderType.getProviderTypeId().equals(queryParamDataProviderType)) {
                        selectedDataProviderType = dataProviderType;
                        break;
                    }
                }
            }
            if (selectedDataProviderType != null) {
                // find the associated messageSchema for the selected dataProviderType from
                // above
                final List<MessageSchema> selectedMessageSchemasByDataProviderTypeAndDataSpace = messageSchemaRepository.findByDataProviderType(selectedDataProviderType.getId());
                selectedMessageSchemasByDataProviderTypeAndDataSpace.forEach((final MessageSchema messageSchema) -> {
                    final MessageSchemaResponseDto messageSchemaDto = messageSchemaMapper.destinationToSource(messageSchema);
                    mapEntityToResponse(messageSchema, messageSchemaDto);
                    selectedMessageSchemas.add(messageSchemaDto);
                });
            }
        }
        logger.info("Catalog Service :: getMessageSchemaByDataSpaceAndProviderType :: Selected MessageSchema by dataSpace AND dataProviderType : {}", selectedMessageSchemas);

    }

    /**
     * This Service method is used to interact with the repository layer to fetch all the MessageSchema entries from the DB
     *
     * @return All the MessageSchema Details if available, if not emptyList
     *
     */

    public List<MessageSchemaResponseDto> getAllMessageSchema() {
        final List<MessageSchemaResponseDto> fetchedMessageSchemaDtos = new ArrayList<>();
        messageSchemaRepository.findAll().forEach((final MessageSchema messageSchemaEntity) -> {
            final MessageSchemaResponseDto queriedMessageSchemaDto = messageSchemaMapper.destinationToSource(messageSchemaEntity);
            mapEntityToResponse(messageSchemaEntity, queriedMessageSchemaDto);
            fetchedMessageSchemaDtos.add(queriedMessageSchemaDto);

        });
        return fetchedMessageSchemaDtos;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch particular MessageSchema entry from the DB using messageSchema id
     *
     * @param id
     *            - identifier of the MessageSchema
     * @return MessageSchema Details if available
     *
     */

    public MessageSchemaResponseDto getMessageSchemaById(String id) {
        MessageSchemaResponseDto queriedMessageSchemaDtoById = null;
        if (id != null && !id.isEmpty()) {
            logger.info("Catalog Service :: getMessageSchemaById() query by id : {}", id);
            try {
                final Optional<MessageSchema> isMessageSchemaEntityQueryByIdAvailable = messageSchemaRepository.findById(Integer.parseInt(id));
                if (isMessageSchemaEntityQueryByIdAvailable.isPresent()) {
                    queriedMessageSchemaDtoById = messageSchemaMapper.destinationToSource(isMessageSchemaEntityQueryByIdAvailable.get());
                    mapEntityToResponse(isMessageSchemaEntityQueryByIdAvailable.get(), queriedMessageSchemaDtoById);

                }
            } catch (final NumberFormatException invalidIdException) {
                logger.error("Catalog Service :: getMessageSchemaById() Invalid messageSchema identifier provided");
                throw new NumberFormatException("Invalid identifier provided for querying messageSchema");
            }
        }
        return queriedMessageSchemaDtoById;
    }

}