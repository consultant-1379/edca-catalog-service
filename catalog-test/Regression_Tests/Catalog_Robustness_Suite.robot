*** comments ***
COPYRIGHT Ericsson 2020-2021 -
 The copyright to the computer program(s) herein is the property of
 Ericsson Inc. The programs may be used and/or copied only with written
 permission from Ericsson Inc. or in accordance with the terms and
 conditions stipulated in the agreement/contract under which the
 program(s) have been supplied.

*** Settings ***
Library            SSHLibrary
Library            REST
Suite Setup        Open Connection And Log In
Suite Teardown     Close All Connections
Test Timeout       5 minutes

*** Variables ***
${HOST}               atvts2731.athtem.eei.ericsson.se
${USERNAME}           root
${PASSWORD}           shroot
${namespace}          som-catalog-iam
${app_name}           eric-edca-catalog
${wait_period}        1 minute 30 seconds

*** Keywords ***
Open Connection And Log In
    Open Connection    ${HOST}
    Login    ${USERNAME}    ${PASSWORD}

*** Test Cases ***
Catalog_Service_Robustness_01 - Verify catalog service is deployed with 2 replicas
    [Documentation]    This testcase is to verify the catalog service is deployed with 2 replicas
    ${expected_available_replicas}=    Convert To String    2
    ${expected_ready_replicas}=    Convert To String    2/2
    ${expected_up_to_date_replicas}=    Convert To String    2
    # verify the current replicaCount is 2
    ${ready_replicas}=    Execute Command    kubectl get deployment --selector=app.kubernetes.io/name=${app_name} -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $2}'
    ${up_to_date_replicas}=    Execute Command    kubectl get deployment --selector=app.kubernetes.io/name=${app_name} -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $3}'
    ${available_replicas}=    Execute Command    kubectl get deployment --selector=app.kubernetes.io/name=${app_name} -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $4}'
    Should Be Equal As Strings    ${ready_replicas}    ${expected_ready_replicas}
    Should Be Equal As Strings    ${up_to_date_replicas}    ${expected_up_to_date_replicas}
    Should Be Equal As Strings    ${available_replicas}    ${expected_available_replicas}

Catalog_Service_Robustness_02 - Verify catalog service pods are deployed on different worker nodes
    [Documentation]    This testcase is to verify pod anti-affinity support in catalog service
    ${catalog_pod_1_worker_node}=    Execute Command    kubectl get pods --selector=app.kubernetes.io/name=${app_name} -n ${namespace} -owide | awk 'FNR == 2 {print}' | awk '{print $7}'
    ${catalog_pod_2_worker_node}=    Execute Command    kubectl get pods --selector=app.kubernetes.io/name=${app_name} -n ${namespace} -owide | awk 'FNR == 3 {print}' | awk '{print $7}'
    Should Not Be Equal As Strings     ${catalog_pod_1_worker_node}    ${catalog_pod_2_worker_node}

Catalog_Service_Robustness_03 - Verify catalog service serves traffic in case of catalog service pod disruptions
    [Documentation]    This testcases is to verify catalog service is able to handle API requests in case of catalog pod disruptions , evictions , container restarts
    # simulate pod evictions , container restart via kubectl delete pod
    Execute Command    kubectl delete pod $(kubectl get pods --selector=app.kubernetes.io/name=${app_name} -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $1}') -n ${namespace}
    ${post_dataspace}=    Execute Command    kubectl exec -it ${app_name}-db-0 -c ${app_name}-db -n ${namespace} -- curl -v -XPOST -H "Content-Type: application/json" -d '{ "name" : "5G" }' http://${app_name}:8080/catalog/v1/dataspace
    Should Contain    ${post_dataspace}     id
    ${get_dataspace}=    Execute Command    kubectl exec -it ${app_name}-db-0 -c ${app_name}-db -n ${namespace} -- curl -v -XGET -H "Content-Type: application/json" http://${app_name}:8080/catalog/v1/dataspace
    Should Contain    ${get_dataspace}    id
    # wait for the deployment to reach the desired pod replicaCount
    Sleep    ${wait_period}
    ${expected_available_replicas}=    Convert To String    2
    ${expected_ready_replicas}=    Convert To String    2/2
    ${expected_up_to_date_replicas}=    Convert To String    2
    # verify the current replicaCount is 2
    ${ready_replicas}=    Execute Command     kubectl get deployment --selector=app.kubernetes.io/name=${app_name} -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $2}'
    ${up_to_date_replicas}=    Execute Command     kubectl get deployment --selector=app.kubernetes.io/name=${app_name} -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $3}'
    ${available_replicas}=    Execute Command     kubectl get deployment --selector=app.kubernetes.io/name=${app_name} -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $4}'
    Should Be Equal As Strings     ${ready_replicas}    ${expected_ready_replicas}
    Should Be Equal As Strings     ${up_to_date_replicas}    ${expected_up_to_date_replicas}
    Should Be Equal As Strings     ${available_replicas}    ${expected_available_replicas}

Catalog_Service_Robustness_04 - Verify catalog service serves traffic when adp-document-database-pg slave replica pod is down
    [Documentation]    This testcases is to verify catalog service is able to handle API requests in case of adp-document-database-pg slave replica pod disruptions , evictions , container restarts
    ${db_replica_pod}=    Execute Command    kubectl get pod -l=role=replica,app=${app_name}-db -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $1}'
    Execute Command    kubectl delete pod ${db_replica_pod} -n ${namespace}
    # catalog service API requests should be able to respond successfully
    ${get_dataspace}=    Execute Command    kubectl exec -it ${app_name}-db-0 -c ${app_name}-db -n ${namespace} -- curl -v -XGET -H "Content-Type: application/json" http://${app_name}:8080/catalog/v1/dataspace
    Should Contain    ${get_dataspace}    id
    # verify adp-document-database-pg returns to required stateful set replica count i,e 1 master and 1 slave replica
    Sleep    ${wait_period}
    ${expected_ready_replicas}=    Convert To String    2/2
    # verify the current replicaCount is 2
    ${ready_replicas}=    Execute Command     kubectl get sts --selector=app.kubernetes.io/name=${app_name}-db -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $2}'
    Should Be Equal As Strings     ${ready_replicas}    ${expected_ready_replicas}

Catalog_Service_Robustness_05 - Verify catalog service serves traffic when adp-document-database-pg master replica pod is down
    [Documentation]    This testcases is to verify catalog service is able to handle API requests in case of adp-document-database-pg master replica pod disruptions , evictions , container restarts
    ${db_master_pod}=    Execute Command    kubectl get pod -l=role=master,app=${app_name}-db -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $1}'
    Execute Command    kubectl delete pod ${db_master_pod} -n ${namespace}
    # catalog service API requests should be able to respond successfully
    ${get_dataspace}=    Execute Command    kubectl exec -it ${app_name}-db-1 -c ${app_name}-db -n ${namespace} -- curl -v -XGET -H "Content-Type: application/json" http://${app_name}:8080/catalog/v1/dataspace
    Should Contain    ${get_dataspace}    id
    # verify adp-document-database-pg returns to required stateful set replica count i,e 1 master and 1 slave replica
    Sleep    ${wait_period}
    ${expected_ready_replicas}=    Convert To String    2/2
    # verify the current replicaCount is 2
    ${ready_replicas}=    Execute Command     kubectl get sts --selector=app.kubernetes.io/name=${app_name}-db -n ${namespace} | awk 'FNR == 2 {print}' | awk '{print $2}'
    Should Be Equal As Strings     ${ready_replicas}    ${expected_ready_replicas}
