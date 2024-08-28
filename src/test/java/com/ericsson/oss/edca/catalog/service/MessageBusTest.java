/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
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
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.ericsson.oss.edca.catalog.controller.dto.MessageBusDto;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;

@WebMvcTest(MessageBusTest.class)
public class MessageBusTest {

    @InjectMocks
    private MessageBusService messageBusService;

    @Mock
    private MessageBusRepository messageBusRepository;

    private MessageBus msgBusModel;

    private MessageBusDto msgBusDto;

    @BeforeEach
    void setupDTO() {
        msgBusDto = new MessageBusDto();
        msgBusDto.setId(101);
        msgBusDto.setName("test");
        msgBusDto.setNameSpace("testSpace");
        msgBusDto.setClusterName("testCluster");
        msgBusDto.setNotificationTopicIds(Collections.emptySet());
        msgBusDto.setMessageStatusTopicIds(Collections.emptySet());
        msgBusDto.setMessageDataTopicIds(Collections.emptySet());
        msgBusDto.setAccessEndpoints(Stream.of("test1", "test2").collect(Collectors.toSet()));

        msgBusModel = new MessageBus();
        msgBusModel.setId(101);
        msgBusModel.setName("test");
        msgBusModel.setNameSpace("testSpace");
        msgBusModel.setClusterName("testCluster");
        msgBusModel.setAccessEndpoints(Stream.of("test1", "test2").collect(Collectors.toSet()));
    }

    @Test
    void createMessageBusEntryTest() {
        assertThat(msgBusDto).isEqualToComparingFieldByField(messageBusService.createMessageBus(msgBusDto));
    }

    @Test
    void getMessageBusEntryByIdTest() {
        Mockito.when(messageBusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(msgBusModel));
        assertThat(messageBusService.getMessageBusById(msgBusDto.getId())).isEqualToIgnoringNullFields(msgBusDto);
    }

    @Test
    void getMessageBusEntryByNamsepaceAndNameTest() {
        when(messageBusRepository.findMessageBusByNameAndNamespace(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(msgBusModel));
        messageBusService.getMessageBusByNameAndNamespace(msgBusDto.getNameSpace(), msgBusDto.getName()).stream().forEach(s -> assertThat(s).isEqualToIgnoringGivenFields(msgBusDto, "id"));

    }

    @Test
    void getMessageBusByQueryFailTest() {
        when(messageBusRepository.findMessageBusByNameAndNamespace(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(Collections.emptyList());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            messageBusService.getMessageBusByNameAndNamespace(msgBusDto.getName(), msgBusDto.getClusterName());
        });

    }

    @Test
    void getAllMessageBusEntriesTest() {
        when(messageBusRepository.findAll()).thenReturn(Arrays.asList(msgBusModel));
        assertThat(messageBusService.getMessageBuses()).anyMatch(s -> s.getClusterName() == msgBusDto.getClusterName());
    }

}
