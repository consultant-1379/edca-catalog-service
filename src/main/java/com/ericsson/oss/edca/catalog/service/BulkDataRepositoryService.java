/*******************************************************************************

* COPYRIGHT Ericsson 2020
*
* The copyright to the computer program(s) herein is the property of
*
* Ericsson Inc. The programs may be used and/or copied only with written
*
* permission from Ericsson Inc. or in accordance with the terms and
*
* conditions stipulated in the agreement/contract under which the
*
* program(s) have been supplied.
* 
******************************************************************************/
package com.ericsson.oss.edca.catalog.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.controller.dto.BulkDataRepositoryDTO;
import com.ericsson.oss.edca.catalog.domain.BulkDataRepository;
import com.ericsson.oss.edca.catalog.domain.utility.BulkDataRepositoryMapper;
import com.ericsson.oss.edca.catalog.repository.BulkDataRepositoryRepo;

@Service
public class BulkDataRepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(BulkDataRepositoryService.class);

    @Autowired
    private BulkDataRepositoryRepo bdrRepo;

    protected static final BulkDataRepositoryMapper modelMapper = BulkDataRepositoryMapper.MAPPER_INSTANCE;

    /**
     * This Service method is used to interact with the repository layer to persist
     * the entities/details in the DB
     * 
     * @param dto - Bulk Data Repository Detail( BulkDataRepositoryDTO)
     * @return - saved/persisted BDR Entry in the DB
     */
    public BulkDataRepositoryDTO saveBDR(final BulkDataRepositoryDTO dto) {
	logger.info("BulkDataRepository-Service :: saveBDR");
	BulkDataRepository bdr = modelMapper.dtoToEntity(dto);
	bdrRepo.save(bdr);
	return modelMapper.entityToDTO(bdr);
    }

    /**
     * This Service method is used to interact with the repository layer to fetch
     * the particular entity/detail from the DB, using the nameSpace & name
     * attributes
     * 
     * @param nameSpace - nameSpace value of the particular Bulk Data Repository
     *                  (BulkDataRepositoryDTO)
     * @return - Bulk Data Repository Details if found in the DB, if not null
     */

    public List<BulkDataRepositoryDTO> getBDRByNameSpaceandName(final String nameSpace, final String name) {
	logger.info("BulkDataRepository-Service :: getBDR");
	return modelMapper.entityToDTOList(bdrRepo.findByNameSpaceAndName(nameSpace, name));
    }

    /**
     * This Service method is used to interact with the repository layer to fetch
     * all the BDR entries from the DB
     * 
     * @return - All the Bulk Data Repository Details if available, if not emptyList
     */

    public List<BulkDataRepositoryDTO> getAllBDR() {
	logger.info("BulkDataRepository-Service :: getallBDR {}", bdrRepo.findAll());
	return modelMapper.entityToDTOList(bdrRepo.findAll());
    }

    /**
     * This Service method is used to interact with the repository layer to fetch
     * the particular BDR entry from the DB, Using id attribute
     * 
     * @param id - Id value of the particular BDR Entry
     * @return - Bulk Data Repository Details if found in the DB, if not null
     */

    public BulkDataRepositoryDTO getBDRById(final int id) {
	logger.info("BulkDataRepository-Service :: getBDRById");
	return modelMapper.entityToDTO(bdrRepo.findById(id).orElse(null));
    }

}
