*** Settings ***
Documentation     RObot Framework Testcases for POST API for register dataspace
Library           REST    ${base_url}

*** Variables ***
${valid_dataspace_req_body}    { "name": "4G" }
${missing_mandatory_name_prop_req_body}    ${EMPTY}
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST with valid params to create a dataspace
    [Documentation]    This test is to verify successful POST operation for registering the Dataspace details
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-space    ${valid_dataspace_req_body}
    Integer    response status    201
    Object    response body    required=["id" , "name" , "dataProviderTypeIds"]
    String    response body name    4G
    Integer    response body id
    Array    response body dataProviderTypeIds
    Output    response
