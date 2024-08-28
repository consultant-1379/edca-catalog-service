from locust import SequentialTaskSet,wait_time,constant,task,User,events
import json,time,string,random,uuid,subprocess
import logging,shlex,os

logger=logging.getLogger(__name__)

db_entity_to_api_dict = ['data_space','data_provider_type',
                         'file_format', 'message_bus',
                         'notification_topic', 'bulk_data_repository',
                         'data_collector']

def id_generator(size=3, chars=string.ascii_uppercase + string.digits):
     return ''.join(random.choice(chars) for _ in range(size))
 
@events.init_command_line_parser.add_listener
def init_parser(parser):
    parser.add_argument("--nameSpace", type=str, default="", help="nameSpace in which Catalog Chart deployed")
    parser.add_argument("--dataDirectory", type=str, default="", help="directory consisting of sample Catalog data")
    parser.add_argument("--reportStats", type=str, default="", help="report path where the results will be stored")

@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    logger.info('Starting Catalog Concurrent Load-Test')
    global catalogdbPodName,catalogNameSpace,dataDir,reportDir
    catalogNameSpace=environment.parsed_options.nameSpace
    dataDir=environment.parsed_options.dataDirectory
    reportDir=environment.parsed_options.reportStats
    output1 = subprocess.Popen(
            'kubectl get pod -o=name -n {0} | grep eric-edca-catalog-db | head -n 1'.format(catalogNameSpace), stdout=subprocess.PIPE,
            shell=True,text=True)
    tmp1=output1.stdout.read().splitlines()
    catalogdbPodName=''.join(tmp1)
    global bdrData,msgBusData,notificationData,dataSpaceData,dataProviderTypeData,dataCollectorData,fileFormatData
    with open(os.path.join(dataDir,"bulk-data-repository.json")) as json_file1:
        bdrData=json.load(json_file1)
    with open(os.path.join(dataDir,"message-bus.json")) as json_file2:
        msgBusData=json.load(json_file2)
    with open(os.path.join(dataDir,"notification-topic.json")) as json_file3:
        notificationData=json.load(json_file3)
    with open(os.path.join(dataDir,"data-space.json")) as json_file4:
        dataSpaceData=json.load(json_file4)
    with open(os.path.join(dataDir,"data-provider-type.json")) as json_file5:
        dataProviderTypeData=json.load(json_file5)
    with open(os.path.join(dataDir,"data-collector.json")) as json_file6:
        dataCollectorData=json.load(json_file6)
    with open(os.path.join(dataDir,"file-format.json")) as json_file7:
        fileFormatData=json.load(json_file7)

@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    logger.info('Stopping Catalog Continous Load-Test')
    truncate_tables()
    environment.runner.quit()

def truncate_tables():
    try:
        logger.info("Master DB pod = " + catalogdbPodName)
        for catalog_table in db_entity_to_api_dict:
            command='kubectl exec '+ catalogdbPodName +' -n '+ catalogNameSpace +' -- psql -U postgres -d catalog -c "TRUNCATE {0} RESTART IDENTITY CASCADE;"'.format(catalog_table)
            truncate_cmd_output = subprocess.check_output(shlex.split(command), stderr=subprocess.STDOUT,shell=False, text=True)
    except subprocess.CalledProcessError as e:
        logger.error(e.output)
    logger.info("Successfully truncated all catalog schema tables")

class helperClass(SequentialTaskSet):
    bdr_id=0
    msgBus_id=0
    notification_id=0
    dataspace_id=0
    dataprovidertype_id=0
    datacollector_id=0
    fileformat_id=0 
    collectorIds=[]

# BulkDataRepository test cases
    @task()
    def postBulkDataRepository(self):
                tmp = dict((k,v) for k,v in bdrData.items())
                for k in tmp.keys():
                    if(k=='name'):
                        tmp[k] =tmp[k]+str(id_generator())
                command ='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -H "Content-Type: application/json" -X POST "http://eric-edca-catalog:9590/catalog/v1/bulk-data-repository" -d '+"'"+json.dumps(tmp)+"'"
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '201' not in output1:
                    print('bulk-data-repository: POST: failure')
                    logger.critical('bulk-data-repository: POST: failure')
                else:
                    jsonData=json.loads(str(output1[:output1.index('&')]))
                    self.bdr_id=jsonData['id']
                    print('bulk-data-repository: POST: success',output1)
                    logger.info('bulk-data-repository: POST: success'+ output1)
                time.sleep(1)

    @task()
    def getBulkDataRepository(self):
                command='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -X GET http://eric-edca-catalog:9590/catalog/v1/bulk-data-repository'
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '200' not in output1:
                    print('bulk-data-repository: GET: failure')
                    logger.critical('bulk-data-repository: GET: failure')
                else:
                    print('bulk-data-repository: GET: success',output1)
                    logger.info('bulk-data-repository: GET: success'+ output1)
                time.sleep(1)

# MessageBus test cases
    @task()
    def postMessageBus(self):
                tmp = dict((k,v) for k,v in msgBusData.items())
                for k in tmp.keys():
                    if(k=='name'):
                        tmp[k] =tmp[k]+str(id_generator())
                command ='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -H "Content-Type: application/json" -X POST "http://eric-edca-catalog:9590/catalog/v1/message-bus" -d '+"'"+json.dumps(tmp)+"'"
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '201' not in output1:
                    print('message-bus: POST: failure')
                    logger.critical('message-bus: POST: failure')
                else:
                    jsonData=json.loads(str(output1[:output1.index('&')]))
                    self.msgBus_id=jsonData['id']
                    print('message-bus: POST: success',output1)
                    logger.info('message-bus: POST: success'+ output1)
                time.sleep(1)

    @task()
    def getMessageBus(self):
                command='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -X GET http://eric-edca-catalog:9590/catalog/v1/message-bus'
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '200' not in output1:
                    print('message-bus: GET: failure')
                    logger.critical('message-bus: GET: failure')
                else:
                    print('message-bus: GET: success',output1)
                    logger.info('message-bus: GET: success'+ output1)
                time.sleep(1)

# NotificationTopic test cases
    @task()
    def postNotificationTopic(self):
                tmp = dict((k,v) for k,v in notificationData.items())
                for k in tmp.keys():
                    if(k=='name'):
                        tmp[k] =tmp[k]+str(id_generator())
                command ='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -H "Content-Type: application/json" -X POST "http://eric-edca-catalog:9590/catalog/v1/notification-topic" -d '+"'"+json.dumps(tmp)+"'"
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '201' not in output1:
                    print('notification-topic: POST: failure')
                    logger.critical('notification-topic: POST: failure')
                else:
                    jsonData=json.loads(str(output1[:output1.index('&')]))
                    self.notification_id=jsonData['id']
                    print('notification-topic: POST: success',output1)
                    logger.info('notification-topic: POST: success'+ output1)
                time.sleep(1)

    @task()
    def getNotificationTopic(self):
                command='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -X GET http://eric-edca-catalog:9590/catalog/v1/notification-topic'
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '200' not in output1:
                    print('notification-topic: GET: failure')
                    logger.critical('notification-topic: GET: failure')
                else:
                    print('notification-topic: GET: success',output1)
                    logger.info('notification-topic: GET: success'+ output1)
                time.sleep(1) 

#dataSpace test cases
    @task()
    def postDataSpace(self):
                tmp = dict((k,v) for k,v in dataSpaceData.items())
                for k in tmp.keys():
                    if(k=='name'):
                        tmp[k] =tmp[k]+str(id_generator())
                command ='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -H "Content-Type: application/json" -X POST "http://eric-edca-catalog:9590/catalog/v1/data-space" -d '+"'"+json.dumps(tmp)+"'"
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '201' not in output1:
                    print('data-space: POST: failure')
                    logger.critical('data-space: POST: failure')
                else:
                    jsonData=json.loads(str(output1[:output1.index('&')]))
                    self.dataspace_id=jsonData['id']
                    print('data-space: POST: success',output1)
                    logger.info('data-space: POST: success'+ output1)
                time.sleep(1)

    @task()
    def getDataSpace(self):
                command='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -X GET http://eric-edca-catalog:9590/catalog/v1/data-space'
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '200' not in output1:
                    print('data-space: GET: failure')
                    logger.critical('data-space: GET: failure')
                else:
                    print('data-space: GET: success',output1)
                    logger.info('data-space: GET: success'+ output1)
                time.sleep(1)  

# dataProviderType test cases
    @task()
    def postdataProviderTypeData(self):
                tmp = dict((k,v) for k,v in dataProviderTypeData.items())
                for k in tmp.keys():
                    if(k=='providerTypeId'):
                        tmp[k] =tmp[k]+str(id_generator())
                command ='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -H "Content-Type: application/json" -X POST "http://eric-edca-catalog:9590/catalog/v1/data-provider-type" -d '+"'"+json.dumps(tmp)+"'"
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '201' not in output1:
                    print('data-provider-type: POST: failure')
                    logger.critical('data-provider-type: POST: failure')
                else:
                    jsonData=json.loads(str(output1[:output1.index('&')]))
                    self.dataprovidertype_id=jsonData['id']
                    print('data-provider-type: POST: success',output1)
                    logger.info('data-provider-type: POST: success'+ output1)
                time.sleep(1)

    @task()
    def getDataProviderTypeData(self):
                command='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -X GET http://eric-edca-catalog:9590/catalog/v1/data-provider-type'
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '200' not in output1:
                    print('data-provider-type: GET: failure')
                    logger.critical('data-provider-type: GET: failure')
                else:
                    print('data-provider-type: GET: success',output1)
                    logger.info('data-provider-type: GET: success'+ output1)
                time.sleep(1)
    
# Data Collector Test Cases
    @task()
    def postDataCollectorData(self):
                tmp = dict((k,v) for k,v in dataCollectorData.items())
                for k in tmp.keys():
                    if(k=='collectorId'):
                        tmp[k] =str(uuid.uuid4())
                        self.collectorIds.append(tmp[k])
                        self.datacollector_id=tmp[k]
                command ='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -H "Content-Type: application/json" -X POST "http://eric-edca-catalog:9590/catalog/v1/data-collector" -d '+"'"+json.dumps(tmp)+"'"
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '201' not in output1:
                    print('data-collector: POST: failure')
                    logger.critical('data-collector: POST: failure')
                else:
                    print('data-collector: POST: success',output1)
                    logger.info('data-collector: POST: success'+ output1)
                time.sleep(1)

# FileFormat test cases
    @task()
    def postFileFormatData(self):
                tmp = dict((k,v) for k,v in fileFormatData.items())
                for k in tmp.keys():
                    if(k=='bulkDataRepositoryId'):
                        tmp[k] =self.bdr_id
                    if(k=='dataProviderTypeId'):
                        tmp[k]=self.dataprovidertype_id
                    if(k=='notificationTopicId'):
                        tmp[k]=self.notification_id
                    if(k=='dataCollectorId'):
                        tmp[k] =self.datacollector_id
                command ='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w ", response_code:%{http_code}, time_taken:%{time_total}" -H "Content-Type: application/json" -X POST "http://eric-edca-catalog:9590/catalog/v1/file-format" -d '+"'"+json.dumps(tmp)+"'"
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '201' not in output1:
                    print('file-format: POST: failure')
                    logger.critical('file-format: POST: failure')
                else:
                    print('file-format: POST: success',output1)
                    logger.info('file-format: POST: success'+ output1)
                time.sleep(1)

    @task()
    def getFileFormatData(self):
                command='kubectl exec ' +catalogdbPodName +' -n '+catalogNameSpace+ ' -- curl -w "& response_code:%{http_code}& time_taken:%{time_total}" -X GET http://eric-edca-catalog:9590/catalog/v1/file-format'
                output1 = subprocess.check_output(shlex.split(command), shell=False,encoding='UTF-8')
                with open(os.path.join(reportDir,"catalog-concurrent_load_test.txt"), "a") as file: 
                    file.write("\n"+output1)
                if '200' not in output1:
                    print('file-format: GET: failure')
                    logger.critical('file-format: GET: failure')
                else:
                    print('file-format: GET: success',output1)
                    logger.info('data-provider-type: GET: success'+ output1)
                time.sleep(1)

class loadTestRunner(User):
    wait_time=constant(10)
    tasks=[helperClass]
  