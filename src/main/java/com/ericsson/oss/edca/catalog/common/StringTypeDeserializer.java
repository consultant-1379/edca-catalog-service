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
package com.ericsson.oss.edca.catalog.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * This class will be used as custom JsonDeserializer which we can use to throw
 * an JsonMappingexception as soon as it encounters any other token than
 * JsonToken.VALUE_STRING
 * 
 *
 */
public class StringTypeDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
	if (!JsonToken.VALUE_STRING.equals(parser.getCurrentToken())) {
	    throw deserializationContext.wrongTokenException(parser, String.class, JsonToken.VALUE_STRING,
		    "Type conversion is not allowed, expected only string values");
	} else {
	    return parser.getValueAsString();
	}
    }
}