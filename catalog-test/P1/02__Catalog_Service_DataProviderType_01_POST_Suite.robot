*** Settings ***
Documentation     RObot Framework Testcases for POST API for register dataProviderType
Library           REST    ${base_url}

*** Variables ***
${valid_data_provider_type_req_body}    {"providerVersion": "pver1", "dataCategory": "CM_EXPORT", "providerTypeId": "pvid_1", "dataSpaceId": 1 }
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST with valid params to create a data provider type
    [Documentation]    This test is to verify successful POST operation for registering the DataProviderType details
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /data-provider-type    ${valid_data_provider_type_req_body}
    Integer    response status    201
    Object    response body    required=["id" , "providerVersion" , "dataCategory" , "providerTypeId" , "fileFormatIds" , "dataSpace"]
    String    response body providerVersion    pver1
    Integer    response body id
    String    response body providerTypeId    pvid_1
    String    response body dataCategory    CM_EXPORT
    Array    response body fileFormatIds
    Object    response body dataSpace
    Output    response
