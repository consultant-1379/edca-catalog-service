*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify unsuccessful operation of GET Message Bus Details with one queryParam Missing(nameSpace)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) with one queryParam Missing(nameSpace)
    [Tags]    GET Request
    ${name}=    Convert To String    messageBus1
    GET    /message-bus?name=${name}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_GET verify unsuccessful operation of GET Message Bus Details using one queryParam Missing(name)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) with one queryParam Missing(name)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    nameSpace1
    GET    /message-bus?nameSpace=${nameSpace}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_GET verify unsuccessful operation of GET Message Bus Details with one queryParam Missing(name) & using unsupported param(specificationReference)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) with one queryParam Missing(name) & using unsupported param(specificationReference)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    nameSpace
    ${specificationReference}=    Convert To String    specref1
    GET    /message-bus?nameSpace=${nameSpace}&specificationReference=${specificationReference}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_GET verify unsuccessful operation of GET Message Bus Details with one queryParam Missing(nameSpace) & using unsupported param(specificationReference)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) with one queryParam Missing(nameSpace) & using unsupported param(specificationReference)
    [Tags]    GET Request
    ${name}=    Convert To String    messageBus1
    ${specificationReference}=    Convert To String    specref1
    GET    /message-bus?name=${name}&specificationReference=${specificationReference}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_GET verify unsuccessful operation of GET Message Bus Details using unsupported param(specificationReference)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) with unsupported param(specificationReference)
    [Tags]    GET Request
    ${specificationReference}=    Convert To String    specref1
    GET    /message-bus?specificationReference=${specificationReference}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_GET verify unsuccessful operation of GET Message Bus Details using invalid id(id as a string)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) using invalid id(id as a string)
    [Tags]    GET Request
    ${id}=    Convert To String    messageBus1
    GET    /message-bus/${id}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_GET verify unsuccessful operation of GET Message Bus Details using non-existing id(Message Bus Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) with non-existing id(Message Bus Detail)
    [Tags]    GET Request
    ${id}=    Convert To Integer    101
    GET    /message-bus/${id}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_GET verify unsuccessful operation of GET Message Bus Details using existing nameSpace & non-existing name params(Message Bus Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) with non-existing name & nameSpace params(Message Bus Detail)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    nameSpace1
    ${name}=    Convert To String    messageBus101
    GET    /message-bus?nameSpace=${nameSpace}&name=${name}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_GET verify unsuccessful operation of GET Message Bus Details using non-existing nameSpace & existing name param(Message Bus Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored Message Bus data
    ...    from DB(postgreSQL) with non-existing nameSpace & with existing name params(Message Bus Detail)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    nameSpace100
    ${name}=    Convert To String    messageBus1
    GET    /message-bus?nameSpace=${nameSpace}&name=${name}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_10_GET verify unsuccessful operation of GET Message Bus Details with improper URL
    [Documentation]    This test is to verify unsuccessful GET operation with improper URL
    [Tags]    GET Request
    GET    /messsage-bus
    Number    response status    404
    String    response headers Content-Type    application/json
    String    response body message    No handler found for GET /catalog/v1/messsage-bus
    Output    response

Catalog_Service_11_GET verify unsuccessful operation of GET Message Bus Details with unsupported HTTP Method(PATCH)
    [Documentation]    This test is to verify unsuccessful GET operation using unsupported HTTP Method(PATCH)
    [Tags]    GET Request
    PATCH    /message-bus
    Number    response status    405
    String    response headers Content-Type    application/json
    String    response body message    Request method 'PATCH' not supported
