*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET ALL FileFormat Details
    [Documentation]    This test is to verify successful GET operation in retrieving the stored FileFormat Details
    ...    from DB(postgreSQL)
    [Tags]    GET Request
    GET    /file-format
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_02_GET verify successful operation of GET FileFormat Details using queryParams(dataProviderType providerTypeId AND dataSpace)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored FileFormat Details
    ...    from DB(postgreSQL) using queryParameters (dataProviderType & dataSpace)
    [Tags]    GET Request
    ${dataProviderType}=    Convert To String   pvid_1
    ${dataSpace}=    Convert To String    4G
    GET    /file-format?dataProviderType=${dataProviderType}&dataSpace=${dataSpace}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_03_GET verify successful operation of GET FileFormat Details using PathVariable (id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored FileFormat Details
    ...    from DB(postgreSQL) using pathvariable (id)
    [Tags]    GET Request
    ${id}=    Convert To Integer    1
    GET    /file-format/${id}
    Number    response status    200
    String    response headers Content-Type    application/json
    Number    response body id    ${id}
    String    response body specificationReference
    String    response body dataEncoding
    Array    response body reportOutputPeriodList
    Object    response body bulkDataRepository
    Object    response body dataProviderType
    Object    response body dataCollector
    Object    response body notificationTopic
    Output    response

Catalog_Service_04_GET verify successful operation of GET FileFormat Details using queryParams(dataSpace)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored FileFormat Details
    ...    from DB(postgreSQL) using queryParameters (dataSpace)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    4G
    GET    /file-format?dataSpace=${dataSpace}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_05_GET verify successful operation of GET FileFormat Details using queryParams(dataCategory)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored FileFormat Details
    ...    from DB(postgreSQL) using queryParameters (dataCategory)
    [Tags]    GET Request
    ${dataCategory}=    Convert To String   CM_EXPORT
    GET    /file-format?dataCategory=${dataCategory}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_06_GET verify successful operation of GET FileFormat Details using queryParams(dataSpace AND dataCategory)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored FileFormat Details
    ...    from DB(postgreSQL) using queryParameters (dataSpace and dataCategory)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    4G
    ${dataCategory}=    Convert To String   CM_EXPORT
    GET    /file-format?dataSpace=$[dataSpace}&dataCategory=${dataCategory}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response