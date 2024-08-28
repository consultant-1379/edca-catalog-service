package com.ericsson.oss.edca.catalog.service;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.edca.catalog.controller.dto.NotificationTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.NotificationTopic;
import com.ericsson.oss.edca.catalog.domain.utility.NotificationTopicMapper;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;
import com.ericsson.oss.edca.catalog.repository.NotificationTopicRepository;

@Service
public class NotificationTopicService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationTopicService.class);

    @Autowired
    private  NotificationTopicRepository notificationTopicRepository;

    @Autowired
    private  MessageBusRepository messageBusRepository;

    private NotificationTopicMapper notificationTopicMapper = NotificationTopicMapper.INSTANCE;

    /**
     * This Service method is used to interact with the repository layer to persist
     * the entities/details in the DB
     *
     * @param dto
     *              - Notification Topic Details (NotificationTopicDto)
     *
     * @return  saved/persisted Notification Topic Entry in the DB
     *
     */

    public NotificationTopicDto createNotificationTopic(NotificationTopicDto notificationTopicDto) throws EntityNotFoundException {

        logger.info("Catalog Service :: createNotificationTopic");

        logger.info("Catalog Service :: POST Request:: Saving Notification Topic Entry", notificationTopicDto);

        NotificationTopic notificationTopic = notificationTopicMapper.dtoToEntity(notificationTopicDto);
        MessageBus messageBus = messageBusRepository.findById(notificationTopicDto.getMessageBusId()).orElseThrow(EntityNotFoundException::new);
        notificationTopic.setMessageBus(messageBus);
        notificationTopicRepository.save(notificationTopic);
        return notificationTopicMapper.entityToDto(notificationTopic);

    }

    /**
     * This Service method is used to interact with the repository layer to fetch
     * the particular entity/detail from the DB, using the name & msgBusId
     * attributes
     *
     * @param name
     *            - Name of the particular Notification Topic(NotificationTopicDto)
     * @param msgBusId 
     *             - Message Bus Id of the particular Notification Topic(NotificationTopicDto)
     *
     * @return - Notification Topic Details if
     *                   found in the DB, if not null
     *
     */

    public NotificationTopicDto getNotificationTopicByNameAndMessageBusId(String name, int msgBusId) {

        logger.info("Catalog Service :: getNotificationTopic by name and message bus id");

        return notificationTopicMapper.entityToDto(notificationTopicRepository.findByNameAndMessageBusId(name,msgBusId).orElse(null));
    }

    /**
     * This Service method is used to interact with the repository layer to fetch
     * all the Notification Topic entries from the DB
     *
     * @return - All the Notification Topic Details if available, if not emptyList
     *
     */

    public List<NotificationTopicDto> getAllNotificationTopics(){

        logger.info("Catalog Service :: getAllNotificationTopics", notificationTopicRepository.findAll());

        return notificationTopicMapper.entityToDTOList(notificationTopicRepository.findAll());

    }

    /**
    * This Service method is used to interact with the repository layer to fetch
    * the particular Notification Topic entry from the DB, Using id attribute
    *
    * @param id 
    *         - Id value of the particular Notification Topic Entry
    *
    * @return  Notification Topic Details if found in the DB, if not null
    *
    */

    public NotificationTopicDto getNotificationTopicById(int id) {

        logger.info("Catalog Service :: getNotificationTopic by id");

        return notificationTopicMapper.entityToDto(notificationTopicRepository.findById(id).orElse(null));
    }

}

