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
import com.ericsson.oss.edca.catalog.controller.dto.MessageStatusTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.domain.MessageStatusTopic;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;
import com.ericsson.oss.edca.catalog.repository.MessageStatusTopicRepository;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MessageStatusTopicServiceTest.class)
public class MessageStatusTopicServiceTest {

    @InjectMocks
    private MessageStatusTopicService messageStatusTopicService;

    @Mock
    private MessageStatusTopicRepository messageStatusTopicRepository;

    @Mock
    private MessageBusRepository messageBusRepository;

    private MessageStatusTopicDto registeredMessageStatusTopicDto;

    private MessageStatusTopic messageStatusTopic;

    private MessageBus messageBus;

    @BeforeEach
    public void setup() {
        registeredMessageStatusTopicDto = new MessageStatusTopicDto();
        registeredMessageStatusTopicDto.setId(1);
        registeredMessageStatusTopicDto.setName("topic1");
        registeredMessageStatusTopicDto.setEncoding(MessageEncoding.JSON);
        registeredMessageStatusTopicDto.setMessageBusId(1);
        registeredMessageStatusTopicDto.setSpecificationReference("specref1");
        registeredMessageStatusTopicDto.setMessageDataTopicIds(Collections.emptySet());

        messageBus = new MessageBus();
        messageBus.setId(1);
        messageBus.setName("messageBus1");
        messageBus.setNameSpace("test");
        messageBus.setAccessEndpoints(Collections.emptySet());
        messageBus.setClusterName("test");

        messageStatusTopic = new MessageStatusTopic();
        messageStatusTopic.setEncoding(MessageEncoding.JSON);
        messageStatusTopic.setId(101);
        messageStatusTopic.setName("topic1");
        messageStatusTopic.setSpecificationReference("specref1");
        messageStatusTopic.setMessageBus(messageBus);
    }

    @Test
    void saveMessageStatusTopicTest() throws Exception {
        Mockito.when(messageBusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(messageStatusTopic.getMessageBus()));
        Mockito.when(messageStatusTopicRepository.save(Mockito.any(MessageStatusTopic.class))).thenReturn(messageStatusTopic);
        final MessageStatusTopicDto receivedMessageStatusTopicDto = new MessageStatusTopicDto();
        receivedMessageStatusTopicDto.setEncoding(MessageEncoding.JSON);
        receivedMessageStatusTopicDto.setName("topic1");
        receivedMessageStatusTopicDto.setSpecificationReference("specref");
        receivedMessageStatusTopicDto.setMessageBusId(1);
        assertNotNull(messageStatusTopicService.createMessageStatusTopic(receivedMessageStatusTopicDto));
    }

    @Test
    public void testPostMessageStatusTopicByNonExistingMessageBus() throws Exception {
        Exception exception = assertThrows(CatalogException.class, () -> {
            messageStatusTopicService.createMessageStatusTopic(registeredMessageStatusTopicDto);
        });
        assertEquals("messageBus not found for messageStatusTopic", exception.getMessage());
    }

    @Test
    void getMessageStatusTopicTestById() {
        when(messageStatusTopicRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(messageStatusTopic));
        MessageStatusTopicDto check = (messageStatusTopicService.getMessageStatusTopicById(registeredMessageStatusTopicDto.getId()));
        assertThat(registeredMessageStatusTopicDto).isEqualToComparingOnlyGivenFields(check, "name", "specificationReference", "encoding");
    }

    @Test
    void getMessageStatusTopicByNameAndMessageBusId() {
        when(messageStatusTopicRepository.findByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(Optional.of(messageStatusTopic));
        MessageStatusTopicDto check = (messageStatusTopicService.getMessageStatusTopicByNameAndMessageBusId(registeredMessageStatusTopicDto.getName(),
                registeredMessageStatusTopicDto.getMessageBusId()));
        assertThat(registeredMessageStatusTopicDto).isEqualToComparingOnlyGivenFields(check, "name", "specificationReference", "encoding");
    }

    @Test
    void getAllMessageStatusTopicsTest() {
        when(messageStatusTopicRepository.findAll()).thenReturn(Arrays.asList(messageStatusTopic));
        assertThat(messageStatusTopicService.getAllMessageStatusTopics()).anyMatch(s -> s.getSpecificationReference() == registeredMessageStatusTopicDto.getSpecificationReference());
    }

    @Test
    public void testPostMessageStatusTopicTypeByNonExistingMessageBus() throws Exception {
        Exception exception = assertThrows(CatalogException.class, () -> {
            messageStatusTopicService.createMessageStatusTopic(registeredMessageStatusTopicDto);
        });
        assertEquals("messageBus not found for messageStatusTopic", exception.getMessage());
    }

}
