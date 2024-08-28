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
package com.ericsson.oss.edca.catalog.domain.utility;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ericsson.oss.edca.catalog.controller.dto.DataProviderTypeDto;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;

@Mapper(componentModel = "spring")
public abstract class DataProviderTypeMapper {
    public static final DataProviderTypeMapper INSTANCE = Mappers.getMapper(DataProviderTypeMapper.class);

    public abstract DataProviderType sourceToDestination(DataProviderTypeDto source);

    @Mapping(target = "dataSpaceId", source = "dataSpace.id")
    public abstract DataProviderTypeDto destinationToSource(DataProviderType destination);
}
