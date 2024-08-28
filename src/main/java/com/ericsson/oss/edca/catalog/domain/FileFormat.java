/*******************************************************************************

* COPYRIGHT Ericsson 2020
*
* The copyright to the computer program(s) herein is the property of
*
* Ericsson Inc. The programs may be used and/or copied only with written
*
* permission from Ericsson Inc. or in accordance with the terms and
*
* conditions stipulated in the agreement/contract under which the
*
* program(s) have been supplied.
*
******************************************************************************/
package com.ericsson.oss.edca.catalog.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * File type, we assume all the files notifications of the same type and format will go to the same topic (regardless of time period)
 */
@Entity
@Table(name = "file_format", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "data_provider_type_id", "data_encoding" }, name = "unique_file_format_per_provider") })
public class FileFormat extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ElementCollection
    @Column(name = "report_output_period_set")
    @CollectionTable(name = "report_output_periods", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    private Set<Integer> reportOutputPeriodList = Collections.emptySet();

    @Column(name = "data_encoding")
    @Enumerated(EnumType.ORDINAL)
    private DataEncoding dataEncoding;

    @Column(name = "specification_reference")
    private String specificationReference;

    @ManyToOne
    @JoinColumn(name = "data_provider_type_id")
    @NotNull
    private DataProviderType dataProviderType;

    @ManyToOne
    @JoinColumn(name = "data_collector_id")
    @NotNull
    private DataCollector dataCollector;

    @ManyToOne
    @JoinColumn(name = "notification_topic_id")
    private NotificationTopic notificationTopic;

    @ManyToOne
    @JoinColumn(name = "bulk_data_repository_id")
    @NotNull
    private BulkDataRepository bulkDataRepository;

    public DataCollector getDataCollector() {
        return dataCollector;
    }

    public void setDataCollector(final DataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    public NotificationTopic getNotificationTopic() {
        return notificationTopic;
    }

    public void setNotificationTopic(final NotificationTopic notificationTopic) {
        this.notificationTopic = notificationTopic;
    }

    public BulkDataRepository getBulkDataRepository() {
        return bulkDataRepository;
    }

    public void setBulkDataRepository(final BulkDataRepository bulkDataRepository) {
        this.bulkDataRepository = bulkDataRepository;
    }

    public Integer getBulkDataRepositoryId() {
        return this.bulkDataRepository.getId();
    }

    public Set<Integer> getReportOutputPeriodList() {
        return Collections.unmodifiableSet(reportOutputPeriodList);
    }

    public void setReportOutputPeriodList(final Set<Integer> reportOutputPeriodList) {
        this.reportOutputPeriodList = new HashSet<>(reportOutputPeriodList);
    }

    public DataEncoding getDataEncoding() {
        return dataEncoding;
    }

    public void setDataEncoding(final DataEncoding dataEncoding) {
        this.dataEncoding = dataEncoding;
    }

    public String getSpecificationReference() {
        return specificationReference;
    }

    public void setSpecificationReference(final String specificationReference) {
        this.specificationReference = specificationReference;
    }

    public DataProviderType getDataProviderType() {
        return dataProviderType;
    }

    public void setDataProviderType(final DataProviderType dataProviderType) {
        this.dataProviderType = dataProviderType;
    }

    @Override
    public String toString() {
        return "FileFormat [reportOutputPeriodList=" + reportOutputPeriodList + ", dataEncoding=" + dataEncoding + ", specificationReference="
                + specificationReference + ", dataProviderType=" + dataProviderType + ", dataCollector=" + dataCollector + ", notificationTopic="
                + notificationTopic + ", bulkDataRepository=" + bulkDataRepository + "]";
    }

}
