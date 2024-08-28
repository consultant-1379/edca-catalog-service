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

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

import javax.validation.constraints.NotEmpty;

import com.ericsson.oss.edca.catalog.common.StringTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class MessageBusDto extends NamedDto {

    @NotEmpty(message = "clusterName is a mandatory field in MessageBus")
    @JsonDeserialize(using = StringTypeDeserializer.class)
    private String clusterName;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @NotEmpty(message = "nameSpace is a mandatory field in MessageBus")
    @JsonDeserialize(using = StringTypeDeserializer.class)
    private String nameSpace;

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @NotEmpty(message = "accessEndpoints is a mandatory field in MessageBus")
    @JsonDeserialize(contentUsing = StringTypeDeserializer.class)
    private Set<String> accessEndpoints = Collections.emptySet();

    public Set<String> getAccessEndpoints() {
        return Collections.unmodifiableSet(accessEndpoints);
    }

    public void setAccessEndpoints(Set<String> accessEndpoints) {
        this.accessEndpoints = new HashSet<>(accessEndpoints);
    }

    private Set<Integer> notificationTopicIds = Collections.emptySet();

    public Set<Integer> getNotificationTopicIds() {
        return Collections.unmodifiableSet(notificationTopicIds);
    }

    public void setNotificationTopicIds(Set<Integer> notificationTopicIds) {
        this.notificationTopicIds = new HashSet<>(notificationTopicIds);
    }

    private Set<Integer> messageStatusTopicIds = Collections.emptySet();

    public Set<Integer> getMessageStatusTopicIds() {
        return Collections.unmodifiableSet(messageStatusTopicIds);
    }

    public void setMessageStatusTopicIds(Set<Integer> messageStatusTopicIds) {
        this.messageStatusTopicIds = new HashSet<>(messageStatusTopicIds);
    }

    private Set<Integer> messageDataTopicIds = Collections.emptySet();

    public Set<Integer> getMessageDataTopicIds() {
        return Collections.unmodifiableSet(messageDataTopicIds);
    }

    public void setMessageDataTopicIds(Set<Integer> messageDataTopicIds) {
        this.messageDataTopicIds = new HashSet<>(messageDataTopicIds);
    }

    @Override
    public String toString() {
        return "MessageBusDto [clusterName=" + clusterName + ", nameSpace=" + nameSpace + ", accessEndpoints=" + accessEndpoints + ", notificationTopicIds=" + notificationTopicIds
                + ", messageStatusTopicIds=" + messageStatusTopicIds + ", messageDataTopicIds=" + messageDataTopicIds + "]";
    }
}
