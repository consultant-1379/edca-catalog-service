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
package com.ericsson.oss.edca.catalog.domain.utility;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ericsson.oss.edca.catalog.controller.dto.BulkDataRepositoryDTO;
import com.ericsson.oss.edca.catalog.domain.BulkDataRepository;

@Mapper(componentModel = "spring")
public abstract class BulkDataRepositoryMapper {

    public static final BulkDataRepositoryMapper MAPPER_INSTANCE = Mappers.getMapper(BulkDataRepositoryMapper.class);

    public abstract BulkDataRepositoryDTO entityToDTO(BulkDataRepository bdr);

    public abstract BulkDataRepository dtoToEntity(BulkDataRepositoryDTO bdrDto);

    public abstract List<BulkDataRepositoryDTO> entityToDTOList(Iterable<BulkDataRepository> iterable);

}
