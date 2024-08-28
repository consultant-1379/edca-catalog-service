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

import javax.validation.constraints.NotNull;

import com.ericsson.oss.edca.catalog.domain.DataEncoding;

public class FileFormatResponseDto extends IdDto {

    @NotNull
    private BulkDataRepositoryDTO bulkDataRepository;

    /**
     * @return the bulkDataRepository
     */
    public BulkDataRepositoryDTO getBulkDataRepository() {
        return bulkDataRepository;
    }

    /**
     * @param bulkDataRepository
     *            the bulkDataRepository to set
     */
    public void setBulkDataRepository(final BulkDataRepositoryDTO bulkDataRepository) {
        this.bulkDataRepository = bulkDataRepository;
    }

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
    public void setDataCollector(final DataCollectorDto dataCollector) {
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
    public void setDataProviderType(final DataProviderTypeDto dataProviderType) {
        this.dataProviderType = dataProviderType;
    }

    private NotificationTopicDto notificationTopic;

    /**
     * @return the notificationTopic
     */
    public NotificationTopicDto getNotificationTopic() {
        return notificationTopic;
    }

    /**
     * @param notificationTopic
     *            the notificationTopic to set
     */
    public void setNotificationTopic(final NotificationTopicDto notificationTopic) {
        this.notificationTopic = notificationTopic;
    }

    private Set<Integer> reportOutputPeriodList;

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

    private DataEncoding dataEncoding;

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
    public void setSpecificationReference(final String specificationReference) {
        this.specificationReference = specificationReference;
    }

    @Override
    public String toString() {
        return "FileFormatResponseDto [reportOutputPeriodList=" + reportOutputPeriodList + ", dataEncoding=" + dataEncoding
                + ", specificationReference=" + specificationReference + ", bulkDataRepository=" + bulkDataRepository + ", dataCollector="
                + dataCollector + ", dataProviderType=" + dataProviderType + ", notificationTopic=" + notificationTopic + "]";
    }

}