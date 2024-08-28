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
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.controller.dto.DataCollectorDto;
import com.ericsson.oss.edca.catalog.domain.DataCollector;
import com.ericsson.oss.edca.catalog.domain.utility.DataCollectorMapper;
import com.ericsson.oss.edca.catalog.repository.DataCollectorRepository;

@Service
public class DataCollectorService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DataCollectorService.class);

    private static final DataCollectorMapper dataCollectorMapper = DataCollectorMapper.INSTANCE;

    /**
     * This Service method is used to interact with the repository layer to persist the DataCollector details in the DB
     *
     * @param dataCollectorDtoToPersist
     *            - DataCollector Detail( DataCollectorDto )
     * @return saved/persisted DataCollectorDto Entry in the DB
     *
     */
    @Autowired
    private DataCollectorRepository dataCollectorRepository;

    public DataCollectorDto saveDataCollector(final DataCollectorDto dataCollectorDtoToPersist) {
        DataCollectorDto persistedDataCollectorDto = null;
        final DataCollector persistedDataCollector = dataCollectorRepository.save(dataCollectorMapper.sourceToDestination(dataCollectorDtoToPersist));
        persistedDataCollectorDto = dataCollectorMapper.destinationToSource(persistedDataCollector);
        logger.info("CatalogService :: saveDataCollector :: persisted data collector : {}", persistedDataCollector);
        return persistedDataCollectorDto;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch all the DataCollector entries from the DB
     *
     * @return All the DataCollector if available, if not emptyList
     *
     */
    public List<DataCollectorDto> getAllDataCollector() {
        final List<DataCollectorDto> fetchedDataCollectorDtos = new ArrayList<>();
        dataCollectorRepository.findAll().forEach((final DataCollector dataCollectorEntity) -> {
            final DataCollectorDto queriedDataCollectorDto = dataCollectorMapper.destinationToSource(dataCollectorEntity);
            fetchedDataCollectorDtos.add(queriedDataCollectorDto);

        });
        logger.info("CatalogService :: getAllDataCollector :: fetched data collectors : {}", fetchedDataCollectorDtos);
        return fetchedDataCollectorDtos;
    }

}
