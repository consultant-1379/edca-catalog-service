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
package com.ericsson.oss.edca.catalog.domain.utility;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ericsson.oss.edca.catalog.controller.dto.DataCollectorDto;
import com.ericsson.oss.edca.catalog.domain.DataCollector;

@Mapper(imports = { UUID.class }, componentModel = "spring")
public abstract class DataCollectorMapper {

    public static final DataCollectorMapper INSTANCE = Mappers.getMapper(DataCollectorMapper.class);

    @Mapping(target = "collectorId", expression = "java( source.getCollectorId().toString() )")
    public abstract DataCollector sourceToDestination(DataCollectorDto source);

    @Mapping(target = "collectorId", expression = "java( UUID.fromString(destination.getCollectorId()) )")
    public abstract DataCollectorDto destinationToSource(DataCollector destination);

}
