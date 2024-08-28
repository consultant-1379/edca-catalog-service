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

public class CatalogException extends Exception {

    private static final long serialVersionUID = 1L;

    public CatalogException(String message, Exception e) {
        super(message, e);
    }

    public CatalogException(String message) {
        super(message);
    }

}
