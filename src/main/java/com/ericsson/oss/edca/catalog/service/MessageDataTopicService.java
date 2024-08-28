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
import com.ericsson.oss.edca.catalog.controller.dto.MessageDataTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageDataTopic;
import com.ericsson.oss.edca.catalog.domain.MessageStatusTopic;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.utility.MessageDataTopicMapper;
import com.ericsson.oss.edca.catalog.repository.MessageStatusTopicRepository;
import com.ericsson.oss.edca.catalog.repository.MessageDataTopicRepository;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;

@Service
public class MessageDataTopicService {

    private static final Logger logger = LoggerFactory.getLogger(MessageDataTopicService.class);

    @Autowired
    private MessageDataTopicRepository messageDataTopicRepository;

    @Autowired
    private MessageBusRepository messageBusRepository;

    @Autowired
    private MessageStatusTopicRepository messageStatusTopicRepository;

    private MessageDataTopicMapper messageDataTopicMapper = MessageDataTopicMapper.INSTANCE;

    /**
     * This Service method is used to interact with the repository layer to persist the Message Data Topic details.
     *
     * @param dto
     *            - Message Status Topic Details (MessageDataTopicDto)
     *
     * @return saved/persisted Message Data Topic Entry in the DB
     *
     */

    public MessageDataTopicDto createMessageDataTopic(MessageDataTopicDto messageDataTopicDto) throws CatalogException {

        MessageDataTopicDto persistedMessageDataTopicDto = null;
        final Optional<MessageBus> isMessageBusExists = messageBusRepository.findById(messageDataTopicDto.getMessageBusId());
        final Optional<MessageStatusTopic> isMessageStatusTopicExists = messageStatusTopicRepository.findById(messageDataTopicDto.getMessageStatusTopicId());
        logger.info("Message Bus found : {}", isMessageBusExists.isPresent());
        logger.info("Message Status Topic found : {}", isMessageStatusTopicExists.isPresent());
        if (isMessageBusExists.isPresent() && isMessageStatusTopicExists.isPresent()) {
            final MessageDataTopic messageDataTopicToRegister = messageDataTopicMapper.dtoToEntity(messageDataTopicDto);
            messageDataTopicToRegister.setMessageBus(isMessageBusExists.get());
            messageDataTopicToRegister.setMessageStatusTopic(isMessageStatusTopicExists.get());
            logger.info("MessageStatusTopic Entity to persist : {}", messageDataTopicToRegister);
            final MessageDataTopic registeredMessageDataTopic = messageDataTopicRepository.save(messageDataTopicToRegister);
            logger.info("Persisted MessageDataTopic : {}", registeredMessageDataTopic);
            persistedMessageDataTopicDto = messageDataTopicMapper.entityToDto(registeredMessageDataTopic);
            logger.info("Persisted Dto mapped : {}", persistedMessageDataTopicDto);
        } else {
            if (!isMessageBusExists.isPresent() && !isMessageStatusTopicExists.isPresent())
                throw new CatalogException(CatalogConstants.MESSAGEDATATOPIC_MESSAGEBUS_NOT_FOUND_MSG + ", " + CatalogConstants.MESSAGEDATATOPIC_MESSAGESTATUSTOPIC_NOT_FOUND_MSG);
            else if (isMessageBusExists.isPresent())
                throw new CatalogException(CatalogConstants.MESSAGEDATATOPIC_MESSAGESTATUSTOPIC_NOT_FOUND_MSG);
            else {
                throw new CatalogException(CatalogConstants.MESSAGEDATATOPIC_MESSAGEBUS_NOT_FOUND_MSG);
            }
        }
        return persistedMessageDataTopicDto;

    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular Message Data Topic from the DB, using the name & messageBusId params
     *
     * @param name
     *            - Name of the particular Message Data Topic (MessageDataTopicDto)
     * @param messageBusId
     *            - Message Bus Id of the particular Message Data Topic(MessageDataTopicDto)
     *
     * @return - Message Data Topic Details if found in the DB, if not null
     *
     */

    public MessageDataTopicDto getMessageDataTopicByNameAndMessageBusId(final String name, final int messageBusId) {

        logger.info("Catalog Service :: getMessageDataTopic by name and message bus id");

        return messageDataTopicMapper.entityToDto(messageDataTopicRepository.findByNameAndMessageBusId(name, messageBusId).orElse(null));
    }

    /**
     * This Service method is used to interact with the repository layer to fetch all the Message Data Topic entries from the DB
     *
     * @return - All the Message Data Topic Details if available, if not emptyList
     *
     */

    public List<MessageDataTopicDto> getAllMessageDataTopics() {

        logger.info("Catalog Service :: getAllMessageDataTopics {}", messageDataTopicRepository.findAll());

        return messageDataTopicMapper.entityToDTOList(messageDataTopicRepository.findAll());

    }

    /**
     * This Service method is used to interact with the repository layer to fetch the particular Message Data Topic entry from the DB, Using id attribute
     *
     * @param id
     *            - Id value of the particular Message Data Topic Entry
     *
     * @return Message Data Topic Details if found in the DB, if not null
     *
     */

    public MessageDataTopicDto getMessageDataTopicById(final int id) {

        logger.info("Catalog Service :: getMessageDataTopicById by id");

        return messageDataTopicMapper.entityToDto(messageDataTopicRepository.findById(id).orElse(null));
    }

}
