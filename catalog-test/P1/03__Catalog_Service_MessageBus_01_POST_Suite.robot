*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET Message Bus Details when no data exists
    [Documentation]    This test is to verify successful GET operation in retrieving the Message Bus detail from DB when no data exists
    [Tags]    GET
    GET    /message-bus
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    maxItems=0
    Output    response

Catalog_Service_02_POST verify successful operation of POST Message Bus with all fields
    [Documentation]    This test is to verify successful POST operation for Message Bus with all details available.
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus100","clusterName":"clusterName1","nameSpace":"nameSpace1","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response

Catalog_Service_03_POST verify successful operation of POST Message Bus with only mandatory fields
    [Documentation]    This test is to verify successful POST operation for Message Bus with only mandatory details available.
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus200","clusterName":"clusterName2","nameSpace":"nameSpace2","accessEndpoints":["ac2"]}
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response
