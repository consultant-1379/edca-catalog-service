## EDCA Data Catalog
	Data Catalog exposes REST APIs that will be consumed by Custom Collector and DRG(Data Relay gateway) Simulators to store and retrieve data available in EDCA. This Component helps in EDCA to mediate the data. The catalog contains only information related to the type of data(ex: notification topic ) available and the entity will be stored in the postgres database).
	
Note: The Data Catalog application is built on top of ADP spring boot Microservice chassis. For more reference please visit ADP SpringBoot Microservice chassis link (https://adp.ericsson.se/workinginadpframework/tutorials/java-spring-boot-chassis)	

## Functionalities Exposed by Application
  - Creation of Catalog Entities(HTTP POST)
  		- New Catalog Entity(Bulk Data Repository, Message Bus, Notification Topic, Data Space, Data Collector, Data Provider Type, File Format) to be created 
  - Discovery of Catalog Entities(HTTP GET)
  		- Get All Entries (Discover all entries of a single entity)
		- Get Entry By Id (Discover an entry by it's id) 
		- Get Entry using query parameters
  More information about various HTTP methods can be found in below link.
  - [https://tools.ietf.org/html/rfc7231](https://tools.ietf.org/html/rfc7231) 
  
  
## Maven Dependencies Used
The application has the following Maven dependencies:
  - Spring Boot Start Parent version 2.2.9
  - Spring Boot Starter Web.
  - Spring Boot Starter AOP
  - Springfox-Swagger-ui
  - Springfox-Swagger2
  - micrometer-registry-prometheus
  - Spring Boot Starter Test.
  - PostgresSQL 42.2.14
  - mapStruct 1.4.1.Final
  - Flyway 6.0.8
  - JaCoCo Code Coverage Plugin.
  - Sonar Maven Plugin.
  - Spotify Dockerfile Maven Plugin.
  - maven CheckStyle Plugin
  - Java 11

## Contact Information
The service guardians are:
  - Lakshmi M G (lakshmi.gururaj@wipro.com)
  - Ruchika Garg Diwakar (ruchika.diwakar@wipro.com)

High-level decisions are made by:
  - PO: Mohammed Asim (mohammed.asim2@wipro.com)
  - SPM: Kieran Nash

## Build related artifacts
The main build tool is BOB provided by ADP. For convenience, maven wrapper is provided to allow the developer to build in an isolated workstation that does not have access to ADP.
  - [ruleset2.0.yaml](ruleset2.0.yaml) - for more details on BOB please click here [BOB].
  - [precoderview.Jenkinsfile](precodereview.Jenkinsfile) - For pre code review Jenkins pipeline that runs when patch set is pushed.
  - [publish.Jenkinsfile](publish.Jenkinsfile) - For publish Jenkins pipeline that runs after patch set is merged to master.
  
## Steps to run application Locally

On the Project Base directory execute the below maven goal to build the application artifact.

	- mvn clean package 
  
## Containerization and Deployment to Kubernetes cluster(Kaas/CCD Cluster).
Following artifacts contains information related to building a container and enabling deployment to a Kubernetes cluster:
- [charts](charts/) folder - used by BOB to lint, package and upload helm chart to helm repository.
  -  Once the project is built in the local workstation using the ```bob release``` command, a packaged helm chart is available in the folder ```.bob/microservice-chassis-internal/``` folder. This chart can be manually installed in Kubernetes using ```helm install``` command.
- [Dockerfile](Dockerfile) - used by Spotify dockerfile maven plugin to build docker image.
  - The base image for the chassis application is ```common-base-os``` available in ```armdocker.rnd.ericsson.se```.
  