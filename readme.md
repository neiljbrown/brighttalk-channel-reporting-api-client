# BrightTALK Channel Owner Reporting API (V1) Client

## Overview
This project contains the source code and releases of an API client for [BrightTALK's](https://www.brighttalk.com/) 
Channel Owner Reporting API.

The API client is specified in Java. A production-ready implementation, built using the Spring framework, is also 
provided.  

The source code is made available under the [Apache License 2.0](http://en.wikipedia.org/wiki/Apache_License). 

## Downloads
A 'binary' distribution containing a pre-built copy of the API client is available for [download](https://github.com/neiljbrown), 
in tar.gz and zip formats. The distribution includes a classses JAR, supporting source and javadoc JARs, and the necessary 
configuration files.   
  
The JAR files are _not_ currently published to a public repository. Support for this will be added in the future, with a 
view to streamlining your build process.   

## Building from source
If you prefer to build the API client yourself, both Maven and Gradle build scripts are provided. See the pom.xml and 
build.gradle files in the project's root folder.

## Runtime Dependencies
The API client requires a minimum of a Java 7 runtime (JRE) and the minimum versions of the following third-party 
libraries on the classpath: 

* com.google.guava:guava:16.0.1
* org.slf4j:slf4j-api:1.7.7
* org.springframework:spring-context:4.0.6.RELEASE
* org.springframework:spring-oxm:4.0.6.RELEASE 
* org.springframework:spring-web:4.0.6.RELEASE
* org.apache.httpcomponents:httpclient:4.3.3
 
If you wish to enable the logging provided by the API client you'll additionally need to add the SLF4J "binding" JAR for 
your chosen logging framework. For more details see the [SLF4J user manual](http://www.slf4j.org/manual.html). For 
example, to use the native SLF4J binding logback, add the following to your classpath:

* ch.qos.logback:logback-classic:1.1.2  

## API Docs
If you wish to familiarise yourself with the code, good places to start are the Javadoc for the [ApiClient] interface 
and its Spring implementation [SpringApiClientImpl]. (Javadoc is currently included in the binary distribution. It 
will be made available online in the future).

## Automated Tests
Unit tests are implemented in JUnit (4) and Hamcrest and can be found in the project's src/test/java folder.

The Spring implementation of the API client has an extensive set of integration tests. See class 
SpringApiClientIntegrationTest. These tests use the 
[Spring MVC Test framework](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#spring-mvc-test-framework) 
to exercise the API client end-to-end against a mock implementation of the API service, including loading the Spring 
application context and unmarshalling of canned API responses.

The Spring implementation also has an additional set of integrations tests which serve to test the production 
implementation of the API client's underlying HTTP client in conjunction with a stubbed API server (implemented using 
[WireMock](http://wiremock.org/)). See class SpringApiClientImplStubbedApiServiceIntegrationTest.     

## Getting Started
This section outlines the steps to use the Spring implementation of the API client for the first time in your Java 
application, after you've downloaded the binaries or built the client from source.

1) Extend your application's build script to declare a compile-time dependency on the API client JAR - 
brighttalk-channel-reporting-api-client-{release}.jar. Also add the API client's runtime dependencies (see above).

2) Copy the API client environment specific property files (brighttalk-channel-reporting-api-client-*.properties) 
included in the binary distribution to your project, in a folder (e.g. src/main/resources) that will get deployed with 
your app and included on the classpath.

3) Tailor the API client environment specific properties. Currently, the only essential change you need to make 
is to re-configure the API credentials for your BrightTALK Channel(s), by setting the value of the `apiUser.*` properties. 
(These credentials can be obtained from [BrightTALK support](support@brighttalk.com)). 

4) Releases of the API client ship with Spring bean config that includes a fully configured, singleton instance of the 
Spring implementation of the API client for use. The Spring bean configuration is Java-based (`@Configuration`), 
implemented in class .spring.AppConfig. To make the API client available for auto-wiring in your own (e.g. DAO) class 
you'll need to import the `@Bean` configured in AppConfig.

If your application uses Java-based Spring bean config, use Spring's `@Import` annotation on one of your `@Configuration` 
classes, e.g. 

<pre>
  @Configuration  
  // Include beans packaged with BrightTALK reporting API client, to get a fully configured instance of ApiClient  
  @Import(com.neiljbrown.brighttalk.channels.reportingapi.client.spring.AppConfig.class)  
  ...  
  public class IntegrationConfig {  
    ...  
  }
</pre>

If your application uses Spring XML bean config, you can enable processing of Spring annotations using the <tt>&lt;context:annotation-config/></tt> element and include the shipped `@Configuration` class in your Spring XML, e.g.
<pre>
&lt!-- One of your Spring bean XML files, e.g. integration-config.xml -->
&lt;beans>
  ...
  &lt;!-- Enable processing of Spring annotations such as @Configuration -->
  &lt;context:annotation-config/>
  &lt;!-- Include beans packaged with BrightTALK reporting API client, to get a fully configured instance of ApiClient -->
  &lt;bean class="com.neiljbrown.brighttalk.channels.reportingapi.client.spring.AppConfig"/>  
  ...
&lt;/beans>
</pre>

For more details of the mechanisms for consuming the packaged `@Bean` configuration see the 
[Spring reference manual, section 'Composing Java-based configurations'](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-java-composing-configuration-classes).

5) If you wish to enable the logging provided by the Spring implementation of the API client then, in addition to 
adding the necessary runtime dependency for SLF4J (see above), you'll need to extend the logging configuration for your 
chosen logging framework. To assist with this, the API client ships with an example configuration for the Logback 
logging framework, which identifies the relevant loggers. See src/main/resources/logback-template.xml. 

Having completed the above steps you're now ready to inject the API client into your application class, e.g. 
<pre>
@Repository
public class BrightTalkChannelReportsDaoImpl implements BrightTalkChannelReportsDao {

  ...
  private ApiClient apiClient;
  ...

  @Autowired
  public BrightTalkChannelReportsDaoImpl(ApiClient apiClient) {
    this.apiClient = apiClient;
  }
  
  ...
}
</pre>

Good luck with building your BrightTALK reporting integration. 
   
--    
Neil Brown.