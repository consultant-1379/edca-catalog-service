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
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.controller.dto.DataSpaceDto;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;
import com.ericsson.oss.edca.catalog.domain.DataSpace;
import com.ericsson.oss.edca.catalog.domain.utility.DataSpaceMapper;
import com.ericsson.oss.edca.catalog.repository.DataSpaceRepository;

@Service
public class DataspaceService {

    private static final Logger logger = LoggerFactory.getLogger(DataspaceService.class);

    @Autowired
    private DataSpaceRepository dataSpaceRepository;

    /**
     * This Service method is used to interact with the repository layer to persist the entities/details in the DB
     *
     * @param dataSpaceDto
     *            - Dataspace Detail( DataSpaceDto )
     * @return - saved/persisted Dataspace Entry in the DB
     */
    public DataSpaceDto saveDataspace(final DataSpaceDto dataSpaceDto) throws Exception {
        final DataSpace dataSpaceEntity = dataSpaceRepository.save(DataSpaceMapper.INSTANCE.sourceToDestination(dataSpaceDto));
        final DataSpaceDto convertedDataSpaceDto = DataSpaceMapper.INSTANCE.destinationToSource(dataSpaceEntity);
        return convertedDataSpaceDto;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch all the Dataspace entries from the DB
     *
     * @return - All the Dataspace Details if available, if not emptyList
     */
    public List<DataSpaceDto> getAllDataSpaces() throws Exception {
        final List<DataSpaceDto> fetchedDataSpaceDtos = new ArrayList<>();
        dataSpaceRepository.findAll().forEach((final DataSpace dataSpaceEntity) -> {
            final DataSpaceDto queriedDataSpaceDto = DataSpaceMapper.INSTANCE.destinationToSource(dataSpaceEntity);
            queriedDataSpaceDto
                    .setDataProviderTypeIds(dataSpaceEntity.getDataProviderTypes().stream().map(DataProviderType::getId).collect(Collectors.toSet()));
            fetchedDataSpaceDtos.add(queriedDataSpaceDto);

        });
        return fetchedDataSpaceDtos;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular Dataspace entry from the DB, Using name attribute
     *
     * @param name
     *            - name value of the particular Dataspace Entry
     * @return - Dataspace Details if found in the DB, if not null
     */
    public DataSpaceDto getDataSpaceByName(final String name) throws Exception {
        DataSpaceDto queriedDataSpaceDtoByName = null;
        // dataspace name is unique - so at most one instance returned
        if (name != null && !name.isEmpty()) {
            logger.info("Catalog Service :: getDataSpaceByNameOrId() query by name : {}", name);
            final List<DataSpace> queriedDataSpaceEntities = dataSpaceRepository.findDistinctDataSpaceByName(name);
            logger.debug("Catalog Service :: getDataSpaceByNameOrId() fetched entities : {}", queriedDataSpaceEntities);
            if (queriedDataSpaceEntities != null && queriedDataSpaceEntities.size() > 0) {
                queriedDataSpaceDtoByName = DataSpaceMapper.INSTANCE.destinationToSource(queriedDataSpaceEntities.get(0));
                queriedDataSpaceDtoByName.setDataProviderTypeIds(
                        queriedDataSpaceEntities.get(0).getDataProviderTypes().stream().map(DataProviderType::getId).collect(Collectors.toSet()));
            }
        }
        return queriedDataSpaceDtoByName;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular Dataspace entry from the DB, Using id attribute
     *
     * @param id
     *            - Id value of the particular Dataspace Entry
     * @return - Dataspace Details if found in the DB, if not null
     */
    public DataSpaceDto getDataSpaceById(final String id) throws Exception {
        DataSpaceDto queriedDataSpaceDtoById = null;
        if (id != null && !id.isEmpty()) {
            logger.info("Catalog Service :: getDataSpaceByNameOrId() query by id : {}", id);
            try {
                final Optional<DataSpace> isDataSpaceEntityQueryByIdAvailable = dataSpaceRepository.findById(Integer.parseInt(id));
                if (isDataSpaceEntityQueryByIdAvailable.isPresent()) {
                    queriedDataSpaceDtoById = DataSpaceMapper.INSTANCE.destinationToSource(isDataSpaceEntityQueryByIdAvailable.get());
                    queriedDataSpaceDtoById.setDataProviderTypeIds(isDataSpaceEntityQueryByIdAvailable.get().getDataProviderTypes().stream()
                            .map(DataProviderType::getId).collect(Collectors.toSet()));
                }
            } catch (final NumberFormatException invalidIdException) {
                logger.error("Catalog Service :: getDataSpaceByNameOrId() Invalid dataspace identifier provided");
                throw new NumberFormatException("Invalid identifier provided for querying dataspace");
            }
        }
        return queriedDataSpaceDtoById;
    }
}
