*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}       ${EMPTY}

*** Test cases ***
Catalog_Service_01_POST verify unsuccessful operation of POST Message Status Topic with one mandatory field missing(without name field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB
    ...    with one mandatory field missing(without name field) present in request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":1,"encoding":"JSON","specificationReference":"specref1"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST verify unsuccessful operation of POST Message Status Topic with one mandatory field missing(without messageBusId field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with one mandatory field missing(without messageBusId field) in request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"name":"topic1","encoding":"JSON","specificationReference":"specref1"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_POST verify unsuccessful operation of POST Message Status Topic with all mandatory fields missing(without name and messageBusId)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with all mandatory fields missing(without name and messageBusId) in request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"encoding":"JSON","specificationReference":"specref1"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_POST verify unsuccessful operation of POST Message Status Topic with existing Message Status Topic details
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with existing Message Status Topic details in the request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":1,"name":"topic1","encoding":"JSON","specificationReference":"specref1"}
    Number    response status    409
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_POST verify unsuccessful operation of POST Message Status Topic with non-acceptable values(numeric values) in name field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'name' field) in request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":1,"name":12,"encoding":"JSON","specificationReference":"specref1"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_POST verify unsuccessful operation of POST Message Status Topic with non-acceptable values(non-numeric values) in messageBusId field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with non-acceptable values(non-numeric values in 'messageBusId' field) in request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":"messageBus1","name":"topic1","encoding":"JSON","specificationReference":"specref1"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_POST verify unsuccessful operation of POST Message Status Topic with empty request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with empty request body
    [Tags]    POST Request
    POST    /message-status-topic    body=
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_POST verify unsuccessful operation of POST Message Status Topic with empty request values(name field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with empty request values(name field) in request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":1,"name":"","encoding":"JSON","specificationReference":"specref1"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_POST verify unsuccessful operation of POST Message Status Topic with empty request values(messageBusId field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Status Topic detail in DB(postgreSQL)
    ...    with empty request values(name field) in request body
    [Tags]    POST Request
    POST    /message-status-topic    body={"messageBusId":"","name":"topic1","encoding":"JSON","specificationReference":"specref1"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response
