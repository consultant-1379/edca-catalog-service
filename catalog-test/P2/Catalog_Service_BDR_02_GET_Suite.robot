*** Settings ***
Library           REST    ${base_url}

*** Variables ***
${base_url}

*** Test cases ***
Catalog_Service_01_GET verify unsuccessful operation of GET BDR Details with one queryParam Missing(nameSpace)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with one queryParam Missing(nameSpace)
    [Tags]    GET Request
    ${name}=    Convert To String    catalogbdr
    GET    /bulk-data-repository?name=${name}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_GET verify unsuccessful operation of GET BDR Details using one queryParam Missing(name)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with one queryParam Missing(name)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    bdr-nameSpace
    GET    /bulk-data-repository?nameSpace=${nameSpace}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_GET verify unsuccessful operation of GET BDR Details using one queryParam value Missing(name)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with one queryParam Missing(name)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    bdr-nameSpace
    GET    /bulk-data-repository?nameSpace=${nameSpace}&name=
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_GET verify unsuccessful operation of GET BDR Details using one queryParam value Missing(nameSpace)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with one queryParam Missing(name)
    [Tags]    GET Request
    ${name}=    Convert To String    catalogbdr
    GET    /bulk-data-repository?nameSpace=&name=${name}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_GET verify unsuccessful operation of GET BDR Details with one queryParam Missing(name) & using unsupported param(clusterName)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with one queryParam Missing(name) & using unsupported param(clusterName)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    bdr-nameSpace
    ${clusterName}=    Convert To String    Kaas
    GET    /bulk-data-repository?nameSpace=${nameSpace}&clusterName=${clusterName}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_06_GET verify unsuccessful operation of GET BDR Details with one queryParam Missing(nameSpace) & using unsupported param(clusterName)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with one queryParam Missing(nameSpace) & using unsupported param(clusterName)
    [Tags]    GET Request
    ${name}=    Convert To String    bdr-nameSpace
    ${clusterName}=    Convert To String    Kaas
    GET    /bulk-data-repository?name=${name}&clusterName=${clusterName}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_07_GET verify unsuccessful operation of GET BDR Details using unsupported param(clusterName)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with unsupported param(clusterName)
    [Tags]    GET Request
    ${clusterName}=    Convert To String    Kaas
    GET    /bulk-data-repository?clusterName=${clusterName}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_08_GET verify unsuccessful operation of GET BDR Details using non-existing id(BDR Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with non-existing id(BDR Detail)
    [Tags]    GET Request
    ${id}=    Convert To Integer    101
    GET    /bulk-data-repository/${id}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_09_GET verify unsuccessful operation of GET BDR Details using unsupported id type(passing as String value)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) using unsupported id type(passing as String value)
    [Tags]    GET Request
    ${id}=    Convert To String    test
    GET    /bulk-data-repository/${id}
    Number    response status    400
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_10_GET verify unsuccessful operation of GET BDR Details using non-existing nameSpace & name params(BDR Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR Details
    ...    from DB(postgreSQL) with non-existing nameSpace & name params(BDR Detail)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    metadata
    ${name}=    Convert To String    catalog
    GET    /bulk-data-repository?nameSpace=${nameSpace}&name=${name}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_11_GET verify unsuccessful operation of GET BDR Details using non-existing nameSpace & existing name param(BDR Detail)
    [Documentation]    This test is to verify unsuccessful GET operation in retrieving the stored BDR data
    ...    from DB(postgreSQL) with non-existing nameSpace & with existing name params(BDR Detail)
    [Tags]    GET Request
    ${nameSpace}=    Convert To String    catalogNameSpace
    ${name}=    Convert To String    catalogbdr
    GET    /bulk-data-repository?nameSpace=${nameSpace}&name=${name}
    Number    response status    404
    String    response headers Content-Type    application/json
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_12_GET verify unsuccessful operation of GET BDR Details with improper URL
    [Documentation]    This test is to verify unsuccessful GET operation with improper URL
    [Tags]    GET Request
    GET    /bulk--data-repository
    Number    response status    404
    String    response headers Content-Type    application/json
    String    response body message    No handler found for GET /catalog/v1/bulk--data-repository
    Output    response

Catalog_Service_13_GET verify unsuccessful operation of GET BDR Details with unsupported HTTP Method(PATCH)
    [Documentation]    This test is to verify unsuccessful GET operation using unsupported HTTP Method(PATCH)
    [Tags]    GET Request
    PATCH    /bulk-data-repository
    Number    response status    405
    String    response headers Content-Type    application/json
    String    response body message    Request method 'PATCH' not supported
