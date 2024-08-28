*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET Notification Topic Details when no data exists
    [Documentation]    This test is to verify successful GET operation in retreiving the Notification Topic detail from DB when no data exists
    [Tags]    GET
    GET    /notification-topic
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    maxItems=0
    Output    response

Catalog_Service_02_POST verify successful operation of POST Notification Topic with all fields
    [Documentation]    This test is to verify successful POST operation for notification topic with all details available.
    [Tags]    POST Request
    POST    /notification-topic    body={"messageBusId":1,"name":"topic1","encoding":"JSON","specificationReference":"specref1","fileFormatIds":[]}
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response

Catalog_Service_03_POST verify successful operation of POST Notification Topic with only mandatory fields
    [Documentation]    This test is to verify successful POST operation for notification topic with only mandatory details available.
    [Tags]    POST Request
    POST    /notification-topic    body={"messageBusId":1,"name":"topic2"}
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response
