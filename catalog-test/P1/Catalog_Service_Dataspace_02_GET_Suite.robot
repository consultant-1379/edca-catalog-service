*** Settings ***
Documentation     RObot Framework Testcases for POST API for register dataspace
Library           REST    ${base_url}

*** Variables ***
${base_url}
${data_space_id}    ${EMPTY}

*** Test Cases ***
Catalog_Service_01_GET verify successful operation of GET All Dataspace Details
    [Documentation]    This test is to verify successful GET operation in retrieving all the stored dataspace Details
    ...    from DB(postgreSQL)
    [Tags]    GET Request
    GET    /data-space
    Integer    response status    200
    Array    response body    uniqueItems=true
    String    response headers Content-Type    application/json
    String    $[*].name    4G
    Integer    $[*].id
    Array    $[*].dataProviderTypeIds
    Output    response

Catalog_Service_02_GET verify successful operation of GET Dataspace Details using queryParams(name)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using queryParameters (name)
    [Tags]    GET Request
    ${name}=    Convert To String    4G
    GET    /data-space?name=${name}
    Integer    response status    200
    String    response headers Content-Type    application/json
    Object    response body    required=[ "id" , "name" , "dataProviderTypeIds" ]
    String    response body name    ${name}
    Integer    response body id
    Array    response body dataProviderTypeIds
    Output    response

Catalog_Service_03_GET verify successful operation of GET Dataspace Details using pathParams(id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using pathParameters (id)
    [Tags]    GET Request
    ${name}=    Convert To String    4G
    ${data_space_id}=    Convert To String    1
    GET    /data-space/${data_space_id}
    Output
    Integer    response status    200
    String    response headers Content-Type    application/json
    Object    response body    required=[ "id" , "name" , "dataProviderTypeIds" ]
    String    response body name    ${name}
    Integer    response body id
    Array    response body dataProviderTypeIds
    Output    response
