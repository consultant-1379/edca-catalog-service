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

import com.ericsson.oss.edca.catalog.controller.dto.MessageDataTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageDataTopic;

@Mapper(componentModel = "spring")
public abstract class MessageDataTopicMapper {

    public static final MessageDataTopicMapper INSTANCE = Mappers.getMapper(MessageDataTopicMapper.class);

    public abstract MessageDataTopicDto entityToDto(MessageDataTopic destination);

    public abstract MessageDataTopic dtoToEntity(MessageDataTopicDto source);

    public abstract List<MessageDataTopicDto> entityToDTOList(Iterable<MessageDataTopic> iterable);

}