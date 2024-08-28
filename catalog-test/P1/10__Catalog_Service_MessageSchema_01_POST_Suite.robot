*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}       ${EMPTY}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET Message Schema Details when no data exists
    [Documentation]    This test is to verify successful GET operation in retrieving the Message Schema detail from DB when no data exists
    [Tags]    GET
    GET    /message-schema
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    maxItems=0
    Output    response

Catalog_Service_02_POST verify successful operation of POST Message Schema with all fields
    [Documentation]    This test is to verify successful POST operation for Message Schema with all details available.
    [Tags]    POST Request
    POST    /message-schema    body={"dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","dataProviderTypeId": 1,"messageDataTopicId": 1,"specificationReference": "specref1" }
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response


