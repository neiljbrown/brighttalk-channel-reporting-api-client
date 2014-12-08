# BrightTALK Channel Owner Reporting API (V1) Client

## Overview
This project contains the source code for an API client for [BrightTALK's](https://www.brighttalk.com/) 
[Channel Owner Reporting API](https://www.brighttalk.com/todo).

The API client is implemented in Java, and packaged as a JAR file. An implementation of the client API is provided 
that is built using the Spring framework. The source code is made available under the popular 
[Apache License 2.0](http://en.wikipedia.org/wiki/Apache_License). 

## Runtime Dependencies
The API client requires a minimum of a Java 7 runtime (JRE) and the minimum versions of the following third-party libraries on the classpath: 

* com.google.guava:guava:16.0.1
* org.slf4j:slf4j-api:1.7.7
* org.springframework:spring-context:4.0.6.RELEASE
* org.springframework:spring-oxm:4.0.6.RELEASE 
* org.springframework:spring-web:4.0.6.RELEASE
* org.apache.httpcomponents:httpclient:4.3.3
 
To enable API client logging you'll additionally need to add the SLF4J "binding" JAR for your chosen logging framework. 
For more details see the [SLF4J user manual](http://www.slf4j.org/manual.html).   

## Code / API Documentation
Javadoc is available [online](https://to-do). If you wish to familiarise yoursefl with the code, good places to start 
are the [ApiClient](https://to-do) interface and its Spring implementation [SpringApiClientImpl](https://to-do), and 
also the tests. 

## Automated Tests
Unit tests are implemented in JUnit (4) and Hamcrest and can be found in the project's src/test/java folder.

The Spring implementation of the API client has an extensive set of integration tests. See the 
SpringApiClientIntegrationTest class. These tests use the 
[Spring MVC Test framework](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#spring-mvc-test-framework) 
to exercise the API client end-to-end against a mock implementation of the API service, including loading the Spring 
application context and unmarshalling of canned API responses, without the need to launch a Servlet container.

The Spring implementation also has a set of functional tests which additionally serve to test the production 
implementation of the API client's underlying HTTP client in conjunction with a stubbed API server (implemented using 
[WireMock](http://wiremock.org/)). See the SpringApiClientImplFunctionalTest class.     

## Building from source
Support is provided for building the project from source using both Maven (see pom.xml) and Gradle (see build.gradle).

--    
Neil Brown.