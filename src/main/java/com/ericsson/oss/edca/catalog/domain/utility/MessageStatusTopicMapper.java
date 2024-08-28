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

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ericsson.oss.edca.catalog.controller.dto.MessageStatusTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageStatusTopic;

@Mapper(componentModel = "spring")
public abstract class MessageStatusTopicMapper {

    public static final MessageStatusTopicMapper INSTANCE = Mappers.getMapper(MessageStatusTopicMapper.class);

    public abstract MessageStatusTopicDto entityToDto(MessageStatusTopic destination);

    public abstract MessageStatusTopic dtoToEntity(MessageStatusTopicDto source);

    public abstract List<MessageStatusTopicDto> entityToDTOList(Iterable<MessageStatusTopic> iterable);

}