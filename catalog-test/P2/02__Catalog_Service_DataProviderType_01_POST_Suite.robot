*** Settings ***
Documentation     RObot Framework Testcases for POST API for register data provider type
Library           REST    ${base_url}

*** Variables ***
${missing_mandatory_name_req_body}    {}
${duplicate_name_req_body}    {"providerVersion": "pver1", "dataCategory": "CM_EXPORT", "providerTypeId": "pvid_1", "dataSpaceId": 1 }
${dataspace_not_exists_req_body}    {"providerVersion": "pver1", "dataCategory": "CM_EXPORT", "providerTypeId": "pvid_1", "dataSpaceId": 2 }
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST for registering the DataProviderType details when dataProviderType already exists
    [Documentation]    This test is to verify unsuccessful POST operation for registering the dataProviderType details when dataspace already exists
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-provider-type    ${duplicate_name_req_body}
    Integer    response status    409
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST - Verify POST with mandatory params missing to create a dataProviderType
    [Documentation]    This test is to verify unsuccessful POST operation for registering the DataProviderType details where mandatory params are missing
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-provider-type    ${missing_mandatory_name_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST - Verify POST with non existent dataSpace to create a dataProviderType
    [Documentation]    This test is to verify unsuccessful POST operation for registering the DataProviderType details where provided dataSpace does not exist
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-provider-type    ${dataspace_not_exists_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response
