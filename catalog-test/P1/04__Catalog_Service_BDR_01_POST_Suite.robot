*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify successful operation of GET BDR Details when no data exists
    [Documentation]    This test is to verify successful GET operation in retreiving the Bulk Data Repository detail from DB when no data exists
    [Tags]    GET
    GET    /bulk-data-repository
    Number    response status    200
    String    response headers Content-Type    application/json
    Array    response body    maxItems=0
    Output    response

Catalog_Service_02_POST verify successful operation of POST BDR with all mandatory fields
    [Documentation]    This test is to verify successful POST operation in storing BDR detail in DB keeping mandatory fields(nameSpace,accessEndpoints,clusterName,name)
    ...    in request body
    [Tags]    POST Request
    POST    /bulk-data-repository    body={"nameSpace":"bdr-nameSpace","accessEndpoints":["eric-oss-dmaap-kafka.edca-sprint2:9092"],"clusterName":"Kaas","name":"catalogbdr"}
    Number    response status    201
    String    response headers Content-Type    application/json
    Object    response body    required=["id"]
    Output    response
