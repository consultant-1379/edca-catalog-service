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

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

@Entity
@Table(name = "data_collector", uniqueConstraints = { @UniqueConstraint(columnNames = { "collector_id" }, name = "unique_collector_id") })
public class DataCollector extends NamedEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "collector_id")
    @NotNull
    private String collectorId;

    @Column(name = "control_endpoint")
    @Convert(converter = DataCollectorControlEpConverter.class)
    private URL controlEndpoint;

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(final String collectorId) {
        this.collectorId = collectorId;
    }

    public URL getControlEndpoint() {
        return controlEndpoint;
    }

    public void setControlEndpoint(final URL controlEndpoint) {
        this.controlEndpoint = controlEndpoint;
    }

    // add a label that can help telling the supplier/node type managed by the
    // collector

}

/**
 * Helper class to ensure controlEndpoint is properly translated between URL/String form when persisting OR retrieiving from database
 */
class DataCollectorControlEpConverter implements AttributeConverter<URL, String> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DataCollectorControlEpConverter.class);

    @Override
    public String convertToDatabaseColumn(final URL attribute) {
        return attribute.toString();
    }

    @Override
    public URL convertToEntityAttribute(final String dbData) {
        URL transformedControlEpURL = null;
        try {
            transformedControlEpURL = new URL(dbData);
        } catch (final MalformedURLException e) {
            logger.error("Control ednpoint could not be translated into URL format . Exception : {}", e.getMessage());
        }
        return transformedControlEpURL;

    }

}
