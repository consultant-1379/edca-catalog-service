*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}       ${EMPTY}

*** Test cases ***
Catalog_Service_01_GET verify unsuccessful operation of GET Message Schema Details with invalid queryParams
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) with oinvalid queryParams
    [Tags]    GET Request
    ${dataProviderType}=    Convert To String    pvid_1
    GET    /message-schema?dataProviderType=${dataProviderType}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_GET verify unsuccessful operation of GET Message Schema Details using invalid queryParam(topic)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using invalid queryParam(topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    pvid_1:pver1:CM_EXPORT:5G
    GET    /message-schema?topic=${topic}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_GET verify unsuccessful operation of GET Message Schema Details using invalid queryParam(topic)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using invalid queryParam(topic)
    [Tags]    GET Request
    ${topic}=    Convert To String    pvid_1:pver1
    GET    /message-schema?topic=${topic}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_GET verify unsuccessful operation of GET Message Schema Details using invalid id(id as a string)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) using invalid id(id as a string)
    [Tags]    GET Request
    ${id}=    Convert To String    abc
    GET    /message-schema/${id}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_GET verify unsuccessful operation of GET Message Schema Details using non-existing id(Message Schema Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) with non-existing id(Message Schema Detail)
    [Tags]    GET Request
    ${id}=    Convert To Integer    101
    GET    /message-schema/${id}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_GET verify unsuccessful operation of GET Message Schema Details using non-existingset of topic(Message Schema Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema Details
    ...    from DB(postgreSQL) with non-existing set of topic(Message Schema Detail)
    [Tags]    GET Request
    ${topic}=    Convert To String    pvid_1:pver2:CM_EXPORT
    GET    /message-schema?topic=${topic}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_GET verify unsuccessful operation of GET Message Schema Details using existing dataSpace & non-existing dataCategory param(Message Schema Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema data
    ...    from DB(postgreSQL) with non-existing dataSpace & with non-existing dataCategory params(Message Schema Detail)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    10G
    ${dataCategory}=    Convert To String    PM_EXPORT
    GET    /message-schema?dataSpace=${dataSpace}&dataCategory=${dataCategory}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_GET verify unsuccessful operation of GET Message Schema Details using existing dataSpace & non-existing dataProviderType param(Message Schema Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema data
    ...    from DB(postgreSQL) with non-existing dataSpace & with non-existing dataProviderType params(Message Schema Detail)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    10G
    ${dataProviderType}=    Convert To String    pvid_100
    GET    /message-schema?dataSpace=${dataSpace}&dataProviderType=${dataProviderType}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_GET verify unsuccessful operation of GET Message Schema Details using non existing dataSpace(Message Schema Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema data
    ...    from DB(postgreSQL) with non-existing dataSpace(Message Schema Detail)
    [Tags]    GET Request
    ${dataSpace}=    Convert To String    10G
    GET    /message-schema?dataSpace=${dataSpace}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_10_GET verify unsuccessful operation of GET Message Schema Details using non existing dataCategory param(Message Schema Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Schema data
    ...    from DB(postgreSQL) with non-existing dataSpace params(Message Schema Detail)
    [Tags]    GET Request
    ${dataCategory}=    Convert To String    PM_EXPORT
    GET    /message-schema?dataSpace=${dataCategory}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_11_GET verify unsuccessful operation of GET Message Schema Details with improper URL
    [Documentation]    This test is to verify unsuccessful GET operation with improper URL
    [Tags]    GET Request
    GET    /messageschema
    Number    response status    404
    String    response headers Content-Type    application/json
    String    response body message    No handler found for GET /catalog/v1/messageschema
    Output    response

Catalog_Service_12_GET verify unsuccessful operation of GET Message Schema Details with unsupported HTTP Method(PATCH)
    [Documentation]    This test is to verify unsuccessful GET operation using unsupported HTTP Method(PATCH)
    [Tags]    GET Request
    PATCH    /message-schema
    Number    response status    405
    String    response headers Content-Type    application/json
    String    response body message    Request method 'PATCH' not supported
