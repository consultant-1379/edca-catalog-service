*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}       ${EMPTY}

*** Test cases ***
Catalog_Service_01_POST verify unsuccessful operation of POST Message Schema with one mandatory field missing(without dataProviderTypeId field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB
    ...    with one mandatory field missing(without dataProviderTypeId field) present in request body
    [Tags]    POST Request
    POST    /message-schema    body={"messageDataTopicId": 1,"dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST verify unsuccessful operation of POST Message Schema with one mandatory field missing(without messageDataTopicId field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB
    ...    with one mandatory field missing(without messageDataTopicId field) present in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId": 1,"dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_POST verify unsuccessful operation of POST Message Schema with one mandatory field missing(without dataCollectorId field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB
    ...    with one mandatory field missing(without dataCollectorId field) present in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId": 1,"messageDataTopicId": 1,"specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_POST verify unsuccessful operation of POST Message Schema with all mandatory fields missing(without dataProviderTypeId, messageDataTopicId and dataCollectorId)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with all mandatory fields missing in request body
    [Tags]    POST Request
    POST    /message-schema    body={"specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_POST verify unsuccessful operation of POST Message Schema with existing Message Schema details
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with existing Message Schema details in the request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId":1,"messageDataTopicId": 1,"dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","specificationReference": "specref"}
    Number    response status    409
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_POST verify unsuccessful operation of POST Message Schema with non-acceptable values(numeric values) in dataCollectorId field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'dataCollectorId' field) in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId":1,"messageDataTopicId": 1,"dataCollectorId":1,"specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_POST verify unsuccessful operation of POST Message Schema with non-acceptable values(non-numeric values) in dataProviderTypeId field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with non-acceptable values(non-numeric values in 'dataProviderTypeId' field) in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId":"abc","messageDataTopicId": 1,"dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_POST verify unsuccessful operation of POST Message Schema with non-acceptable values(non-numeric values) in messageDataTopicId field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with non-acceptable values(non-numeric values in 'messageDataTopicId' field) in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId":1,"messageDataTopicId":"abc","dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_POST verify unsuccessful operation of POST Message Schema with empty request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with empty request body
    [Tags]    POST Request
    POST    /message-schema    body=
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_10_POST verify unsuccessful operation of POST Message Schema with empty request values(dataProviderTypeId field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with empty request values(dataProviderTypeId field) in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId":"","messageDataTopicId": 1,"dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_11_POST verify unsuccessful operation of POST Message Schema with empty request values(messageDataTopicId field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with empty request values(messageDataTopicId field) in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId":1,"messageDataTopicId":"" ,"dataCollectorId":"d65f5310-1593-4ae1-9f1d-ab9104180f10","specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_12_POST verify unsuccessful operation of POST Message Schema with empty request values(dataCollectorId field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Schema detail in DB(postgreSQL)
    ...    with empty request values(dataCollectorId field) in request body
    [Tags]    POST Request
    POST    /message-schema    body={"dataProviderTypeId":1,"messageDataTopicId": 1,"dataCollectorId":"","specificationReference": "specref"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response
