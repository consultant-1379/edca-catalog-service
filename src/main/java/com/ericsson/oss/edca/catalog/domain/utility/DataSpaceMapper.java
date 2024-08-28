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

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.ericsson.oss.edca.catalog.controller.dto.DataSpaceDto;
import com.ericsson.oss.edca.catalog.domain.DataSpace;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class DataSpaceMapper {

    public static final DataSpaceMapper INSTANCE = Mappers.getMapper(DataSpaceMapper.class);

    public abstract DataSpace sourceToDestination(DataSpaceDto source);

    public abstract DataSpaceDto destinationToSource(DataSpace destination);

}