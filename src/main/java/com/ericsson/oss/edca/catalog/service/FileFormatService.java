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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatDto;
import com.ericsson.oss.edca.catalog.controller.dto.FileFormatResponseDto;
import com.ericsson.oss.edca.catalog.domain.BulkDataRepository;
import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.DataCollector;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;
import com.ericsson.oss.edca.catalog.domain.DataSpace;
import com.ericsson.oss.edca.catalog.domain.FileFormat;
import com.ericsson.oss.edca.catalog.domain.NotificationTopic;
import com.ericsson.oss.edca.catalog.domain.utility.BulkDataRepositoryMapper;
import com.ericsson.oss.edca.catalog.domain.utility.DataCollectorMapper;
import com.ericsson.oss.edca.catalog.domain.utility.DataProviderTypeMapper;
import com.ericsson.oss.edca.catalog.domain.utility.FileFormatMapper;
import com.ericsson.oss.edca.catalog.domain.utility.NotificationTopicMapper;
import com.ericsson.oss.edca.catalog.repository.BulkDataRepositoryRepo;
import com.ericsson.oss.edca.catalog.repository.DataCollectorRepository;
import com.ericsson.oss.edca.catalog.repository.DataProviderTypeRepository;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;
import com.ericsson.oss.edca.catalog.repository.FileFormatRepository;
import com.ericsson.oss.edca.catalog.repository.NotificationTopicRepository;

@Service
public class FileFormatService {
    private static final Logger logger = LoggerFactory.getLogger(FileFormatService.class);

    private static final FileFormatMapper fileFormatMapper = FileFormatMapper.INSTANCE;

    @Autowired
    private DataProviderTypeMapper dataProviderTypeMapper;

    @Autowired
    private DataCollectorMapper dataCollectorMapper;

    @Autowired
    private BulkDataRepositoryMapper bulkDataRepositoryMapper;

    @Autowired
    private NotificationTopicMapper notificationTopicMapper;

    @Autowired
    private DataProviderTypeRepository dataProviderTypeRepository;

    @Autowired
    private DataCollectorRepository dataCollectorRepository;

    @Autowired
    private NotificationTopicRepository notificationTopicRepository;

    @Autowired
    private BulkDataRepositoryRepo bulkDataRepositoryRepo;

    @Autowired
    private FileFormatRepository fileFormatRepository;

    @Autowired
    private DataSpaceRepository dataSpaceRepository;

    /**
     * Utility method is used to interact with the repository layer to validate the fileFormat request details before persisting
     *
     * @param fileFormatDto
     *            - fileFormat request details
     * @param fileFormatToPersist
     *            - fileFormat to persist
     * @return
     *
     */
    private Map<String, String> validateFileFormat(final FileFormatDto fileFormatDto, final FileFormat fileFormatToPersist) {
        final Map<String, String> propertyErrorMessageMap = new HashMap<>();
        final Optional<DataProviderType> isDataProviderTypeAvailable = dataProviderTypeRepository.findById(fileFormatDto.getDataProviderTypeId());
        if (!isDataProviderTypeAvailable.isPresent()) {
            logger.error("Catalog Service :: validateFileFormat :: DataProviderType with id {} not found", fileFormatDto.getDataProviderTypeId());
            propertyErrorMessageMap.put("dataProviderTypeId", CatalogConstants.FILEFORMAT_DATAPROVIDERTYPE_NOT_FOUND_MSG);
        } else {
            final DataProviderType associatedDataProviderType = isDataProviderTypeAvailable.get();
            fileFormatToPersist.setDataProviderType(associatedDataProviderType);

        }
        final List<DataCollector> isDataCollectorAvailable = dataCollectorRepository
                .findDistinctDataCollectorByCollectorId(fileFormatDto.getDataCollectorId().toString());
        if (isDataCollectorAvailable == null || isDataCollectorAvailable.isEmpty()) {
            logger.error("Catalog Service :: validateFileFormat :: DataCollector with id {} not found", fileFormatDto.getDataCollectorId());
            propertyErrorMessageMap.put("dataCollectorId", CatalogConstants.FILEFORMAT_DATACOLLECTOR_NOT_FOUND_MSG);
        } else {
            // Each dataCollector is registered with unique collectorId
            final DataCollector associatedDataCollector = isDataCollectorAvailable.get(0);
            fileFormatToPersist.setDataCollector(associatedDataCollector);

        }
        final Optional<NotificationTopic> isNotificationTopicAvailable = notificationTopicRepository.findById(fileFormatDto.getNotificationTopicId());
        if (!isNotificationTopicAvailable.isPresent()) {
            logger.error("Catalog Service :: validateFileFormat :: NotificationTopic with id {} not found", fileFormatDto.getNotificationTopicId());
            propertyErrorMessageMap.put("notificationTopicId", CatalogConstants.FILEFORMAT_NOTIFICATION_TOPIC_NOT_FOUND_MSG);
        } else {
            final NotificationTopic associatedNotificationTopic = isNotificationTopicAvailable.get();
            fileFormatToPersist.setNotificationTopic(associatedNotificationTopic);

        }
        final Optional<BulkDataRepository> isBulkDataRepositoryAvailable = bulkDataRepositoryRepo.findById(fileFormatDto.getBulkDataRepositoryId());
        if (!isBulkDataRepositoryAvailable.isPresent()) {
            logger.error("Catalog Service :: validateFileFormat :: BulkDataRepository with id {} not found", fileFormatDto.getDataCollectorId());
            propertyErrorMessageMap.put("bulkDataRepositoryId", CatalogConstants.FILEFORMAT_BULK_DATA_REPO_NOT_FOUND_MSG);
        } else {
            final BulkDataRepository associatedBulkDataRepository = isBulkDataRepositoryAvailable.get();
            fileFormatToPersist.setBulkDataRepository(associatedBulkDataRepository);

        }
        return propertyErrorMessageMap;

    }

    private void mapEntityToResponse(final FileFormat fileFormat, final FileFormatResponseDto fileFormatResponseDto) {
        fileFormatResponseDto.setDataCollector(dataCollectorMapper.destinationToSource(fileFormat.getDataCollector()));
        fileFormatResponseDto.setBulkDataRepository(bulkDataRepositoryMapper.entityToDTO(fileFormat.getBulkDataRepository()));
        fileFormatResponseDto.setNotificationTopic(notificationTopicMapper.entityToDto(fileFormat.getNotificationTopic()));
        fileFormatResponseDto.setDataProviderType(dataProviderTypeMapper.destinationToSource(fileFormat.getDataProviderType()));
    }

    /**
     * This Service method is used to interact with the repository layer to persist the FileFormat entities/details in the DB
     *
     * @param fileFormatDtoToRegister
     *            - FileFormat Detail( FileFormatDto )
     * @return saved/persisted FileFormat Entry in the DB
     *
     */
    public FileFormatResponseDto saveFileFormat(final FileFormatDto fileFormatDtoToRegister) throws CatalogException {
        final FileFormat fileFormatToPersist = new FileFormat();
        fileFormatToPersist.setDataEncoding(fileFormatDtoToRegister.getDataEncoding());
        fileFormatToPersist.setReportOutputPeriodList(fileFormatDtoToRegister.getReportOutputPeriodList());
        fileFormatToPersist.setSpecificationReference(fileFormatDtoToRegister.getSpecificationReference());
        final Map<String, String> propertyErrorMessageMap = validateFileFormat(fileFormatDtoToRegister, fileFormatToPersist);
        logger.info("FileFormat to Persist : {}", fileFormatToPersist);
        if (propertyErrorMessageMap.size() > 0) {
            // prepare exception message with all properties which are non-existent entities
            final String fileFormatValidationErrorMsg = String.join(",", new ArrayList<String>(propertyErrorMessageMap.values()));
            throw new CatalogException(fileFormatValidationErrorMsg);
        }
        final FileFormat persistedFileFormat = fileFormatRepository.save(fileFormatToPersist);
        logger.info("Persisted FileFormat : {}", persistedFileFormat);
        final FileFormatResponseDto persistedFileFormatDto = fileFormatMapper.destinationToSource(persistedFileFormat);
        mapEntityToResponse(persistedFileFormat, persistedFileFormatDto);
        logger.info("Persisted FileFormatDto : {}", persistedFileFormatDto);
        return persistedFileFormatDto;

    }

    /**
     * This Service method is used to interact with the repository layer to fetch all the FileFormat entries from the DB
     *
     * @return All the FileFormat Details if available, if not emptyList
     *
     */
    public List<FileFormatResponseDto> getAllFileFormats() {
        final List<FileFormatResponseDto> fetchedFileFormatDtos = new ArrayList<>();
        fileFormatRepository.findAll().forEach((final FileFormat fileFormatEntity) -> {
            final FileFormatResponseDto queriedFileFormatDto = fileFormatMapper.destinationToSource(fileFormatEntity);
            mapEntityToResponse(fileFormatEntity, queriedFileFormatDto);
            fetchedFileFormatDtos.add(queriedFileFormatDto);

        });
        return fetchedFileFormatDtos;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch particular FileFormat entry from the DB using fileFormat id
     *
     * @param id
     *            - identifier of the FileFormat
     * @return FileFormat Details if available
     *
     */
    public FileFormatResponseDto getFileFormatById(final String id) {
        FileFormatResponseDto queriedFileFormatDtoById = null;
        if (id != null && !id.isEmpty()) {
            logger.info("Catalog Service :: getFileFormatById() query by id : {}", id);
            try {
                final Optional<FileFormat> isFileFormatEntityQueryByIdAvailable = fileFormatRepository.findById(Integer.parseInt(id));
                if (isFileFormatEntityQueryByIdAvailable.isPresent()) {
                    queriedFileFormatDtoById = fileFormatMapper.destinationToSource(isFileFormatEntityQueryByIdAvailable.get());
                    mapEntityToResponse(isFileFormatEntityQueryByIdAvailable.get(), queriedFileFormatDtoById);

                }
            } catch (final NumberFormatException invalidIdException) {
                logger.error("Catalog Service :: getFileFormatById() Invalid fileformat identifier provided");
                throw new NumberFormatException("Invalid identifier provided for querying fileFormat");
            }
        }
        return queriedFileFormatDtoById;
    }

    /**
     * Utility method is used to interact with the repository layer to fetch the particular FileFormat entries from the DB, for provided dataspace and
     * dataCategory
     *
     * @param queryParamDataspace
     *            - dataSpace name filter param
     * @param queryParamDataProviderType
     *            - queryParamDataProviderType identifier filter param
     * @param selectedFileFormatDtos
     *            - selected FileFormat entries
     * @return
     *
     */
    private void getFileFormatByDataSpaceAndProviderType(final String queryParamDataspace, final String queryParamDataProviderType,
                                                         final List<FileFormatResponseDto> selectedFileFormats) {
        DataProviderType selectedDataProviderType = null;
        final List<DataSpace> isDataSpaceAvailable = dataSpaceRepository.findDistinctDataSpaceByName(queryParamDataspace);
        // each Dataspace has unique name , so at most one entity returned
        if (isDataSpaceAvailable != null && isDataSpaceAvailable.size() == 1) {
            // find the associated dataProviderType for the dataSpace
            final List<DataProviderType> associatedDataProviderTypes = dataProviderTypeRepository.findByDataspaceName(queryParamDataspace);
            if (associatedDataProviderTypes != null && !associatedDataProviderTypes.isEmpty()) {
                for (final DataProviderType dataProviderType : associatedDataProviderTypes) {
                    // select the dataProviderType matching the provided dataProviderTypeId in the query
                    if (dataProviderType.getProviderTypeId().equals(queryParamDataProviderType)) {
                        selectedDataProviderType = dataProviderType;
                        break;
                    }
                }
            }
            if (selectedDataProviderType != null) {
                // find the associated fileformats for the selected dataProviderType from above
                final List<FileFormat> selectedFileFormatsByDataProviderTypeAndDataSpace = fileFormatRepository
                        .findByDataProviderType(selectedDataProviderType.getId());
                selectedFileFormatsByDataProviderTypeAndDataSpace.forEach((final FileFormat fileFormat) -> {
                    final FileFormatResponseDto fileFormatDto = fileFormatMapper.destinationToSource(fileFormat);
                    mapEntityToResponse(fileFormat, fileFormatDto);
                    selectedFileFormats.add(fileFormatDto);
                });
            }
        }
        logger.info("Catalog Service :: getFileFormatByDataSpaceAndProviderType :: Selected FileFormat by dataSpace AND dataProviderType : {}",
                selectedFileFormats);
    }

    /**
     * Utility method is used to interact with the repository layer to fetch the particular FileFormat entries from the DB, for provided dataspace and
     * dataCategory
     *
     * @param queryParamDataspace
     *            - dataSpace name filter param
     * @param queryParamDataCategory
     *            - dataCategory filter param
     * @param selectedFileFormatDtos
     *            - selected FileFormat entries
     * @return
     *
     */
    private void getFileFormatByDataSpaceAndOrDataCategory(final String queryParamDataspace, final String queryParamDataCategory,
                                                           final List<FileFormatResponseDto> selectedFileFormatDtos) {
        final List<DataProviderType> selectedDataProviderTypeEntities = new ArrayList<>();
        if (!queryParamDataCategory.isEmpty() && !queryParamDataspace.isEmpty()) {
            final List<DataProviderType> dataProviderTypesByDataSpaceAndCategory = dataProviderTypeRepository
                    .findByDataSpaceNameAndDataCategory(DataCategory.valueOf(queryParamDataCategory), queryParamDataspace);
            if (dataProviderTypesByDataSpaceAndCategory != null && !dataProviderTypesByDataSpaceAndCategory.isEmpty()) {
                selectedDataProviderTypeEntities.addAll(dataProviderTypesByDataSpaceAndCategory);
            }
        } else if (!queryParamDataCategory.isEmpty() || !queryParamDataspace.isEmpty()) {
            List<DataProviderType> dataProviderTypesByDataSpaceOrDataCategory = null;
            if (!queryParamDataCategory.isEmpty()) {
                dataProviderTypesByDataSpaceOrDataCategory = dataProviderTypeRepository
                        .findByDataCategory(DataCategory.valueOf(queryParamDataCategory));
            } else if (!queryParamDataspace.isEmpty()) {
                dataProviderTypesByDataSpaceOrDataCategory = dataProviderTypeRepository.findByDataspaceName(queryParamDataspace);
            }
            if (dataProviderTypesByDataSpaceOrDataCategory != null && !dataProviderTypesByDataSpaceOrDataCategory.isEmpty()) {
                selectedDataProviderTypeEntities.addAll(dataProviderTypesByDataSpaceOrDataCategory);
            }
        }
        selectedDataProviderTypeEntities.forEach((final DataProviderType dataProviderType) -> {
            final List<FileFormat> selectedFileFormats = fileFormatRepository.findByDataProviderType(dataProviderType.getId());
            selectedFileFormats.forEach((final FileFormat fileFormat) -> {
                final FileFormatResponseDto selectedFileFormatDto = fileFormatMapper.destinationToSource(fileFormat);
                mapEntityToResponse(fileFormat, selectedFileFormatDto);
                selectedFileFormatDtos.add(selectedFileFormatDto);
            });
        });
        logger.info("Catalog Service :: getFileFormatByDataSpaceAndOrDataCategory :: Selected fileFormat by DataSpace AND/OR DataCategory : {}",
                selectedFileFormatDtos);
    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular FileFormat entries from the DB, Using query params
     *
     * @param queryParams
     *            - Supported Query Criteria: 1. All file formats for a dataspace and/or data category (e.g. all stats files for 5G, all stats files,
     *            all 5G file formats) 2 .All file formats for a dataspace and a data provider type (e.g. 5G CTR)
     * @return List of FileFormat entries matching criteria else emptyList
     */
    public List<FileFormatResponseDto> getFileFormatByQueryParams(final Map<String, String> queryParams) {
        final List<FileFormatResponseDto> selectedFileFormats = new ArrayList<>();
        final String queryParamDataspace = queryParams.getOrDefault("dataSpace", "");
        final String queryParamDataProviderType = queryParams.getOrDefault("dataProviderType", "");
        final String queryParamDataCategory = queryParams.getOrDefault("dataCategory", "");

        /*
         * Supported Query Criteria:
         *
         * Following combinations are supported: 1. All file formats for a dataspace and/or data category (e.g. all stats files for 5G, all stats
         * files, all 5G file formats) 2 .All file formats for a dataspace and a data provider type (e.g. 5G CTR)
         */

        if (!queryParamDataspace.isEmpty() && !queryParamDataProviderType.isEmpty()) {
            // Find all file formats for a dataspace and a data provider type (e.g. 5G CTR)
            getFileFormatByDataSpaceAndProviderType(queryParamDataspace, queryParamDataProviderType, selectedFileFormats);
        } else if (!queryParamDataspace.isEmpty() || !queryParamDataCategory.isEmpty()) {
            // Find all file formats for a dataspace and/or data category (e.g. all stats files for 5G, all stats files, all 5G file formats)
            getFileFormatByDataSpaceAndOrDataCategory(queryParamDataspace, queryParamDataCategory, selectedFileFormats);
        }
        return selectedFileFormats;
    }
}
