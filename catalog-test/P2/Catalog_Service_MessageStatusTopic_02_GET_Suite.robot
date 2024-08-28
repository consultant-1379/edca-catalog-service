*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}       ${EMPTY}

*** Test cases ***
Catalog_Service_01_GET verify unsuccessful operation of GET Message Status Topic Details with one queryParam Missing(messageBusId)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) with one queryParam Missing(messageBusId)
    [Tags]    GET Request
    ${name}=    Convert To String    topic1
    GET    /message-status-topic?name=${name}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_GET verify unsuccessful operation of GET Message Status Topic Details using one queryParam Missing(name)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) with one queryParam Missing(name)
    [Tags]    GET Request
    ${messageBusId}=    Convert To String    1
    GET    /message-status-topic?messageBusId=${messageBusId}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_GET verify unsuccessful operation of GET Message Status Topic Details with one queryParam Missing(name) & using unsupported param(specificationReference)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) with one queryParam Missing(name) & using unsupported param(specificationReference)
    [Tags]    GET Request
    ${messageBusId}=    Convert To String    1
    ${specificationReference}=    Convert To String    specref1
    GET    /message-status-topic?messageBusId=${messageBusId}&specificationReference=${specificationReference}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_GET verify unsuccessful operation of GET Message Status Topic Details with one queryParam Missing(messageBusId) & using unsupported param(specificationReference)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) with one queryParam Missing(messageBusId) & using unsupported param(specificationReference)
    [Tags]    GET Request
    ${name}=    Convert To String    topic1
    ${specificationReference}=    Convert To String    specref1
    GET    /message-status-topic?name=${name}&specificationReference=${specificationReference}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_GET verify unsuccessful operation of GET Message Status Topic Details using unsupported param(specificationReference)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) with unsupported param(specificationReference)
    [Tags]    GET Request
    ${specificationReference}=    Convert To String    specref1
    GET    /message-status-topic?specificationReference=${specificationReference}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_GET verify unsuccessful operation of GET Message Status Topic Details using invalid messageBusId(messageBus Id as a string)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic data
    ...    from DB(postgreSQL) using invalid messageBusId(messageBus Id as a string)
    [Tags]    GET Request
    ${name}=    Convert To String    topic100
    ${messageBusId}=    Convert To String    messageBusId
    GET    /message-status-topic?name=${name}&messageBusId=${messageBusId}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_GET verify unsuccessful operation of GET Message Status Topic Details using invalid id(id as a string)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) using invalid id(id as a string)
    [Tags]    GET Request
    ${id}=    Convert To String    abc
    GET    /message-status-topic/${id}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_GET verify unsuccessful operation of GET Message Status Topic Details using non-existing id(Message Status Topic Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) with non-existing id(Message Status Topic Detail)
    [Tags]    GET Request
    ${id}=    Convert To Integer    101
    GET    /message-status-topic/${id}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_GET verify unsuccessful operation of GET Message Status Topic Details using non-existing name & messageBusId params(Message Status Topic Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic Details
    ...    from DB(postgreSQL) with non-existing name & messageBusId params(Message Status Topic Detail)
    [Tags]    GET Request
    ${name}=    Convert To String    topic100
    ${messageBusId}=    Convert To String    1
    GET    /message-status-topic?name=${name}&messageBusId=${messageBusId}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_10_GET verify unsuccessful operation of GET Message Status Topic Details using existing name & non-existing messageBusId param(Message Status Topic Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Status Topic data
    ...    from DB(postgreSQL) with non-existing name & with existing messageBusId params(Message Status Topic Detail)
    [Tags]    GET Request
    ${name}=    Convert To String    topic1
    ${messageBusId}=    Convert To String    100
    GET    /message-status-topic?name=${name}&messageBusId=${messageBusId}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_11_GET verify unsuccessful operation of GET Message Status Topic Details with improper URL
    [Documentation]    This test is to verify unsuccessful GET operation with improper URL
    [Tags]    GET Request
    GET    /messagestatus-topic
    Number    response status    404
    String    response headers Content-Type    application/json
    String    response body message    No handler found for GET /catalog/v1/messagestatus-topic
    Output    response

Catalog_Service_12_GET verify unsuccessful operation of GET Message Status Topic Details with unsupported HTTP Method(PATCH)
    [Documentation]    This test is to verify unsuccessful GET operation using unsupported HTTP Method(PATCH)
    [Tags]    GET Request
    PATCH    /message-status-topic
    Number    response status    405
    String    response headers Content-Type    application/json
    String    response body message    Request method 'PATCH' not supported
