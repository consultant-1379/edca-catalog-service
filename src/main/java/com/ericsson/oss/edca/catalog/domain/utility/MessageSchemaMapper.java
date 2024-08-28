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

import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaDto;
import com.ericsson.oss.edca.catalog.controller.dto.MessageSchemaResponseDto;
import com.ericsson.oss.edca.catalog.domain.MessageSchema;

@Mapper(componentModel = "spring", imports = { UUID.class })
public abstract class MessageSchemaMapper {

    public static final MessageSchemaMapper INSTANCE = Mappers.getMapper(MessageSchemaMapper.class);

    public abstract MessageSchema sourceToDestination(final MessageSchemaDto source);

    @Mapping(target = "dataCollector.collectorId", expression = "java( UUID.fromString(dataCollector.getCollectorId()) )")
    public abstract MessageSchemaResponseDto destinationToSource(final MessageSchema destination);
}
