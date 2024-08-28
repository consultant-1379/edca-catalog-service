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

import com.ericsson.oss.edca.catalog.controller.dto.NotificationTopicDto;
import com.ericsson.oss.edca.catalog.domain.NotificationTopic;

@Mapper(componentModel = "spring")
public abstract class NotificationTopicMapper {

    public static final NotificationTopicMapper INSTANCE = Mappers.getMapper(NotificationTopicMapper.class);

    public abstract NotificationTopicDto entityToDto(NotificationTopic destination);

    public abstract NotificationTopic dtoToEntity(NotificationTopicDto source);

    public abstract List<NotificationTopicDto> entityToDTOList(Iterable<NotificationTopic> iterable);

}