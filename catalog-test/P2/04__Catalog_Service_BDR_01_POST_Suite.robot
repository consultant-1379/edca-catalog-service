*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_POST verify unsuccessful operation of POST BDR with one mandatory field missing(without nameSpace field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB
    ...    with one mandatory field missing(without nameSpace field) present in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas","name":"catalogbdr"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST verify unsuccessful operation of POST BDR with one mandatory field missing(without name field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with one mandatory field missing(without name field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_POST verify unsuccessful operation of POST BDR with one mandatory field missing(without clusterName field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with one mandatory field missing(without clusteName field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"name":"catalogbdr"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_POST verify unsuccessful operation of POST BDR with one mandatory field missing(without accessEndPoints field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with one mandatory field missing(without accessEndpoints field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","clusterName":"Kaas","name":"catalogbdr"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_POST verify unsuccessful operation of POST BDR with existing BDR details
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with existing BDR details in the request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas","name":"catalogbdr"}
    Number    response status    409
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_POST verify unsuccessful operation of POST BDR with non-acceptable values(numeric values) in name field
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'name' field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas","name":1234}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_POST verify unsuccessful operation of POST BDR with non-acceptable values(numeric values) in nameSpace field
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'nameSpace' field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":1.5,"accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas","name":"catalogbdr"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_POST verify unsuccessful operation of POST BDR with non-acceptable values(numeric values) in clusterName field
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'clusterName' field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":1.5,"name":"catalogbdr"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_POST verify unsuccessful operation of POST BDR with non-acceptable values(numeric values) in accessEndpoints field
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with non-acceptable values(numeric values in 'accessEndpoints' field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092",1.545],"clusterName":"Kaas","name":"catalogbdr"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_10_POST verify unsuccessful operation of POST BDR with empty request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with empty request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body=
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_11_POST verify unsuccessful operation of POST BDR with empty request values(name field) in request body
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with empty request values(name field) in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas","name":""}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_12_POST verify unsuccessful operation of POST BDR with unknown fields(nameSpaces non-existing field)
    [Documentation]    This test is to verify unsuccessful POST operation in storing BDR detail in DB(postgreSQL)
    ...    with unknown property/field in request body(nameSpaces)
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpaces":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas","name":"catalogbdr"}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response
