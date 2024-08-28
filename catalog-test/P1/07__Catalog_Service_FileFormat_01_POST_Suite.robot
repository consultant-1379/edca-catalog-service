*** Settings ***
Documentation     RObot Framework Testcases for POST API for register file format
Library           REST    ${base_url}

*** Variables ***
${valid_file_format_req_body}    { "dataProviderTypeId": 1, "reportOutputPeriodList": [15], "dataEncoding": "XML", "specificationReference": "", "notificationTopicId": 1, "bulkDataRepositoryId": 1, "dataCollectorId": "d65f5310-1593-4ae1-9f1d-ab9104180f10"}
${base_url}

*** Test Cases ***
Catalog_Service_01_POST - Verify POST with valid params to create a file format
    [Documentation]    This test is to verify successful POST operation for registering the FileFormat
    ...    in DB(postgreSQL)
    [Tags]    POST Request
    POST    /file-format    ${valid_file_format_req_body}
    Integer    response status    201
    Object    response body    required=["reportOutputPeriodList" , "dataEncoding" , "specificationReference" , "dataProviderType" , "bulkDataRepository" , "notificationTopic" , "dataCollector"]
    Output    response
