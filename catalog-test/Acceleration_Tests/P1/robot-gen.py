# COPYRIGHT Ericsson 2021
# The copyright to the computer program(s) herein is the property of
# Ericsson Inc. The programs may be used and/or copied only with written
# permission from Ericsson Inc. or in accordance with the terms and
# conditions stipulated in the agreement/contract under which the
# program(s) have been supplied.

import yaml
import json
import sys
import os

try:
    if len(sys.argv) == 2 or len(sys.argv) == 3:
        file_name=sys.argv[1]
    else :
        file_name = "rest-endpoint-input.yaml"
    a_yaml_file = open(file_name)
    parsed_yaml_file = yaml.load(a_yaml_file, Loader=yaml.FullLoader)
    #print(parsed_yaml_file)

    text = """
*** Settings ***
Resource         rest-keyword.robot

*** Variables ***
${namespace}

*** Test Case ***
"""

    def addPostCall(post):
        testcase=("\n\n"+post["testcase"]+"\n")
        testcase += "\t[Documentation]    "+post["doc"] +"\n"
        testcase += "\t[Tags]    POST Request\n"
        baseUrl = restapi["baseUrl"]
        url = post["url"]
        expectedRsCode = post["expectedStatusCode"]
        requestBody = str(json.dumps(post["requestBody"]))
        expectedResponse=""
        if post.get("expectedResponse"):
            expectedResponse = str(json.dumps(post["expectedResponse"]))
        testcase+=("\tPost HTTP Request  "+baseUrl+url+"  "+ requestBody + "  "+ str(expectedRsCode)+"  "+ expectedResponse+"\n")
        return testcase

    def addGetCall(get):
        testcase=("\n\n"+get["testcase"]+"\n")
        testcase += "\t[Documentation]    "+get["doc"]+"\n"
        testcase+= "\t[Tags]    GET Request\n"
        baseUrl = restapi["baseUrl"]
        url = get["url"]
        expectedRsCode = get["expectedStatusCode"]
        expectedResponse=""
        if get.get("expectedResponse"):
            expectedResponse = str(json.dumps(get["expectedResponse"]))
        testcase+=("\tGet HTTP Request  "+baseUrl+url+"  "+str(expectedRsCode)+"  "+expectedResponse+"\n")
        return testcase

    def addPatchCall(patch):
        testcase=("\n\n"+patch["testcase"]+"\n")
        testcase += "\t[Documentation]    "+patch["doc"]+"\n"
        testcase += "\t[Tags]    PATCH Request\n"
        baseUrl = restapi["baseUrl"]
        url = patch["url"]
        expectedRsCode = patch["expectedStatusCode"]
        if patch.get("requestBody") :
            requestBody = str(json.dumps(patch["requestBody"]))
        else:
            requestBody = "{}"
        expectedResponse=""
        if patch.get("expectedResponse"):
            expectedResponse = str(json.dumps(patch["expectedResponse"]))
        testcase+=("\tPATCH HTTP Request  "+baseUrl+url+"  "+ requestBody + "  "+ str(expectedRsCode)+"  "+expectedResponse+"\n")
        return testcase

    def addPutCall(put):
        testcase=("\n\n"+put["testcase"]+"\n")
        testcase += "\t[Documentation]    "+put["doc"]+"\n"
        testcase += "\t[Tags]    PUT Request\n"
        baseUrl = restapi["baseUrl"]
        url = put["url"]
        expectedRsCode = put["expectedStatusCode"]
        if put.get("requestBody") :
            requestBody = str(json.dumps(put["requestBody"]))
        else:
            requestBody = "{}" 
        expectedResponse=""
        if put.get("expectedResponse"):
            expectedResponse = str(json.dumps(put["expectedResponse"]))      
        testcase+=("\tPut HTTP Request  "+baseUrl+url+"  "+ requestBody + "  "+ str(expectedRsCode)+"  "+expectedResponse+"\n")
        return testcase

    def addDeletCall(delete):
        testcase=("\n\n"+delete["testcase"]+"\n")
        testcase += "\t[Documentation]    "+delete["doc"]+"\n"
        testcase += "\t[Tags]    DELETE Request\n"
        baseUrl = restapi["baseUrl"]
        url = delete["url"]
        expectedRsCode = delete["expectedStatusCode"]
        expectedResponse=""
        if delete.get("expectedResponse"):
            expectedResponse = str(json.dumps(delete["expectedResponse"]))
        testcase+=("\tDelete HTTP Request  "+baseUrl+url+"  "+str(expectedRsCode)+"  "+expectedResponse+"\n")
        return testcase

    for restapi in parsed_yaml_file['restapi']:
        baseUrl = restapi["baseUrl"]
        for testcase in restapi["testcases"]:
            if testcase.get('restcall') == "post":
                text += addPostCall(testcase)
            if testcase.get('restcall') == "get":
                text += addGetCall(testcase)
            if testcase.get('restcall') == "patch":
                text += addPatchCall(testcase)

    if len(sys.argv) == 3:
        file_name=sys.argv[2]
        if os.path.exists(os.path.dirname(file_name)) == False:
            os.mkdir(os.path.dirname(file_name))
            print(os.path.dirname(file_name)+" is created")
    else:
        file_name="rest-api-testcases.robot"
    robot_gen = open(file_name,"w")
    robot_gen.write(text)
    robot_gen.close()
    a_yaml_file.close()
except Exception as msg:
    print(msg)
else:
    print("Robot File is generated Successfully")