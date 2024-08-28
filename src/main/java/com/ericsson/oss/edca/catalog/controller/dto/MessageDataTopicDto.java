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
package com.ericsson.oss.edca.catalog.controller.dto;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import javax.validation.constraints.NotNull;

import com.ericsson.oss.edca.catalog.domain.MessageEncoding;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class MessageDataTopicDto extends NamedDto {

    private MessageEncoding encoding;

    private Set<Integer> messageSchemaIds = Collections.emptySet();

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull(message = "messageBusId is a mandatory field which should not be empty")
    private Integer messageBusId;

    @JsonProperty(access = Access.READ_ONLY)
    private MessageBusDto messageBus;

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull(message = "messageStatusTopicId is a mandatory field which should not be empty")
    private Integer messageStatusTopicId;

    @JsonProperty(access = Access.READ_ONLY)
    private MessageStatusTopicDto messageStatusTopic;

    public void setEncoding(final MessageEncoding encoding) {
        this.encoding = encoding;
    }

    public MessageEncoding getEncoding() {
        return encoding;
    }

    public Set<Integer> getMessageSchemaIds() {
        return Collections.unmodifiableSet(messageSchemaIds);
    }

    public void setMessageSchemaIds(final Set<Integer> messageSchemaIds) {
        this.messageSchemaIds = new HashSet<>(messageSchemaIds);
    }

    public MessageBusDto getMessageBus() {
        return this.messageBus;
    }

    public void setMessageBus(final MessageBusDto messageBus) {
        this.messageBus = messageBus;
    }

    public void setMessageBusId(final Integer messageBusId) {
        this.messageBusId = messageBusId;
    }

    public int getMessageBusId() {
        return this.messageBusId;
    }

    public MessageStatusTopicDto getMessageStatusTopic() {
        return this.messageStatusTopic;
    }

    public void setMessageStatusTopic(final MessageStatusTopicDto messageStatusTopic) {
        this.messageStatusTopic = messageStatusTopic;
    }

    public void setMessageStatusTopicId(final Integer messageStatusTopicId) {
        this.messageStatusTopicId = messageStatusTopicId;
    }

    public int getMessageStatusTopicId() {
        return this.messageStatusTopicId;
    }

    @Override
    public String toString() {
        return "MessageDataTopicDto [encoding=" + encoding + ", messageSchemaIds=" + messageSchemaIds + ", messageBusId=" + messageBusId + ", messageBus=" + messageBus + ", messageStatusTopicId="
                + messageStatusTopicId + ", messageStatusTopic=" + messageStatusTopic + "]";
    }

}
