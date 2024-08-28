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

import com.ericsson.oss.edca.catalog.controller.dto.NotificationTopicDto;
import com.ericsson.oss.edca.catalog.domain.MessageBus;
import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.ericsson.oss.edca.catalog.domain.NotificationTopic;
import com.ericsson.oss.edca.catalog.repository.MessageBusRepository;
import com.ericsson.oss.edca.catalog.repository.NotificationTopicRepository;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(NotificationTopicServiceTest.class)
public class NotificationTopicServiceTest {

    @InjectMocks
    private NotificationTopicService service;

    @Mock
    private NotificationTopicRepository ntRepo;

    @Mock
    private MessageBusRepository msgRepo;

    private NotificationTopic ntModel;

    private NotificationTopicDto ntDTO;

    private MessageBus msgBus;

    @BeforeEach
    void setupDTO() {
        ntDTO = new NotificationTopicDto();
        ntDTO.setId(1);
        ntDTO.setName("topic1");
        ntDTO.setEncoding(MessageEncoding.JSON);
        ntDTO.setMessageBusId(1);
        ntDTO.setSpecificationReference("specref1");
        ntDTO.setFileFormatIds(Collections.emptySet());

        msgBus = new MessageBus();
        msgBus.setId(1);
        msgBus.setName("msgBus100");
        msgBus.setNameSpace("test");
        msgBus.setAccessEndpoints(Collections.emptySet());
        msgBus.setClusterName("test");

        ntModel = new NotificationTopic();
        ntModel.setEncoding(MessageEncoding.JSON);
        ntModel.setId(101);
        ntModel.setName("topic1");
        ntModel.setSpecificationReference("specref1");
        ntModel.setMessageBus(msgBus);

    }

    @Test
    void saveNotificationTopicTest() {
        when(msgRepo.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(msgBus));
        NotificationTopicDto check = (service.createNotificationTopic(ntDTO));
        assertThat(ntDTO).isEqualToComparingOnlyGivenFields(check, "name", "specificationReference", "encoding");
    }

    @Test
    void getNotificationTopicTestById() {
        when(ntRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(ntModel));
        NotificationTopicDto check = (service.getNotificationTopicById(ntDTO.getId()));
        assertThat(ntDTO).isEqualToComparingOnlyGivenFields(check, "name", "specificationReference", "encoding");
    }

    @Test
    void getNotificationTopicByNameAndMessageBusId() {
        when(ntRepo.findByNameAndMessageBusId(Mockito.any(String.class), Mockito.anyInt())).thenReturn(Optional.of(ntModel));
        NotificationTopicDto check = (service.getNotificationTopicByNameAndMessageBusId(ntDTO.getName(), ntDTO.getMessageBusId()));
        assertThat(ntDTO).isEqualToComparingOnlyGivenFields(check, "name", "specificationReference", "encoding");
    }

    @Test
    void getAllNotifcationTopicsTest() {
        when(ntRepo.findAll()).thenReturn(Arrays.asList(ntModel));
        assertThat(service.getAllNotificationTopics()).anyMatch(s -> s.getSpecificationReference() == ntDTO.getSpecificationReference());
    }
}
