*** Settings ***
Documentation     RObot Framework Testcases for GET API for data provider type
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test Cases ***
Catalog_Service_01_GET verify successful operation of GET All DataProviderType Details
    [Documentation]    This test is to verify successful GET operation in retrieving all the stored dataProviderType Details
    ...    from DB(postgreSQL)
    [Tags]    GET Request
    GET    /data-provider-type
    Integer    response status    200
    Array    response body    uniqueItems=true
    String    response headers Content-Type    application/json
    # Object    $[*].dataSpace    {'dataProviderTypeIds': [1], 'id': 1, 'name': '4G'}    
    Integer    $[*].id
    String    $[*].providerVersion    pver1
    String    $[*].providerTypeId    pvid_1
    String    $[*].dataCategory    CM_EXPORT
    Array    $[*].fileFormatIds
    Output    response

Catalog_Service_02_GET verify successful operation of GET DataProviderType Details using queryParams(dataSpace)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored dataProviderType Details
    ...    from DB(postgreSQL) using queryParameters (dataSpace)
    [Tags]    GET Request
    ${name}=    Convert To String    4G
    GET    /data-provider-type?dataSpace=${name}
    Integer    response status    200
    Array    response body    uniqueItems=true
    String    response headers Content-Type    application/json
    # Object    $[*].dataSpace    {'dataProviderTypeIds': [1], 'id': 1, 'name': '4G'}
    Integer    $[*].id
    String    $[*].providerVersion    pver1
    String    $[*].providerTypeId    pvid_1
    String    $[*].dataCategory    CM_EXPORT
    Array    $[*].fileFormatIds
    Output    response

Catalog_Service_03_GET verify successful operation of GET Dataspace Details using pathParams(id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using pathParameters (id)
    [Tags]    GET Request
    ${data_provider_type_id}=    Convert To String    1
    GET    /data-provider-type/${data_provider_type_id}
    Output
    Integer    response status    200
    String    response headers Content-Type    application/json
    Object    response body    required=["id" , "providerVersion" , "dataCategory" , "providerTypeId" , "fileFormatIds" , "dataSpace"]
    String    response body providerVersion    pver1
    Integer    response body id
    String    response body providerTypeId    pvid_1
    String    response body dataCategory    CM_EXPORT
    Array    response body fileFormatIds
    Object    response body dataSpace
    Output    response
