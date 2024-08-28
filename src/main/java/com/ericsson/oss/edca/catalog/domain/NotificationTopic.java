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
@Table(name = "notification_topic", uniqueConstraints = { @UniqueConstraint(columnNames = { "message_bus_id", "name" }, name = "uniqueTopicPerService") })
public class NotificationTopic extends NamedEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * reference to the notification schema (e.g. data schema)
     */
    @Column(name = "specification_reference")
    private String specificationReference;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private MessageEncoding encoding;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notificationTopic")
    private Set<FileFormat> fileFormats = Collections.emptySet();

    public void addFileFormat(final FileFormat fileFormat) {
        if (fileFormat.isNew()) {
            getFileFormatsInternal().add(fileFormat);
        }
        fileFormat.setNotificationTopic(this);
    }

    private Collection<FileFormat> getFileFormatsInternal() {
        if (fileFormats == null) {
            this.fileFormats = new HashSet<>();
        }
        return Collections.unmodifiableSet(fileFormats);
    }

    @ManyToOne
    @JoinColumn(name = "message_bus_id")
    @NotNull
    private MessageBus messageBus;

    public void setMessageBus(final MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    public Set<Integer> getFileFormatIds() {
        if (fileFormats == null) {
            return new HashSet<>();
        }
        return getFileFormatsInternal().stream().map(FileFormat::getId).collect(Collectors.toSet());
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

    public Set<FileFormat> getFileFormats() {
        return Collections.unmodifiableSet(fileFormats);
    }

    public void setFileFormats(final Set<FileFormat> fileFormats) {
        this.fileFormats = new HashSet<>(fileFormats);
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    public int getMessageBusId() {
        return messageBus.getId();
    }

}
