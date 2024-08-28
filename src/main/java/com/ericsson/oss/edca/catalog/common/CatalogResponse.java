/*******************************************************************************

* COPYRIGHT Ericsson 2020
*
*
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

package com.ericsson.oss.edca.catalog.common;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CatalogResponse {

    private LocalDateTime timeStamp = LocalDateTime.now();
    private String message;

    @JsonInclude(Include.NON_NULL)
    private String path;

    public CatalogResponse(LocalDateTime timeStamp, String message, String path) {
        super();
        this.timeStamp = timeStamp;
        this.message = message;
        this.path = path;
    }

    public CatalogResponse(LocalDateTime timeStamp, String message) {
        super();
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}
