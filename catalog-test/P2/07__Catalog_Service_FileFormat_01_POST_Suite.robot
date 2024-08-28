*** Settings ***
Documentation     RObot Framework Testcases for POST API for register file format
Library           REST    ${base_url}

*** Variables ***
${missing_mandatory_params_req_body}    {}
${duplicate_provider_type_encoding_req_body}    { "dataProviderTypeId": 1, "reportOutputPeriodList": [15], "dataEncoding": "XML", "specificationReference": "", "notificationTopicId": 1, "bulkDataRepositoryId": 1, "dataCollectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f10"}
${invalid_dataprovidertype_req_body}    { "dataProviderTypeId": 2, "reportOutputPeriodList": [15], "dataEncoding": "XML", "specificationReference": "", "notificationTopicId": 1, "bulkDataRepositoryId": 1, "dataCollectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f10"}
${invalid_bdr_req_body}    { "dataProviderTypeId": 1, "reportOutputPeriodList": [15], "dataEncoding": "XML", "specificationReference": "", "notificationTopicId": 1, "bulkDataRepositoryId": 2, "dataCollectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f10"}
${invalid_collector_req_body}    { "dataProviderTypeId": 1, "reportOutputPeriodList": [15], "dataEncoding": "XML", "specificationReference": "", "notificationTopicId": 1, "bulkDataRepositoryId": 2, "dataCollectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f12"}
${invalid_collector_uuid_req_body}    { "dataProviderTypeId": 1, "reportOutputPeriodList": [15], "dataEncoding": "XML", "specificationReference": "", "notificationTopicId": 1, "bulkDataRepositoryId": 1, "dataCollectorId": "$d65f5310-1593-4ae1-9f1d-ab9104180f10"}
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST for registering the DataProviderType details when fileFormat already exists
    [Documentation]    This test is to verify unsuccessful POST operation for registering the dataProviderType details when fileformat already exists
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /file-format    ${duplicate_provider_type_encoding_req_body}
    Integer    response status    409
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_02_POST - Verify POST with mandatory params missing to create a dataProviderType
    [Documentation]    This test is to verify unsuccessful POST operation for registering the DataProviderType details where mandatory params are missing
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /file-format    ${missing_mandatory_params_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_03_POST - Verify POST with non existent dataProviderType to create a fileFormat
    [Documentation]    This test is to verify unsuccessful POST operation for registering the FileFormat details where provided dataProviderType does not exist
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /file-format    ${invalid_dataprovidertype_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_04_POST - Verify POST with non existent bulk data repository to create a fileFormat
    [Documentation]    This test is to verify unsuccessful POST operation for registering the FileFormat details where provided BDR does not exist
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /file-format    ${invalid_bdr_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response

Catalog_Service_05_POST - Verify POST with non existent data collector to create a fileFormat
    [Documentation]    This test is to verify unsuccessful POST operation for registering the FileFormat details where provided data collector does not exist
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /file-format    ${invalid_collector_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response
    
Catalog_Service_06_POST - Verify POST with invalid collector UUID to create a fileFormat
    [Documentation]    This test is to verify unsuccessful POST operation for registering the FileFormat details where provided dataProviderType does not exist
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /file-format    ${invalid_collector_uuid_req_body}
    Integer    response status    400
    Object    response body    required=["timeStamp","message"]
    Output    response
