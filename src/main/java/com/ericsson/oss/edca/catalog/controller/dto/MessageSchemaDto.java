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

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class MessageSchemaDto extends IdDto {

    @NotNull
    private UUID dataCollectorId;

    @NotNull
    private Integer dataProviderTypeId;

    @NotNull
    private Integer messageDataTopicId;

    private String specificationReference;

    public UUID getDataCollectorId() {
        return dataCollectorId;
    }

    public void setDataCollectorId(UUID dataCollectorId) {
        this.dataCollectorId = dataCollectorId;
    }

    public Integer getDataProviderTypeId() {
        return dataProviderTypeId;
    }

    public void setDataProviderTypeId(Integer dataProviderTypeId) {
        this.dataProviderTypeId = dataProviderTypeId;
    }

    public Integer getMessageDataTopicId() {
        return messageDataTopicId;
    }

    public void setMessageDataTopicId(Integer messageDataTopicId) {
        this.messageDataTopicId = messageDataTopicId;
    }

    public String getSpecificationReference() {
        return specificationReference;
    }

    public void setSpecificationReference(String specificationReference) {
        this.specificationReference = specificationReference;
    }

    @Override
    public String toString() {
        return "MessageSchemaDto [dataCollectorId=" + dataCollectorId + ", dataProviderTypeId=" + dataProviderTypeId + ", messageDataTopicId=" + messageDataTopicId + ", specificationReference="
                + specificationReference + "]";
    }

}
