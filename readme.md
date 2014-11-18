# BrightTALK Channel Owner Reporting API (V1) Client

## Overview
This project contains the source code for an API client for [BrightTALK's](https://www.brighttalk.com/) 
[Channel Owner Reporting API](https://www.brighttalk.com/todo).

The API client is implemented in Java (7), and packaged as a JAR file. An implementation of the client API is provided 
that is built using the Spring framework (4.x). The source code is made available under the popular 
[Apache License 2.0](http://en.wikipedia.org/wiki/Apache_License). 

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