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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.UniqueConstraint;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "message_schema", uniqueConstraints = { @UniqueConstraint(columnNames = { "data_provider_type_id" }, name = "unique_message_schema_per_provider") })
public class MessageSchema extends BaseEntity {
    /**
     * message schema for streaming data. Multiple schemas on the same topic are allowed, but discouraged.
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "specification_reference")
    private String specificationReference;

    @ManyToOne
    @JoinColumn(name = "data_provider_type_id")
    @NotNull
    private DataProviderType dataProviderType;

    @ManyToOne
    @JoinColumn(name = "data_topic_id")
    @NotNull
    private MessageDataTopic messageDataTopic;

    @ManyToOne
    @JoinColumn(name = "data_collector_id")
    @NotNull
    private DataCollector dataCollector;

    public DataCollector getDataCollector() {
        return dataCollector;
    }

    public void setDataCollector(final DataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    public MessageDataTopic getDataTopic() {
        return messageDataTopic;
    }

    public void setMessageDataTopic(final MessageDataTopic messageDataTopic) {
        this.messageDataTopic = messageDataTopic;
    }

    public Integer getDataTopicId() {
        return this.messageDataTopic.getId();
    }

    public DataProviderType getDataProviderType() {
        return dataProviderType;
    }

    public void setDataProviderType(final DataProviderType dataProviderType) {
        this.dataProviderType = dataProviderType;
    }

    public String getSpecificationReference() {
        return specificationReference;
    }

    public void setSpecificationReference(final String specificationReference) {
        this.specificationReference = specificationReference;
    }

    @Override
    public String toString() {
        return "MessageSchema [specificationReference=" + specificationReference + ", dataProviderType=" + dataProviderType + ", dataCollector=" + dataCollector + ", messageDataTopic="
                + messageDataTopic + "]";
    }

}