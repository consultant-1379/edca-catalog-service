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
package com.ericsson.oss.edca.catalog.domain;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.EnumType;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "message_status_topic", uniqueConstraints = { @UniqueConstraint(columnNames = { "message_bus_id", "name" }, name = "uniqueMessageStatusTopicPerService") })
public class MessageStatusTopic extends NamedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * reference to the notification schema (e.g. data schema)
     */
    @Column(name = "specification_reference")
    private String specificationReference;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private MessageEncoding encoding;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageStatusTopic")
    private Set<MessageDataTopic> messageDataTopics = Collections.emptySet();

    public void addDataTopic(final MessageDataTopic messageDataTopic) {
        if (messageDataTopic.isNew()) {
            getDataTopicsInternal().add(messageDataTopic);
        }
        messageDataTopic.setMessageStatusTopic(this);
    }

    private Collection<MessageDataTopic> getDataTopicsInternal() {
        if (messageDataTopics == null) {
            this.messageDataTopics = new HashSet<>();
        }
        return Collections.unmodifiableSet(messageDataTopics);
    }

    @ManyToOne
    @JoinColumn(name = "message_bus_id")
    @NotNull
    private MessageBus messageBus;

    public void setMessageBus(final MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    public Set<Integer> getMessageDataTopicIds() {
        if (messageDataTopics == null) {
            return new HashSet<>();
        }
        return getDataTopicsInternal().stream().map(MessageDataTopic::getId).collect(Collectors.toSet());
    }

    public String getSpecificationReference() {
        return specificationReference;
    }

    public void setSpecificationReference(final String specificationReference) {
        this.specificationReference = specificationReference;
    }

    public MessageEncoding getEncoding() {
        return encoding;
    }

    public void setEncoding(final MessageEncoding encoding) {
        this.encoding = encoding;
    }

    public Set<MessageDataTopic> getMessageDataTopics() {
        return Collections.unmodifiableSet(messageDataTopics);
    }

    public void setMessageDataTopics(final Set<MessageDataTopic> messageDataTopics) {
        this.messageDataTopics = new HashSet<>(messageDataTopics);
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    public int getMessageBusId() {
        return messageBus.getId();
    }

}
