# Spring-Cloud-Configuration

CONFIGURATION IN A DISTRIBUTED SYSTEM

Configuration with Spring Cloud Config
- Using config client and server 
- Backed stores
- Updating Configuration and @RefreshScope
- Storing and retrieving sensitive configuration 

What's so different about managing configuration in a cloud-native application ?
Cloud base application are distributed system where as non Cloud Application are non distributed

Issues with typical Configuration Management
- Deployment-oriented
- Push-based is usually not dynamic enough
- Pull-based adds latency with temporal polling

Configuration server solves all those problem. 

Application Configuration Server:
- Dedicated, dynamic, centralized key/value store (may be distributed)
- Authoritative sources
- Auditing
- Versioning
- Cryptography support 

MANAGING APPLICATION CONFIGURATION WITH SPRING CLOUD
Spring Cloud Config provides server and client-side support for externalized configuration in a distributed system

Config Client is Embedded in application 
Spring Environment abstraction
 eg @Inject
    Environment
    
Config Server
Standalone (can be embedded)
Spring PropertySource abstraction
 e.g. classspath:file.properties
 
 - HTTP REST access
 - Output formats
   - JSON (default)
   - Properties
   - YAML
   
 - Backend Stores
   - Git (default) 
   - SVN
   - FileSystem
   
 - Configuration scopes
   - like Dev, QA, Prod
   
pom.xml

<dependencyManagement>
<dependencies>
<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-dependencies</artifactId>
<version>Camden.SR2 </version>
<type>pom</type>
<scope>import</scope>
</dependency>
</dependencies>
</dependencyMahagement>

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-config-server</artifactId>
</dependency>

- Create a folder to store configuration 
- (optional) Add a properties or yml file with a named application ( for global properties like db config) 
- Add properties or yml files named {application}-{profile}  ( for application or profile specific configuration) 
- git init ( location where the configuration file is located) 
- git add ; git commit
- (optional) Setup remote i.e get repository and git push

Configuation of actual configuration server:
application.properties
server.port = 8888
speing.cloud.config.server.git.uri = <uri_to_git_repo>

application.yml
server:
 port: 8888
spring:
 cloud:
  config:
   server:
    git:
     uri: <uri_to_git_repo>
     
Application.java
@SpringBootApplication
@EnableConfigServer
public class Application{

  public static void main(String[] args)
  {
      SpringApplication.run(Application.class,args);
  }
}

for config server to be discover 
add eureka client dependencies, service-url configuration, @EnableDiscoveryClient to make the config server discoverable! 

also Secure Config Server using Spring Security

SPRING CLOUD CONFIG SERVER: REST Endpoints

Rest Endpoint Parameters
{application} map to spring.application.name on client
{profile} map to spring.profiles.active.on client
{label} server side feature to refer to set of config files by name

GET /{application}/{profile}[/{label}]

./myapp/dev/master
./myapp/prod/v2  v2 is the branch
./myapp/default    picks the base or the default application.properties 

GET /{application}-{profile}.(yml | properties)
./myapp-dev.yml
./myapp-prod.properties

GET /{label}/{application}-{profile}.(yml | properties)
./master/myapp-dev.yml
./v2/myapp-prod.properties
./master/myapp-default.properties



SPRING CLOUD CONFIGURATION CLIENT
 - fetching configuration 

the application properties need to be bootstrap its properties before the appliction context has started. there are two ways to do that both used 
bootstrap.properties or bootstrap.yml

two ways: 
1) Config first: Specify the location of the config server
2) Discovery first: Discover the location of the config server

USING SPRING CLOUD CONFIG CLIENT

pom.xml
<dependencyManagement>
 <dependencies>
  <dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-dependencies</artifactId>
   <version>Camden.SRS</version>
   <type>pom</type>
   <scope> import</scope>
  </dependency>
 </dependencies>
 </dependencyManagement>
 
 
<dependency>
 <groupId>org.springframework.cloud</groupId>
 <artificatId>spring-cloud-client</artifactId>
 </dependency>
 
 CONFIG FIRST:
 
 bootstrap.properties
 spring.application.name = <your_app_name>
 spring.cloud.config.uri = http://localhost:8888/
 
 bootstrap.yml
 spring:
  application:
   name: <your_app_name>
  cloud:
   config:
    uri: http://localhost:8888/
    

DISCOVERY FIRST:

bootstrap.properties
spring.application.name = <your_app_name>
spring.cloud.config.discovery.enabled = true

bootstrap.yml
spring:
 application:
  name: <your_app_name>
 cloud:
  discovery:
   enabled: true
   
Note: don't forget to add eureka slient dependencies, service-url configuration, and 
@EnableDiscoveryClient




