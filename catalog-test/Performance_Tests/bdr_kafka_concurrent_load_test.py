from locust import User,wait_time,constant,task,SequentialTaskSet,events
import subprocess,os,logging

logger=logging.getLogger(__name__)

# currently this concurrent test file pushes & pull files at a average of 3 files every 30 s. we can push the load by setting timeout with this locust file

# arguments to be given in the cmd during execution
@events.init_command_line_parser.add_listener
def init_parser(parser):
    parser.add_argument("--cc_nameSpace", type=str, default="", help="NameSpace in which custom collector is deployed")
    parser.add_argument("--drg_nameSpace", type=str, default="", help="NameSpace in which drg simulator is deployed")
    parser.add_argument("--bdr_nameSpace", type=str, default="", help="NameSpace in which bdr component is deployed")
    parser.add_argument("--reportStats", type=str, default="", help="report path where the results will be stored")

@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    global ccNameSpace,drgNameSpace,bdrNameSpace,ccPodName,drgPodName,reportDir
    ccNameSpace=environment.parsed_options.cc_nameSpace
    drgNameSpace=environment.parsed_options.drg_nameSpace
    bdrNameSpace=environment.parsed_options.bdr_nameSpace
    reportDir=environment.parsed_options.reportStats
    output1 = subprocess.Popen(
            'kubectl get pod -o=name -n {0} | grep custom-collector'.format(ccNameSpace), stdout=subprocess.PIPE,
            shell=True,text=True)
    tmp1=output1.stdout.read().splitlines()
    ccPodName=''.join(tmp1)
    output2 = subprocess.Popen(
            'kubectl get pod -o=name -n {0} | grep drg-simulator'.format(drgNameSpace), stdout=subprocess.PIPE,
            shell=True,text=True)
    tmp2=output2.stdout.read().splitlines()
    drgPodName=''.join(tmp2)
    output3=subprocess.run('kubectl exec ' + ccPodName +' -n '+ccNameSpace+' -- bash -c "rm -rf test-data/pm-data"',capture_output=True, shell=True,text=True)
    logger.info('removed cc-data from pod'+str(output3))
    output4=subprocess.run('kubectl cp ./CC_data/pm-data ' + ccNameSpace +'/'+ccPodName.strip('pod/')+':./test-data/pm-data',capture_output=True, shell=True,text=True)
    logger.info('copied new cc-data to pod'+str(output4))
    output5=subprocess.run('kubectl cp ./DRG_data/auto1.sh ' + drgNameSpace +'/d'+drgPodName.strip('pod/')+':./',capture_output=True, shell=True,text=True)
    logger.info('copied drg-data to pod'+str(output5))
    logger.info('test started with ccPod'+ccPodName+'drgPod'+drgPodName)

class helperClass(SequentialTaskSet):
    counter=0

    @task()
    def upload_files(self):
        self.counter=self.counter+1
        ccPid=subprocess.run('kubectl exec ' + ccPodName +' -n '+ccNameSpace+' -- bash -c "pgrep -x java"',capture_output=True,encoding='UTF-8')
        output6=subprocess.run('kubectl exec ' + ccPodName +' -n '+ccNameSpace+' -- bash -c "kill -9 '+str(ccPid.stdout)+'"',capture_output=True, shell=True,text=True)
        logger.info('killed existing ccProcess'+str(output6))
        ccLogs=subprocess.run('kubectl exec ' + ccPodName +' -n '+ccNameSpace+' -- bash -c "java -jar custom-collector.jar --spring.config.additional-location=config.properties --edca.nameSpace='+
           bdrNameSpace+' --edca.test.counter=1 --automation"',capture_output=True, shell=True,encoding='UTF-8')
        with open(os.path.join(reportDir,"bdr&kafka_concurrent_load_test.txt"), "a") as file: 
            file.write("\n"+str(ccLogs.stdout))
        if("Uploading file to BDR: success" in str(ccLogs.stdout)):
            print('uploading files:success')
            logger.info('uploaded files:success'+str(ccLogs.stdout))
        else:
            print('uploading files:failed',str(ccLogs.stdout))
            logger.critical('uploaded files:failure'+str(ccLogs.stdout))

    @task()
    def download_files(self):
        self.counter=self.counter+1
        drgPid=subprocess.run('kubectl exec ' + drgPodName +' -n '+drgNameSpace+' -- bash -c "pgrep -x java"',capture_output=True,encoding='UTF-8')
        output7=subprocess.run('kubectl exec ' + drgPodName +' -n '+drgNameSpace+' -- bash -c "chmod +x auto1.sh && kill -9 '+str(drgPid.stdout)+'"',capture_output=True, shell=True,text=True)
        logger.info('killed existing drgProcess'+str(output7))
        drgLogs=subprocess.run('kubectl exec ' + drgPodName +' -n '+drgNameSpace+' -- bash -c "./auto1.sh"',shell=False,encoding='UTF-8',timeout=15)
        with open(os.path.join(reportDir,"bdr&kafka_concurrent_load_test.txt"), "a") as file1: 
            file1.write("\n"+str(drgLogs))
        if("downloaded not Successfully" in str(drgLogs) and "Downloading object from BDR started" in str(drgLogs)):
            print('downloading files:success')
            logger.info('downloading files:success'+str(drgLogs))
        else:
            print('downloading files:failed',str(drgLogs))
            logger.critical('downloading files:failure'+str(drgLogs))

class testUser(User):
    wait_time=constant(300)
    tasks=[helperClass]