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
package com.ericsson.oss.edca.catalog.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotEmpty;

/**
 * Message bus endpoint. It can be either internal to the cluster (in such a case it should have a namespace and service name or external - in such a case the cluster name and name is sufficient.
 */
@Entity
@Table(name = "message_bus", uniqueConstraints = { @UniqueConstraint(columnNames = { "cluster_name", "namespace", "name" }, name = "uniqueMessageBusName") })
public class MessageBus implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "cluster_name")
    @NotEmpty
    private String clusterName;

    @Column(name = "namespace")
    private String nameSpace;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageBus")
    private Set<NotificationTopic> notificationTopics;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageBus")
    private Set<MessageStatusTopic> messageStatusTopics;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageBus")
    private Set<MessageDataTopic> messageDataTopics;

    /**
     * Message bus endpoints
     */
    @ElementCollection
    @NotEmpty
    @CollectionTable(name = "message_bus_endpoint_ids", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    @Column(name = "access_endpoints")
    private Set<String> accessEndpoints;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(final String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(final String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public Set<NotificationTopic> getNotificationTopics() {
        return Collections.unmodifiableSet(notificationTopics);
    }

    public void setNotificationTopics(final Set<NotificationTopic> notificationTopics) {
        this.notificationTopics = new HashSet<>(notificationTopics);
    }

    public Set<MessageStatusTopic> getMessageStatusTopics() {
        return Collections.unmodifiableSet(messageStatusTopics);
    }

    public void setMessageStatusTopics(final Set<MessageStatusTopic> messageStatusTopics) {
        this.messageStatusTopics = new HashSet<>(messageStatusTopics);
    }

    public Set<MessageDataTopic> getMessageDataTopics() {
        return Collections.unmodifiableSet(messageDataTopics);
    }

    public void setMessageDataTopics(final Set<MessageDataTopic> messageDataTopics) {
        this.messageDataTopics = new HashSet<>(messageDataTopics);
    }

    public Set<String> getAccessEndpoints() {
        return Collections.unmodifiableSet(accessEndpoints);
    }

    public void setAccessEndpoints(final Set<String> accessEndpoints) {
        this.accessEndpoints = new HashSet<>(accessEndpoints);
    }

    public void addNotificationTopic(final NotificationTopic dataProviderType) {
        if (dataProviderType.isNew()) {
            getNotificationTopicsInternal().add(dataProviderType);
        }
        dataProviderType.setMessageBus(this);
    }

    private Collection<NotificationTopic> getNotificationTopicsInternal() {
        if (notificationTopics == null) {
            this.notificationTopics = new HashSet<>();
        }
        return Collections.unmodifiableSet(notificationTopics);
    }

    public Set<Integer> getNotificationTopicIds() {
        if (notificationTopics == null) {
            return new HashSet<>();
        }
        return getNotificationTopicsInternal().stream().map(NotificationTopic::getId).collect(Collectors.toSet());
    }

    public void addMessageStatusTopic(final MessageStatusTopic messageStatusTopic) {
        if (messageStatusTopic.isNew()) {
            getMessageStatusTopicsInternal().add(messageStatusTopic);
        }
        messageStatusTopic.setMessageBus(this);
    }

    private Collection<MessageStatusTopic> getMessageStatusTopicsInternal() {
        if (messageStatusTopics == null) {
            this.messageStatusTopics = new HashSet<>();
        }
        return Collections.unmodifiableSet(messageStatusTopics);
    }

    public Set<Integer> getMessageStatusTopicIds() {
        if (messageStatusTopics == null) {
            return new HashSet<>();
        }
        return getMessageStatusTopicsInternal().stream().map(MessageStatusTopic::getId).collect(Collectors.toSet());
    }

    public void addMessageDataTopic(final MessageDataTopic messageDataTopic) {
        if (messageDataTopic.isNew()) {
            getMessageDataTopicsInternal().add(messageDataTopic);
        }
        messageDataTopic.setMessageBus(this);
    }

    private Collection<MessageDataTopic> getMessageDataTopicsInternal() {
        if (messageDataTopics == null) {
            this.messageDataTopics = new HashSet<>();
        }
        return Collections.unmodifiableSet(messageDataTopics);
    }

    public Set<Integer> getMessageDataTopicIds() {
        if (messageDataTopics == null) {
            return new HashSet<>();
        }
        return getMessageDataTopicsInternal().stream().map(MessageDataTopic::getId).collect(Collectors.toSet());
    }
}
