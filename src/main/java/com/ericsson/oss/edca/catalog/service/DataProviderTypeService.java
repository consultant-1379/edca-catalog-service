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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.DataProviderTypeDto;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;
import com.ericsson.oss.edca.catalog.domain.DataSpace;
import com.ericsson.oss.edca.catalog.domain.utility.DataProviderTypeMapper;
import com.ericsson.oss.edca.catalog.repository.DataProviderTypeRepository;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;

@Service
public class DataProviderTypeService {

    private static final Logger logger = LoggerFactory.getLogger(DataProviderTypeService.class);

    @Autowired
    private DataProviderTypeRepository dataProviderTypeRepository;

    @Autowired
    private DataSpaceRepository dataspaceRepository;

    /**
     * This Service method is used to interact with the repository layer to persist the DataProviderType entities/details in the DB
     *
     * @param dataProviderTypeDto
     *            - DataProviderType Detail( DataProviderTypeDto )
     * @return saved/persisted DataProviderType Entry in the DB
     * @throws CatalogException
     */
    public DataProviderTypeDto saveDataProviderType(final DataProviderTypeDto dataProviderTypeDto) throws CatalogException {
        DataProviderTypeDto persistedDataProviderTypeDto = null;
        final Optional<DataSpace> isDataspaceExists = dataspaceRepository.findById(dataProviderTypeDto.getDataSpaceId());
        logger.info("Dataspace found : {}", isDataspaceExists.isPresent());
        if (isDataspaceExists.isPresent()) {
            final DataProviderType dataProviderTypeToRegister = DataProviderTypeMapper.INSTANCE.sourceToDestination(dataProviderTypeDto);
            dataProviderTypeToRegister.setDataSpace(isDataspaceExists.get());
            logger.info("DataProviderType Entity to persist : {}", dataProviderTypeToRegister);
            final DataProviderType registeredDataProviderType = dataProviderTypeRepository.save(dataProviderTypeToRegister);
            logger.info("Persisted dataProviderType : {}", registeredDataProviderType);
            persistedDataProviderTypeDto = DataProviderTypeMapper.INSTANCE.destinationToSource(registeredDataProviderType);
            logger.info("Persisted Dto mapped : {}", persistedDataProviderTypeDto);
        } else {
            // throwing the application exception for dataspace not found for dataProviderType
            throw new CatalogException(CatalogConstants.DATAPROVIDERTYPE_DATASPACE_NOT_FOUND_MSG);
        }
        return persistedDataProviderTypeDto;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch all the DataProviderType entries from the DB
     *
     * @return All the DataProviderType Details if available, if not emptyList
     *
     */
    public List<DataProviderTypeDto> getAllDataProviderTypes() {
        final List<DataProviderTypeDto> fetchedDataProviderTypeDtos = new ArrayList<>();
        dataProviderTypeRepository.findAll().forEach((final DataProviderType dataProviderTypeEntity) -> {
            final DataProviderTypeDto queriedDataProviderTypeDto = DataProviderTypeMapper.INSTANCE.destinationToSource(dataProviderTypeEntity);
            fetchedDataProviderTypeDtos.add(queriedDataProviderTypeDto);

        });
        return fetchedDataProviderTypeDtos;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular DataProviderType entry from the DB, Using dataspace
     * attribute
     *
     * @param dataspaceQueryName
     * @return All the DataProviderType Details queried by dataspace attribute if available, if not emptyList
     *
     */
    public List<DataProviderTypeDto> getAllDataProviderTypesByDataspace(final String dataspaceQueryName) {
        final List<DataProviderTypeDto> fetchedDataProviderTypeDtos = new ArrayList<>();
        dataProviderTypeRepository.findByDataspaceName(dataspaceQueryName).forEach((final DataProviderType dataProviderTypeEntity) -> {
            final DataProviderTypeDto queriedDataProviderTypeDto = DataProviderTypeMapper.INSTANCE.destinationToSource(dataProviderTypeEntity);
            fetchedDataProviderTypeDtos.add(queriedDataProviderTypeDto);
        });

        return fetchedDataProviderTypeDtos;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular DataProviderType entry from the DB, Using id
     * attribute
     *
     * @param dataProvideTypeId
     * @return
     *
     */
    public DataProviderTypeDto getDataProviderTypeById(final String dataProvideTypeId) {
        DataProviderTypeDto fetchedDataProviderTypeDto = null;
        final Optional<DataProviderType> isDataProviderTypeEntityFound = dataProviderTypeRepository.findById(Integer.parseInt(dataProvideTypeId));
        if (isDataProviderTypeEntityFound.isPresent()) {
            final DataProviderType queriedDataProviderTypeEntity = isDataProviderTypeEntityFound.get();
            fetchedDataProviderTypeDto = DataProviderTypeMapper.INSTANCE.destinationToSource(queriedDataProviderTypeEntity);
        }
        return fetchedDataProviderTypeDto;
    }

}
