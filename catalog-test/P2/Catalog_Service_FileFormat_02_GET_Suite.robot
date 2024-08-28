*** Settings ***
Documentation     RObot Framework Testcases for GET API for querying file format
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test Cases ***
Catalog_Service_01_GET verify unsuccessful operation of GET FileFormat Details using invalid query params
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored file format Details
    ...    from DB(postgreSQL) using invalid queryParameters
    [Tags]    GET Request
    GET    /file-format?unknown_prop=unknown_prop_val_1
    Object    response body
    Integer    response status    400
    Object    response body    required=["timeStamp" , "message"]
    String    response body message
    Output    response

Catalog_Service_02_GET verify unsuccessful operation of GET Dataspace Details using non-associated queryParams(dataSpace and dataProviderType)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using non-existent queryParameters(name)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    non-existent-dataspace-name
    ${dataProviderType}=    Convert To String    non-existent-dataprovider-type-id
    GET    /file-format?dataSpace=${dataSpace}&dataProviderType=${dataProviderType}
    Array    response body

Catalog_Service_03_GET verify unsuccessful operation of GET FileFormat Details using non-existent pathParams(id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored fileformat Details
    ...    from DB(postgreSQL) using non-existent pathParameters (id)
    [Tags]    GET Request
    ${file_format_id}=    Convert To String    1000
    GET    /file-format/${file_format_id}
    Output
    Object    response body    required=["timeStamp" , "message"]
    Integer    response status    404
    String    response headers Content-Type    application/json

Catalog_Service_04_GET verify unsuccessful operation of GET FileFormat Details using invalid non integer pathParams(id)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored fileformat Details
    ...    from DB(postgreSQL) using non integer pathParameters(id)
    [Tags]    GET Request
    ${file_format_id}=    Convert To String    1000x
    GET    /file-format/${file_format_id}
    Output
    Object    response body    required=["timeStamp" , "message"]
    Integer    response status    400
    String    response headers Content-Type    application/json

Catalog_Service_05_GET verify unsuccessful operation of GET FileFormat Details using non-associated queryParams(dataSpace)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored fileformat Details
    ...    from DB(postgreSQL) using queryParams(dataSpace)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    5G
    GET    /file-format?dataSpace=${dataSpace}
    Output
    Array    response body
    String    response headers Content-Type    application/json

Catalog_Service_06_GET verify unsuccessful operation of GET FileFormat Details using non-associated queryParams(dataSpace and dataCategory)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored fileformat Details
    ...    from DB(postgreSQL) using queryParams(dataSpace and dataCategory)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    5G
    ${dataCategory}=    Convert To String    PM_STATS
    GET    /file-format?dataCategory=${dataCategory}&dataSpace=${dataSpace}
    Output    response body
    Array    response body
    String    response headers Content-Type    application/json

Catalog_Service_07_GET verify unsuccessful operation of GET FileFormat Details using non-associated queryParams(dataCategory)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored fileformat Details
    ...    from DB(postgreSQL) using queryParams(dataCategory)
    [Tags]    GET Request
    ${dataCategory}=    Convert To String    PM_STATS
    GET    /file-format?dataCategory=${dataCategory}
    Output    response body
    Array    response body
    String    response headers Content-Type    application/json
