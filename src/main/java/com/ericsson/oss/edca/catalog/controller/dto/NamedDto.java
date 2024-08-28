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

import javax.validation.constraints.NotEmpty;

import com.ericsson.oss.edca.catalog.common.StringTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class NamedDto extends IdDto {

    @NotEmpty(message = "name is a mandatory field which should not be empty")
    @JsonDeserialize(using = StringTypeDeserializer.class)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}