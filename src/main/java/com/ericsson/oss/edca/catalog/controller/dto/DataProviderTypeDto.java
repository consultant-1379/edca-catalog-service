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
package com.ericsson.oss.edca.catalog.controller.dto;

import java.util.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class DataProviderTypeDto extends IdDto {

    @JsonProperty(access = Access.READ_ONLY)
    private DataSpaceDto dataSpace;

    /**
     * 
     * @return dataspace entity related with data-provider-type
     */

    public DataSpaceDto getDataSpace() {
        return dataSpace;
    }

    public void setDataSpace(final DataSpaceDto dataSpace) {
        this.dataSpace = dataSpace;
    }

    private Set<Integer> fileFormatIds = new HashSet<>();

    private Set<Integer> messageSchemaIds = new HashSet<>();

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull
    private Integer dataSpaceId;

    /**
     * @return the dataSpaceId
     */
    public Integer getDataSpaceId() {
        return dataSpaceId;
    }

    /**
     * @param dataSpaceId
     *            the dataSpaceId to set
     */
    public void setDataSpaceId(final Integer dataSpaceId) {
        this.dataSpaceId = dataSpaceId;
    }

    @NotEmpty
    String providerVersion;

    /**
     * @return the providerVersion
     */
    public String getProviderVersion() {
        return providerVersion;
    }

    /**
     * @param providerVersion
     *            the providerVersion to set
     */
    public void setProviderVersion(final String providerVersion) {
        this.providerVersion = providerVersion;
    }

    @NotNull
    DataCategory dataCategory;

    /**
     * @return the dataCategory
     */
    public DataCategory getDataCategory() {
        return dataCategory;
    }

    /**
     * @param dataCategory
     *            the dataCategory to set
     */
    public void setDataCategory(final DataCategory dataCategory) {
        this.dataCategory = dataCategory;
    }

    @NotEmpty
    String providerTypeId;

    /**
     * @return the providerTypeId
     */
    public String getProviderTypeId() {
        return providerTypeId;
    }

    /**
     * @param providerTypeId
     *            the providerTypeId to set
     */
    public void setProviderTypeId(final String providerTypeId) {
        this.providerTypeId = providerTypeId;
    }

    /**
     * @return the fileFormatIds
     */
    public Set<Integer> getFileFormatIds() {
        return Collections.unmodifiableSet(fileFormatIds);
    }

    /**
     * @param messageSchemaIds
     *            the messageSchemaIds to set
     */
    public void setFileFormatIds(final Set<Integer> fileFormatIds) {
        this.fileFormatIds = new HashSet<>(fileFormatIds);
    }

    /**
     * @return the /**
     * @return the fileFormatIds
     */
    public Set<Integer> getMessageSchemaIds() {
        return Collections.unmodifiableSet(messageSchemaIds);
    }

    /**
     * @param messageSchemaIds
     *            the messageSchemaIds to set
     */
    public void setMessageSchemaIds(final Set<Integer> messageSchemaIds) {
        this.messageSchemaIds = new HashSet<>(messageSchemaIds);
    }

    @Override
    public String toString() {
        return "DataProviderTypeDto [dataSpace=" + dataSpace + ", fileFormatIds=" + fileFormatIds + ", messageSchemaIds=" + messageSchemaIds + ", dataSpaceId=" + dataSpaceId + ", providerVersion="
                + providerVersion + ", dataCategory=" + dataCategory + ", providerTypeId=" + providerTypeId + "]";
    }

}
