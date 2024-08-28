*** Settings ***
Documentation     RObot Framework Testcases for POST API for register dataspace
Library           REST    ${base_url}

*** Variables ***
${missing_mandatory_name_req_body}    {}
${duplicate_name_req_body}    { "name" : "4G" }
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST for registering the Dataspace details when dataspace already exists
    [Documentation]    This test is to verify unsuccessful POST operation for registering the Dataspace details when dataspace already exists
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-space    ${duplicate_name_req_body}
    Integer    response status    409
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST - Verify POST with mandatory param name missing to create a dataspace
    [Documentation]    This test is to verify unsuccessful POST operation for registering the Dataspace details where mandatory param(name) is missing
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-space    ${missing_mandatory_name_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response
