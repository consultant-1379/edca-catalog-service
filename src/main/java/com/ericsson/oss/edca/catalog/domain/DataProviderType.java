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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Type of data provider. That loosely maps to a type of node and data type e.g. PM stats generating function of a node. Data collectors may not have a clear ide of what node they are dealing with if
 * the same model/subsystem generates data - they are dealing with the same interface!
 */
@Entity
@Table(name = "data_provider_type", uniqueConstraints = { @UniqueConstraint(columnNames = { "data_space_id", "provider_type_id", "version" }, name = "unique_data_provider") })
public class DataProviderType extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "data_space_id")
    @NotNull
    private DataSpace dataSpace;

    @Column
    @NotEmpty
    private String providerVersion;

    @Column
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private DataCategory dataCategory;

    /**
     * identifiers of the data provider (e.g sni or fdn) would be nicer to use an array of string but that complicates things with JPA since it is more difficult to put a unique constraint on it
     */
    @NotEmpty
    @Column(name = "provider_type_id")
    private String providerTypeId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataProviderType", orphanRemoval = true)
    private Set<FileFormat> fileFormats = Collections.emptySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataProviderType", orphanRemoval = true)
    private Set<MessageSchema> messageSchemas = Collections.emptySet();

    public DataSpace getDataSpace() {
        return dataSpace;
    }

    public void setDataSpace(final DataSpace dataSpace) {
        this.dataSpace = dataSpace;
    }

    public String getProviderVersion() {
        return providerVersion;
    }

    public void setProviderVersion(final String providerVersion) {
        this.providerVersion = providerVersion;
    }

    public DataCategory getDataCategory() {
        return dataCategory;
    }

    public void setDataCategory(final DataCategory dataCategory) {
        this.dataCategory = dataCategory;
    }

    public String getProviderTypeId() {
        return providerTypeId;
    }

    public void setProviderTypeId(final String providerTypeId) {
        this.providerTypeId = providerTypeId;
    }

    public Set<FileFormat> getFileFormats() {
        return Collections.unmodifiableSet(fileFormats);
    }

    public void setFileFormats(final Set<FileFormat> fileFormats) {
        this.fileFormats = new HashSet<>(fileFormats);
    }

    public void addFileFormat(final FileFormat fileFormat) {
        if (fileFormat.isNew()) {
            getFileFormatsInternal().add(fileFormat);
        }
        fileFormat.setDataProviderType(this);
    }

    private Collection<FileFormat> getFileFormatsInternal() {
        if (fileFormats == null) {
            this.fileFormats = new HashSet<>();
        }
        return Collections.unmodifiableSet(fileFormats);
    }

    private Collection<MessageSchema> getMessageSchemasInternal() {
        if (messageSchemas == null) {
            this.messageSchemas = new HashSet<>();
        }
        return Collections.unmodifiableSet(messageSchemas);
    }

    public Set<MessageSchema> getMessageSchemas() {
        return Collections.unmodifiableSet(messageSchemas);
    }

    public void setMessageSchemas(final Set<MessageSchema> messageSchemas) {
        this.messageSchemas = new HashSet<>(messageSchemas);
    }

    public void addMessageSchema(final MessageSchema messageSchema) {
        if (messageSchema.isNew()) {
            getMessageSchemasInternal().add(messageSchema);
        }
        messageSchema.setDataProviderType(this);
    }

    public Integer getDataSpaceId() {
        return dataSpace.getId();
    }

    public Set<Integer> getFileFormatIds() {
        if (fileFormats == null) {
            return new HashSet<>();
        }
        return getFileFormatsInternal().stream().map(FileFormat::getId).collect(Collectors.toSet());
    }

    public Set<Integer> getMessageSchemaIds() {
        if (messageSchemas == null) {
            return new HashSet<>();
        }
        return getMessageSchemasInternal().stream().map(MessageSchema::getId).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "DataProviderType [dataSpace=" + dataSpace + ", providerVersion=" + providerVersion + ", dataCategory=" + dataCategory + ", providerTypeId=" + providerTypeId + ", fileFormats="
                + fileFormats + ", messageSchemas=" + messageSchemas + "]";
    }

}
