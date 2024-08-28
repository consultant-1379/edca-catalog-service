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

import java.net.URL;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class DataCollectorDto extends NamedDto {

    @NotNull
    @JsonProperty(access = Access.WRITE_ONLY)
    private UUID collectorId;

    @NotNull
    private URL controlEndpoint;

    /**
     * @return the collectorId
     */
    public UUID getCollectorId() {
        return collectorId;
    }

    /**
     * @param collectorId
     *            the collectorId to set
     */
    public void setCollectorId(final UUID collectorId) {
        this.collectorId = collectorId;
    }

    /**
     * @return the controlEndPoint
     */
    public URL getControlEndpoint() {
        return controlEndpoint;
    }

    /**
     * @param controlEndPoint
     *            the controlEndPoint to set
     */
    public void setControlEndpoint(final URL controlEndPoint) {
        this.controlEndpoint = controlEndPoint;
    }

    @Override
    public String toString() {
        return "DataCollectorDto [collectorId=" + collectorId + ", controlEndpoint=" + controlEndpoint + "]";
    }

}
