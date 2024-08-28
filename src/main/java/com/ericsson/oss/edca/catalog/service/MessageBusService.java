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
package com.ericsson.oss.edca.catalog.service;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ericsson.oss.edca.catalog.controller.dto.MessageBusDto;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.utility.MessageBusMapper;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;

@Service
public class MessageBusService {

    private static final Logger logger = LoggerFactory.getLogger(MessageBusService.class);

    private final MessageBusRepository messageBusRepository;

    private MessageBusMapper messageBusMapper = MessageBusMapper.INSTANCE;

    public MessageBusService(@Autowired MessageBusRepository messageBusRepository) {
	this.messageBusRepository = messageBusRepository;
    }

    public MessageBusDto createMessageBus(final MessageBusDto messageBusDto) {
	logger.info("MessageBus Service :: createMessageBus");
	MessageBus messageBus = messageBusMapper.sourceToDestination(messageBusDto);
	messageBusRepository.save(messageBus);
	return messageBusMapper.destinationToSource(messageBus);
    }

    public List<MessageBusDto> getMessageBuses() {
	logger.info("MessageBus Service:: Get Message Bus");
	return StreamSupport
		.stream(Spliterators.spliteratorUnknownSize(messageBusRepository.findAll().iterator(),
			Spliterator.ORDERED), false)
		.map(messageBus -> messageBusMapper.destinationToSource(messageBus)).collect(Collectors.toList());
    }

    public MessageBusDto getMessageBusById(final int id) {
	logger.info("MessageBus Service:: Get Message Bus by id");
	return messageBusMapper
		.destinationToSource(messageBusRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public List<MessageBusDto> getMessageBusByNameAndNamespace(final String name, final String namespace) {
	logger.info("Get Request:: Get Message by name and namespace");
	List<MessageBusDto> messageBusDtos = StreamSupport
		.stream(Spliterators.spliteratorUnknownSize(
			messageBusRepository.findMessageBusByNameAndNamespace(name, namespace).iterator(),
			Spliterator.ORDERED), false)
		.map(messageBus -> messageBusMapper.destinationToSource(messageBus)).collect(Collectors.toList());
	if (CollectionUtils.isEmpty(messageBusDtos))
	    throw new EntityNotFoundException();
	return messageBusDtos;
    }

}
