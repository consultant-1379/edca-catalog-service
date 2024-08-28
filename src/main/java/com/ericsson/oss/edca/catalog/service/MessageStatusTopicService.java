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
package com.ericsson.oss.edca.catalog.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.common.CatalogConstants;
import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.MessageStatusTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.MessageStatusTopic;
import com.ericsson.oss.edca.catalog.domain.utility.MessageStatusTopicMapper;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;
import com.ericsson.oss.edca.catalog.repository.MessageStatusTopicRepository;

@Service
public class MessageStatusTopicService {

    private static final Logger logger = LoggerFactory.getLogger(MessageStatusTopicService.class);

    @Autowired
    private MessageStatusTopicRepository messageStatusTopicRepository;

    @Autowired
    private MessageBusRepository messageBusRepository;

    private MessageStatusTopicMapper messageStatusTopicMapper = MessageStatusTopicMapper.INSTANCE;

    /**
     * This Service method is used to interact with the repository layer to persist the given messageStatusTopic details
     *
     * @param dto
     *            - Message Status Topic Details (MessageStatusTopicDto)
     *
     * @return saved/persisted Message Status Topic Entry in the DB
     *
     */

    public MessageStatusTopicDto createMessageStatusTopic(final MessageStatusTopicDto messageStatusTopicDto) throws CatalogException {

        MessageStatusTopicDto persistedMessageStatusTopicDto = null;
        final Optional<MessageBus> isMessageBusExists = messageBusRepository.findById(messageStatusTopicDto.getMessageBusId());
        logger.info("Message Bus found : {}", isMessageBusExists.isPresent());
        if (isMessageBusExists.isPresent()) {
            final MessageStatusTopic messageStatusTopicToRegister = messageStatusTopicMapper.dtoToEntity(messageStatusTopicDto);
            messageStatusTopicToRegister.setMessageBus(isMessageBusExists.get());
            logger.info("MessageStatusTopic Entity to persist : {}", messageStatusTopicToRegister);
            final MessageStatusTopic registeredMessageStatusTopic = messageStatusTopicRepository.save(messageStatusTopicToRegister);
            logger.info("Persisted MessageStatusTopic : {}", registeredMessageStatusTopic);
            persistedMessageStatusTopicDto = messageStatusTopicMapper.entityToDto(registeredMessageStatusTopic);
            logger.info("Persisted Dto mapped : {}", persistedMessageStatusTopicDto);
        } else {
            throw new CatalogException(CatalogConstants.MESSAGESTATUSTOPIC_MESSAGEBUS_NOT_FOUND_MSG);
        }
        return persistedMessageStatusTopicDto;
    }

    /**
     * This Service method is used to interact with the repository layer to fetch a message status topic, using the name & messageBusId as params
     *
     * @param name
     *            - Name of the particular Message Status Topic (MessageStatusTopicDto)
     * @param messageBusId
     *            - Message Bus Id of the particular Message Status Topic(MessageStatusTopicDto)
     *
     * @return - Message Status Topic Details if found in the DB, if not null
     *
     */

    public MessageStatusTopicDto getMessageStatusTopicByNameAndMessageBusId(final String name, final int messageBusId) {

        logger.info("Catalog Service :: getMessageStatusTopic by name and message bus id");

        return messageStatusTopicMapper.entityToDto(messageStatusTopicRepository.findByNameAndMessageBusId(name, messageBusId).orElse(null));
    }

    /**
     * This Service method is used to interact with the repository layer to fetch all the Message Status Topic entries from the DB
     *
     * @return - All the Message Status Topic Details if available, if not emptyList
     *
     */

    public List<MessageStatusTopicDto> getAllMessageStatusTopics() {

        logger.info("Catalog Service :: getAllMessageStatusTopics {}", messageStatusTopicRepository.findAll());

        return messageStatusTopicMapper.entityToDTOList(messageStatusTopicRepository.findAll());

    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular Message Status Topic entry from the DB, Using id attribute
     *
     * @param id
     *            - Id value of the particular Message Status Topic Entry
     *
     * @return Message Status Topic Details if found in the DB, if not null
     *
     */

    public MessageStatusTopicDto getMessageStatusTopicById(final int id) {

        logger.info("Catalog Service :: getMessageStatusTopicById by id");
        return messageStatusTopicMapper.entityToDto(messageStatusTopicRepository.findById(id).orElse(null));
    }

}
