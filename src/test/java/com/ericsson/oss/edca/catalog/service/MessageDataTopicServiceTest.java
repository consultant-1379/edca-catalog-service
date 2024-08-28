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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.ericsson.oss.edca.catalog.common.CatalogException;
import com.ericsson.oss.edca.catalog.controller.dto.MessageDataTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.MessageDataTopic;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.domain.MessageStatusTopic;
import com.ericsson.oss.edca.catalog.repository.MessageDataTopicRepository;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;
import com.ericsson.oss.edca.catalog.repository.MessageStatusTopicRepository;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MessageDataTopicServiceTest.class)
public class MessageDataTopicServiceTest {

    @InjectMocks
    private MessageDataTopicService messageDataTopicService;

    @Mock
    private MessageDataTopicRepository messageDataTopicRepository;

    @Mock
    private MessageBusRepository messageBusRepository;

    @Mock
    private MessageStatusTopicRepository messageStatusTopicRepository;

    private MessageDataTopicDto registeredMessageDataTopicDto;

    private MessageDataTopic messageDataTopic;

    private MessageBus messageBus;

    private MessageStatusTopic messageStatusTopic;

    @BeforeEach
    public void setup() {
        registeredMessageDataTopicDto = new MessageDataTopicDto();
        registeredMessageDataTopicDto.setId(1);
        registeredMessageDataTopicDto.setName("topic1");
        registeredMessageDataTopicDto.setEncoding(MessageEncoding.JSON);
        registeredMessageDataTopicDto.setMessageBusId(1);
        registeredMessageDataTopicDto.setMessageStatusTopicId(1);
        registeredMessageDataTopicDto.setMessageSchemaIds(Collections.emptySet());

        messageBus = new MessageBus();
        messageBus.setId(1);
        messageBus.setName("messageBus1");
        messageBus.setNameSpace("test");
        messageBus.setAccessEndpoints(Collections.emptySet());
        messageBus.setClusterName("test");

        messageStatusTopic = new MessageStatusTopic();
        messageStatusTopic.setEncoding(MessageEncoding.JSON);
        messageStatusTopic.setId(1);
        messageStatusTopic.setMessageBus(messageBus);
        messageStatusTopic.setName("status-topic");
        messageStatusTopic.setSpecificationReference("spec-ref");

        messageDataTopic = new MessageDataTopic();
        messageDataTopic.setEncoding(MessageEncoding.JSON);
        messageDataTopic.setId(101);
        messageDataTopic.setName("topic1");
        messageDataTopic.setMessageStatusTopic(messageStatusTopic);
        messageDataTopic.setMessageBus(messageBus);
    }

    @Test
    void saveMessageDataTopicTest() throws Exception {
        Mockito.when(messageDataTopicRepository.save(Mockito.any(MessageDataTopic.class))).thenReturn(messageDataTopic);
        Mockito.when(messageBusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(messageDataTopic.getMessageBus()));
        Mockito.when(messageStatusTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(messageDataTopic.getMessageStatusTopic()));
        final MessageDataTopicDto receivedMessageDataTopicDto = new MessageDataTopicDto();
        receivedMessageDataTopicDto.setEncoding(MessageEncoding.JSON);
        receivedMessageDataTopicDto.setName("topic1");
        receivedMessageDataTopicDto.setMessageStatusTopicId(1);
        receivedMessageDataTopicDto.setMessageBusId(1);
        assertNotNull(messageDataTopicService.createMessageDataTopic(receivedMessageDataTopicDto));
    }

    @Test
    public void testPostMessageDataTopicByBothNonExisting() throws Exception {
        Exception exception = assertThrows(CatalogException.class, () -> {
            messageDataTopicService.createMessageDataTopic(registeredMessageDataTopicDto);
        });
        assertEquals("messageBus not found for messageDataTopic, messageStatusTopic not found for messageDataTopic", exception.getMessage());
    }

    @Test
    public void testPostMessageDataTopicByNonExistingMessageStatusTopic() throws Exception {
        Mockito.when(messageBusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(messageDataTopic.getMessageBus()));
        Exception exception = assertThrows(CatalogException.class, () -> {
            messageDataTopicService.createMessageDataTopic(registeredMessageDataTopicDto);
        });
        assertEquals("messageStatusTopic not found for messageDataTopic", exception.getMessage());
    }

    @Test
    public void testPostMessageDataTopicByNonExistingMessageBus() throws Exception {
        Mockito.when(messageStatusTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(messageDataTopic.getMessageStatusTopic()));
        Exception exception = assertThrows(CatalogException.class, () -> {
            messageDataTopicService.createMessageDataTopic(registeredMessageDataTopicDto);
        });
        assertEquals("messageBus not found for messageDataTopic", exception.getMessage());
    }

    @Test
    void getMessageStatusTopicTestById() {
        when(messageDataTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(messageDataTopic));
        MessageDataTopicDto check = (messageDataTopicService.getMessageDataTopicById(registeredMessageDataTopicDto.getId()));
        assertThat(registeredMessageDataTopicDto).isEqualToComparingOnlyGivenFields(check, "name", "encoding");
    }

    @Test
    void getMessageStatusTopicByNameAndMessageBusId() {
        when(messageDataTopicRepository.findByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(Optional.of(messageDataTopic));
        MessageDataTopicDto check = (messageDataTopicService.getMessageDataTopicByNameAndMessageBusId(registeredMessageDataTopicDto.getName(), registeredMessageDataTopicDto.getMessageBusId()));
        assertThat(registeredMessageDataTopicDto).isEqualToComparingOnlyGivenFields(check, "name", "encoding");
    }

    @Test
    void getAllMessageStatusTopicsTest() {
        when(messageDataTopicRepository.findAll()).thenReturn(Arrays.asList(messageDataTopic));
        assertThat(messageDataTopicService.getAllMessageDataTopics()).anyMatch(s -> s.getName() == registeredMessageDataTopicDto.getName());
    }

    @Test
    public void testPostMessageDataTopicTypeByNonExistingEntities() throws Exception {
        Exception exception = assertThrows(CatalogException.class, () -> {
            messageDataTopicService.createMessageDataTopic(registeredMessageDataTopicDto);
        });
        assertEquals("messageBus not found for messageDataTopic, messageStatusTopic not found for messageDataTopic", exception.getMessage());
    }

}
