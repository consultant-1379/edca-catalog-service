*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET ALL Message Bus Details
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL)
    [Tags]    GET Request
    GET    /message-bus
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_02_GET verify successful operation of GET Message Bus Details using queryParams(nameSpace & name)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) using queryParameters (nameSpace & name)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    nameSpace1
    ${name}=    Convert To String    messageBus100
    GET    /message-bus?nameSpace=${nameSpace}&name=${name}
    Number    response status    200
    String    response headers Content-Type    application/json
    Output    response

Catalog_Service_03_GET verify successful operation of GET Message Bus Details using PathVariable (id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Message Bus Details
    ...    from DB(postgreSQL) using pathvariable (id)
    [Tags]    GET Request
    ${id}=    Convert To Integer    1
    GET    /message-bus/${id}
    Number    response status    200
    String    response headers Content-Type    application/json
    Number    response body id    ${id}
    Output    response
