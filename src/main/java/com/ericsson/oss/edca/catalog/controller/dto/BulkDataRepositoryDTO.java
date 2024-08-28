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
package com.ericsson.oss.edca.catalog.controller.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import com.ericsson.oss.edca.catalog.common.StringTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDataRepositoryDTO extends NamedDto {

    @NotEmpty(message = "clusterName is a mandatory field in BulkDataRepository")
    @JsonDeserialize(using = StringTypeDeserializer.class)
    private String clusterName;

    @NotEmpty(message = "nameSpace is a mandatory field in BulkDataRepository")
    @JsonDeserialize(using = StringTypeDeserializer.class)
    private String nameSpace;

    @NotEmpty(message = "accessEndpoints is a mandatory field in BulkDataRepository")
    @JsonDeserialize(contentUsing = StringTypeDeserializer.class)
    private Set<String> accessEndpoints = Collections.emptySet();

    private Set<Integer> fileFormatIds = Collections.emptySet();

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public Set<String> getAccessEndpoints() {
        return Collections.unmodifiableSet(accessEndpoints);
    }

    public void setAccessEndpoints(Set<String> accessEndpoints) {
        this.accessEndpoints = new HashSet<>(accessEndpoints);
    }

    public Set<Integer> getFileFormatIds() {
        return Collections.unmodifiableSet(fileFormatIds);
    }

    public void setFileFormatIds(Set<Integer> fileFormatIds) {
        this.fileFormatIds = new HashSet<>(fileFormatIds);
    }

    @Override
    public String toString() {
        return "BulkDataRepositoryDTO [clusterName=" + clusterName + ", nameSpace=" + nameSpace + ", accessEndpoints=" + accessEndpoints + ", fileFormatIds=" + fileFormatIds + "]";

    }

}
