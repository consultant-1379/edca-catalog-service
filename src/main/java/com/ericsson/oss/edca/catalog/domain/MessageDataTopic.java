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

import java.util.HashSet;
import java.util.Set;
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
@Table(name = "message_data_topic", uniqueConstraints = { @UniqueConstraint(columnNames = { "message_bus_id", "name" }, name = "uniqueMessageDataTopicPerService") })
public class MessageDataTopic extends NamedEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column
    @Enumerated(EnumType.ORDINAL)

    private MessageEncoding encoding;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageDataTopic")
    private Set<MessageSchema> messageSchemas = Collections.emptySet();

    public void addMessageSchema(final MessageSchema messageSchema) {
        if (messageSchema.isNew()) {
            getMessageSchemasInternal().add(messageSchema);
        }
        messageSchema.setMessageDataTopic(this);
    }

    private Collection<MessageSchema> getMessageSchemasInternal() {
        if (messageSchemas == null) {
            this.messageSchemas = new HashSet<>();
        }
        return Collections.unmodifiableSet(messageSchemas);
    }

    @ManyToOne
    @JoinColumn(name = "message_bus_id")
    @NotNull
    private MessageBus messageBus;

    @ManyToOne
    @JoinColumn(name = "message_status_topic_id")
    @NotNull
    private MessageStatusTopic messageStatusTopic;

    public void setMessageBus(final MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    public void setMessageStatusTopic(final MessageStatusTopic messageStatusTopic) {
        this.messageStatusTopic = messageStatusTopic;
    }

    public Set<Integer> getMessageSchemaIds() {
        if (messageSchemas == null) {
            return new HashSet<>();
        }
        return getMessageSchemasInternal().stream().map(MessageSchema::getId).collect(Collectors.toSet());
    }

    public MessageEncoding getEncoding() {
        return encoding;
    }

    public void setEncoding(final MessageEncoding encoding) {
        this.encoding = encoding;
    }

    public Set<MessageSchema> getMessageSchemas() {
        return Collections.unmodifiableSet(messageSchemas);
    }

    public void setMessageSchemas(final Set<MessageSchema> messageSchemas) {
        this.messageSchemas = new HashSet<>(messageSchemas);
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    public int getMessageBusId() {
        return messageBus.getId();
    }

    public MessageStatusTopic getMessageStatusTopic() {
        return messageStatusTopic;
    }

    public int getMessageStatusTopicId() {
        return messageStatusTopic.getId();
    }

}
