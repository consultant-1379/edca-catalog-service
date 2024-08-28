# COPYRIGHT Ericsson 2021
# The copyright to the computer program(s) herein is the property of
# Ericsson Inc. The programs may be used and/or copied only with written
# permission from Ericsson Inc. or in accordance with the terms and
# conditions stipulated in the agreement/contract under which the
# program(s) have been supplied.

*** Settings ***
Library           Collections
Library           String
Library           REST
Library           JsonLib.py


*** Variables ***
${namespace}


*** Keywords ***
Get HTTP Request
    [Arguments]     ${url}   ${expectedRsCode}  ${expectedResponse}=None
    [Tags]    GET Request
    GET    ${url}
    Number    response status    ${expectedRsCode}
    String    response headers Content-Type    application/json
    Log Many  response
    IF  ${expectedResponse}
        ${json_string}=  Object  response
        ${expectedResponse}=  string to json   ${expectedResponse}
        ${return}  Compare Json  ${json_string}[0][body]   ${expectedResponse}
        Should Be True  ${return}
    END

Post HTTP Request
    [Arguments]     ${url}  ${request_body}  ${expectedRsCode}  ${expectedResponse}=None
    [Tags]    POST Request
    POST    ${url}    ${request_body}
    Integer    response status    ${expectedRsCode}
    Output    response
    IF  ${expectedResponse}
        ${json_string}=  Object  response
        ${expectedResponse}=  string to json   ${expectedResponse}
        ${return}  Compare Json  ${json_string}[0][body]   ${expectedResponse}
        Should Be True  ${return}
    END

Put HTTP Request
    [Arguments]     ${url}   ${request_body}  ${expectedRsCode}  ${expectedResponse}=None
    [Tags]    PUT Request
    PUT    ${url}    ${request_body}
    Integer    response status    ${expectedRsCode}
    Output    response
    IF  ${expectedResponse}
        ${json_string}=  Object  response
        ${expectedResponse}=  string to json   ${expectedResponse}
        ${return}  Compare Json  ${json_string}[0][body]   ${expectedResponse}
        Should Be True  ${return}
    END

Delete HTTP Request
    [Arguments]     ${url}  ${expectedRsCode}  ${expectedResponse}=None
    [Tags]    Delete Request
    DELETE    ${url}
    Number    response status    ${expectedRsCode}
    String    response headers Content-Type    application/json
    Output    response
    IF  ${expectedResponse}
        ${json_string}=  Object  response
        ${expectedResponse}=  string to json   ${expectedResponse}
        ${return}  Compare Json  ${json_string}[0][body]   ${expectedResponse}
        Should Be True  ${return}
    END

PATCH HTTP Request
    [Arguments]     ${url}   ${request_body}   ${expectedRsCode}  ${expectedResponse}=None
    [Tags]    PATCH request
    PATCH     ${url}     ${request_body}
    Number    response status    ${expectedRsCode}
    String    response headers Content-Type    application/json
    IF  ${expectedResponse}
        ${json_string}=  Object  response
        ${expectedResponse}=  string to json   ${expectedResponse}
        ${return}  Compare Json  ${json_string}[0][body]   ${expectedResponse}
        Should Be True  ${return}
    END