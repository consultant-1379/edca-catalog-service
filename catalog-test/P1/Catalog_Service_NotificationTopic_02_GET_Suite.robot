*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET ALL Notfication Topic Details
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Notification Topic Details
    ...    from DB(postgreSQL)
    [Tags]    GET Request
    GET    /notification-topic
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    uniqueItems=true
    Output    response

Catalog_Service_02_GET verify successful operation of GET Notification Topic Details using queryParams(messageBusId & name)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Notification Topic Details
    ...    from DB(postgreSQL) using queryParameters (messageBusId & name)
    [Tags]    GET Request
    ${name}=    Convert To String    topic1
    ${messageBusId}=    Convert To String    1
    GET    /notification-topic?name=${name}&messageBusId=${messageBusId}
    Number    response status    200
    String    response headers Content-Type    application/json
    String    response body name    ${name}
    Output    response

Catalog_Service_03_GET verify successful operation of GET Notification Topic Details using PathVariable (id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored Notification Topic Details
    ...    from DB(postgreSQL) using pathvariable (id)
    [Tags]    GET Request
    ${id}=    Convert To Integer    1
    GET    /notification-topic/${id}
    Number    response status    200
    String    response headers Content-Type    application/json
    Number    response body id    ${id}
    Output    response
