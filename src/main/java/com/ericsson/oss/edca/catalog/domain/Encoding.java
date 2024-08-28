/*******************************************************************************
* COPYRIGHT Ericsson 2020
*
* The copyright to the computer program(s) herein is the property of
* Ericsson Inc. The programs may be used and/or copied only with written
* permission from Ericsson Inc. or in accordance with the terms and
* conditions stipulated in the agreement/contract under which the
* program(s) have been supplied.
* 
******************************************************************************/
package com.ericsson.oss.edca.catalog.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Encoding {
	JSON("JSON");

	private final String value;

	Encoding(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@JsonCreator
	public static Encoding fromValue(String value) {
		for (Encoding r : Encoding.values()) {
			if (r.getValue().equals(value)) {
				return r;
			}
		}
		throw new IllegalArgumentException("Metadata does not support other formats except 'JSON'");
	}

	@Override
	public String toString() {
		return value;
	}

}
