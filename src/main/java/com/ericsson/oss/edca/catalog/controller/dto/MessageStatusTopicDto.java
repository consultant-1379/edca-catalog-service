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

public class MessageStatusTopicDto extends NamedDto {

    private String specificationReference;

    private MessageEncoding encoding;

    private Set<Integer> messageDataTopicIds = Collections.emptySet();

    @JsonProperty(access = Access.READ_ONLY)
    private MessageBusDto messageBus;

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull(message = "messageBusId is a mandatory field which should not be empty")
    private Integer messageBusId;

    public MessageEncoding getEncoding() {
        return encoding;
    }

    public void setEncoding(final MessageEncoding encoding) {
        this.encoding = encoding;
    }

    public String getSpecificationReference() {
        return specificationReference;
    }

    public void setSpecificationReference(final String specificationReference) {
        this.specificationReference = specificationReference;
    }

    public Set<Integer> getMessageDataTopicIds() {
        return Collections.unmodifiableSet(messageDataTopicIds);
    }

    public void setMessageDataTopicIds(final Set<Integer> messageDataTopicIds) {
        this.messageDataTopicIds = new HashSet<>(messageDataTopicIds);
    }

    public void setMessageBusId(final Integer messageBusId) {
        this.messageBusId = messageBusId;
    }

    public int getMessageBusId() {
        return this.messageBusId;
    }

    public MessageBusDto getMessageBus() {
        return this.messageBus;
    }

    public void setMessageBus(final MessageBusDto messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public String toString() {
        return "MessageStatusTopicDto [specificationReference=" + specificationReference + ", encoding=" + encoding + ", messageDataTopicIds=" + messageDataTopicIds + ", messageBusId=" + messageBusId
                + ", messageBus=" + messageBus + "]";
    }

}
