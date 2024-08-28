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

import javax.validation.constraints.NotNull;

public class MessageSchemaResponseDto extends IdDto {

    @NotNull
    private DataCollectorDto dataCollector;

    /**
     * @return the dataCollector
     */
    public DataCollectorDto getDataCollector() {
        return dataCollector;
    }

    /**
     * @param dataCollector
     *            the dataCollector to set
     */
    public void setDataCollector(DataCollectorDto dataCollector) {
        this.dataCollector = dataCollector;
    }

    @NotNull
    private DataProviderTypeDto dataProviderType;

    /**
     * @return the dataProviderType
     */
    public DataProviderTypeDto getDataProviderType() {
        return dataProviderType;
    }

    /**
     * @param dataProviderType
     *            the dataProviderType to set
     */
    public void setDataProviderType(DataProviderTypeDto dataProviderType) {
        this.dataProviderType = dataProviderType;
    }

    private MessageDataTopicDto messageDataTopic;

    /**
     * @return the messageDataTopic
     */
    public MessageDataTopicDto getMessageDataTopic() {
        return messageDataTopic;
    }

    /**
     * @param messageDataTopic
     *            the messageDataTopic to set
     */
    public void setMessageDataTopic(MessageDataTopicDto messageDataTopic) {
        this.messageDataTopic = messageDataTopic;
    }

    private String specificationReference;

    /**
     * @return the specificationReference
     */
    public String getSpecificationReference() {
        return specificationReference;
    }

    /**
     * @param specificationReference
     *            the specificationReference to set
     */
    public void setSpecificationReference(String specificationReference) {
        this.specificationReference = specificationReference;
    }

    @Override
    public String toString() {
        return "MessageSchemaResponseDto [dataCollector=" + dataCollector + ", dataProviderType=" + dataProviderType + ", messageDataTopic=" + messageDataTopic + ", specificationReference="
                + specificationReference + "]";
    }

}
