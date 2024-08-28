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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.ericsson.oss.edca.catalog.domain.DataEncoding;

public class FileFormatDto extends IdDto {

    @NotNull
    private Integer bulkDataRepositoryId;

    @NotNull
    private UUID dataCollectorId;

    @NotNull
    private Integer dataProviderTypeId;

    private Set<Integer> reportOutputPeriodList;

    private DataEncoding dataEncoding;

    private String specificationReference;

    private Integer notificationTopicId;

    /**
     * @return the dataProviderTypeId
     */
    public Integer getDataProviderTypeId() {
        return dataProviderTypeId;
    }

    /**
     * @param dataProviderTypeId
     *            the dataProviderTypeId to set
     */
    public void setDataProviderTypeId(final Integer dataProviderTypeId) {
        this.dataProviderTypeId = dataProviderTypeId;
    }

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
    public void setSpecificationReference(final String specificationReference) {
        this.specificationReference = specificationReference;
    }

    /**
     * @return the notificationTopicId
     */
    public Integer getNotificationTopicId() {
        return notificationTopicId;
    }

    /**
     * @param notificationTopicId
     *            the notificationTopicId to set
     */
    public void setNotificationTopicId(final Integer notificationTopicId) {
        this.notificationTopicId = notificationTopicId;
    }

    /**
     * @return the bulkDataRepositoryId
     */
    public Integer getBulkDataRepositoryId() {
        return bulkDataRepositoryId;
    }

    /**
     * @param bulkDataRepositoryId
     *            the bulkDataRepositoryId to set
     */
    public void setBulkDataRepositoryId(final Integer bulkDataRepositoryId) {
        this.bulkDataRepositoryId = bulkDataRepositoryId;
    }

    /**
     * @return the dataCollectorId
     */
    public UUID getDataCollectorId() {
        return dataCollectorId;
    }

    /**
     * @param dataCollectorId
     *            the dataCollectorId to set
     */
    public void setDataCollectorId(final UUID dataCollectorId) {
        this.dataCollectorId = dataCollectorId;
    }

    /**
     * @return the reportOutputPeriodList
     */
    public Set<Integer> getReportOutputPeriodList() {
        return Collections.unmodifiableSet(reportOutputPeriodList);
    }

    /**
     * @param reportOutputPeriodList
     *            the reportOutputPeriodList to set
     */
    public void setReportOutputPeriodList(final Set<Integer> reportOutputPeriodList) {
        this.reportOutputPeriodList = new HashSet<>(reportOutputPeriodList);
    }

    /**
     * @return the dataEncoding
     */
    public DataEncoding getDataEncoding() {
        return dataEncoding;
    }

    /**
     * @param dataEncoding
     *            the dataEncoding to set
     */
    public void setDataEncoding(final DataEncoding dataEncoding) {
        this.dataEncoding = dataEncoding;
    }

    @Override
    public String toString() {
        return "FileFormatDto [dataProviderTypeId=" + dataProviderTypeId + ", reportOutputPeriodList=" + reportOutputPeriodList + ", dataEncoding="
                + dataEncoding + ", specificationReference=" + specificationReference + ", notificationTopicId=" + notificationTopicId
                + ", bulkDataRepositoryId=" + bulkDataRepositoryId + ", dataCollectorId=" + dataCollectorId + "]";
    }
}