*** Settings ***
Documentation     RObot Framework Testcases for POST API for register data collector
Library           REST    ${base_url}

*** Variables ***
${valid_datacollector_req_body}    {"collectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f10", "controlEndpoint": "http://8.8.8.8:9090/end_point", "name": "dc_1"}
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST with valid params to create a data collector
    [Documentation]    This test is to verify successful POST operation for registering the DataCollector details
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-collector    ${valid_datacollector_req_body}
    Integer    response status    201
    Object    response body    required=["name" , "controlEndpoint"]
    String    response body name    dc_1
    String    response body controlEndpoint    http://8.8.8.8:9090/end_point
    Output    response
