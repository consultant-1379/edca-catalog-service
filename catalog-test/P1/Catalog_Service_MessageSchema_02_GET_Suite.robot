*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}       ${EMPTY}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET ALL Message Schema Details
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL)
    [Tags]    GET Request
    GET    /message-schema
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_02_GET verify successful operation of GET Message Schema Details using queryParams (topic)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    pvid_1:pver1:CM_EXPORT
    GET    /message-schema?topic=${topic}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_03_GET verify successful operation of GET Message Schema Details using queryParams(topic)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    pvid_1:pver1:
    GET    /message-schema?topic=${topic}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_04_GET verify successful operation of GET Message Schema Details using queryParams(topic)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    pvid_1::CM_EXPORT
    GET    /message-schema?topic=${topic}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_05_GET verify successful operation of GET Message Schema Details using queryParams(topic)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    :pver1:CM_EXPORT
    GET    /message-schema?topic=${topic}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_06_GET verify successful operation of GET Message Schema Details using queryParams(topic)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    ::CM_EXPORT
    GET    /message-schema?topic=${topic}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_07_GET verify successful operation of GET Message Schema Details using queryParams(topic)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    pvid_1::
    GET    /message-schema?topic=${topic}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_08_GET verify successful operation of GET Message Schema Details using queryParams(topic)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    :pver1:
    GET    /message-schema?topic=${topic}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_09_GET verify successful operation of GET Message Schema Details using queryParams(dataSpace)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (dataSpace)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    4G
    GET    /message-schema?dataSpace=${dataSpace}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_10_GET verify successful operation of GET Message Schema Details using queryParams(dataCategory)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (dataCategory)
    [Tags]    GET Request
    ${dataCategory}=    Convert To String    CM_EXPORT
    GET    /message-schema?dataCategory=${dataCategory}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_11_GET verify successful operation of GET Message Schema Details using queryParams(dataSpace and dataCategory)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (dataSpace & dataCategory)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    4G
    ${dataCategory}=    Convert To String    CM_EXPORT
    GET    /message-schema?dataSpace=${dataSpace}&dataCategory=${dataCategory}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_12_GET verify successful operation of GET Message Schema Details using queryParams(dataSpace and dataProviderType)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using queryParameters (dataSpace & dataProviderType)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    4G
    ${dataProviderType}=    Convert To String    pvid_1
    GET    /message-schema?dataSpace=${dataSpace}&dataProviderType=${dataProviderType}
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_13_GET verify successful operation of GET Message Schema Details using PathVariable (id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using pathvariable (id)
    [Tags]    GET Request
    ${id}=    Convert To Integer    1
    GET    /message-schema/${id}
    Number    response status    200
    String    response headers Content-Type    application/json
    Number    response body id    ${id}
    Output    response
