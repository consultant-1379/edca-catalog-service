*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_POST verify unsuccessful operation of POST Message Bus with one mandatory field missing(without name field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB
    ...    with one mandatory field missing(without name field) present in request body
    [Tags]    POST Request
    POST    /message-bus    body={"clusterName":"clusterName2","nameSpace":"nameSpace2","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST verify unsuccessful operation of POST Message Bus with one mandatory field missing(without clusterName field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with one mandatory field missing(without clusterName field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","nameSpace":"nameSpace2","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_POST verify unsuccessful operation of POST Message Bus with one mandatory field missing(without nameSpace field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with one mandatory field missing(without nameSpace field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","clusterName":"clusterName2","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_POST verify unsuccessful operation of POST Message Bus with one mandatory field missing(without accesEndpoints field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with one mandatory field missing(without accesEndpoints field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","clusterName":"clusterName2","nameSpace":"nameSpace2","notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_POST verify unsuccessful operation of POST Message Bus with all mandatory fields missing(without name and messageBusId)
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with all mandatory fields missing(without name and messageBusId) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_POST verify unsuccessful operation of POST Message Bus with existing Message Bus details
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with existing Message Bus details in the request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus100","clusterName":"clusterName1","nameSpace":"nameSpace1","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    409
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_POST verify unsuccessful operation of POST Message Bus with non-acceptable values(numeric values) in clusterName field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'clusterName' field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus1","clusterName":1,"nameSpace":"nameSpace2","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_POST verify unsuccessful operation of POST Message Bus with non-acceptable values(numeric values) in name field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'name' field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":2,"clusterName":"clusterName2","nameSpace":"nameSpace2","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_POST verify unsuccessful operation of POST Message Bus with non-acceptable values(numeric values) in nameSpace field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'nameSpace' field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","clusterName":"clusterName2","nameSpace":2,"accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_10_POST verify unsuccessful operation of POST Message Bus with non-acceptable values in accessEndpoints field
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'accessEndpoints' field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","clusterName":"clusterName2","nameSpace":"nameSpace2","accessEndpoints":"ac1","notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_11_POST verify unsuccessful operation of POST Message Bus with empty request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with empty request body
    [Tags]    POST Request
    POST    /message-bus    body=
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_12_POST verify unsuccessful operation of POST Message Bus with empty request values(name field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with empty request values(name field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"","clusterName":"clusterName2","nameSpace":"nameSpace2","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_13_POST verify unsuccessful operation of POST Message Bus with empty request values(clusterName field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with empty request values(clusterName field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","clusterName":"","nameSpace":"nameSpace2","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_14_POST verify unsuccessful operation of POST Message Bus with empty request values(nameSpace field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with empty request values(nameSpace field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","clusterName":"clusterName2","nameSpace":"","accessEndpoints":["ac1"],"notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_15_POST verify unsuccessful operation of POST Message Bus with empty request values(nameSpace field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing Message Bus detail in DB(postgreSQL)
    ...    with empty request values(nameSpace field) in request body
    [Tags]    POST Request
    POST    /message-bus    body={"name":"messageBus2","clusterName":"clusterName2","nameSpace":"nameSpace2","accessEndpoints":"","notificationTopicIds":[]}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response
