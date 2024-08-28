*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}       ${EMPTY}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET Message Status Topic Details when no data exists
    [Documentation]    This test is to verify successful GET operation in retreiving the Message Status Topic detail from DB when no data exists
    [Tags]    GET
    GET    /message-status-topic
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    maxItems=0
    Output    response

Catalog_Service_02_POST verify successful operation of POST Message Status Topic with all fields
    [Documentation]    This test is to verify successful POST operation for message status topic with all details available.
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":1,"name":"topic1","encoding":"JSON","specificationReference":"specref1"}
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response

Catalog_Service_03_POST verify successful operation of POST Message Status Topic with only mandatory fields
    [Documentation]    This test is to verify successful POST operation for Message Status topic with only mandatory details available.
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":1,"name":"topic2"}
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response
