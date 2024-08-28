*** Settings ***
Documentation     RObot Framework Testcases for GET API for querying dataspace
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test Cases ***
Catalog_Service_01_GET verify unsuccessful operation of GET Dataspace Details using invalid query params
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using invalid queryParameters
    [Tags]    GET Request
    GET    /data-space?unknown_prop=unknown_prop_val_1
    Object    response body
    Integer    response status    400
    Object    response body    required=["timeStamp" , "message"]
    String    response body message
    Output    response

Catalog_Service_02_GET verify unsuccessful operation of GET Dataspace Details using non-existent queryParams(name)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using non-existent queryParameters(name)
    [Tags]    GET Request
    ${name}=    Convert To String    non-existent-dataspace-name
    GET    /data-space?name=${name}
    Object    response body    required=["timeStamp" , "message"]
    Integer    response status    404
    String    response body message
    Output    response

Catalog_Service_03_GET verify unsuccessful operation of GET Dataspace Details using non-existent pathParams(id)
    [Documentation]    This test is to verify successful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using non-existent pathParameters (id)
    [Tags]    GET Request
    ${data_space_id}=    Convert To String    1000
    GET    /data-space/${data_space_id}
    Output
    Object    response body    required=["timeStamp" , "message"]
    Integer    response status    404
    String    response headers Content-Type    application/json
    String    response body message
    Output    response

Catalog_Service_04_GET verify unsuccessful operation of GET Dataspace Details using invalid non integer pathParams(id)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored dataspace Details
    ...    from DB(postgreSQL) using non integer pathParameters(id)
    [Tags]    GET Request
    ${data_space_id}=    Convert To String    1000x
    GET    /data-space/${data_space_id}
    Output
    Object    response body    required=["timeStamp" , "message"]
    Integer    response status    400
    String    response headers Content-Type    application/json
    String    response body message
    Output    response
