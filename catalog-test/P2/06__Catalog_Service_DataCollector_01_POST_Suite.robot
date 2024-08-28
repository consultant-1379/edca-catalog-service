*** Settings ***
Documentation     RObot Framework Testcases for POST API for register dataCollector
Library           REST    ${base_url}

*** Variables ***
${missing_mandatory_params_req_body}    {}
${duplicate_data_collector_req_body}    {"collectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f10", "controlEndpoint": "http://8.8.8.8:9090/end_point", "name": "dc_1"}
${invalid_collectoruuid_req_body}    {"collectorId": "$d65f5310-1593-4ae1-9f1d-ab9104180f10", "controlEndpoint": "http://8.8.8.8:9090/end_point", "name": "dc_1"}
${invalid_controlendpointurl_req_body}    {"collectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f10", "controlEndpoint": "8.8.8.8:9090/end_point", "name": "dc_1"}
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST for registering the DataCollector details when dataCollector already exists
    [Documentation]    This test is to verify unsuccessful POST operation for registering the DataCollector details when dataCollector already exists
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-collector    ${duplicate_data_collector_req_body}
    Integer    response status    409
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST - Verify POST with mandatory params missing to create a dataCollector
    [Documentation]    This test is to verify unsuccessful POST operation for registering the DataCollector details where mandatory params is missing
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-collector    ${missing_mandatory_params_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_POST - Verify POST for registering the DataCollector details when collectorId is not of UUID spec formats
    [Documentation]    This test is to verify unsuccessful POST operation for registering the DataCollector details when collectorId is not of UUID spec formats
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-collector    ${invalid_collectoruuid_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_POST - Verify POST for registering the DataCollector details when controlEndpoint is not of URL spec formats
    [Documentation]    This test is to verify unsuccessful POST operation for registering the DataCollector details when controlEndpoint is not of URL spec formats
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-collector    ${invalid_controlendpointurl_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response
