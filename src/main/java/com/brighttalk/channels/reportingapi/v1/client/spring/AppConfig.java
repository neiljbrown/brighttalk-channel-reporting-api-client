/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.spring;

import java.util.Arrays;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * An instance of a {@link Configuration Spring Java Config} class which declares the objects used by the
 * {@link com.brighttalk.channels.reportingapi.v1.client.spring.SpringApiClientImpl Spring implementation of the API
 * client}, which are to be managed by the Spring container, using {@link Bean} annotated methods, and wires-up the
 * dependencies of these beans. (The Java equivalent of more traditional XML-based Spring bean configuration).
 * <p>
 * Services which use the Spring implementation of the API client can utilise this bean configuration if they wish by
 * importing it into their own Spring (XML or Java) configuration using &lt;import&gt; or @Import and @Autowired
 * respectively.
 */
@Configuration
@PropertySource("classpath:/com/brighttalk/channels/reportingapi/v1/client/application-${environment:production}.properties")
public class AppConfig {

  @Value("${apiservice.host}")
  private String apiServiceHost;
  @Value("${apiservice.port}")
  private int apiServicePort;
  @Value("${apiuser.key}")
  private String apiUserKey;
  @Value("${apiuser.secret}")
  private String apiUserSecret;

  /**
   * @return The application's {@link PropertySourcesPlaceholderConfigurer}. Resolves ${...} placeholders used within
   * bean definition property values and @Value annotations using the current Spring {@link Environment} which itself
   * has been loaded from declared PropertySource(s).
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /**
   * @return The instance of {@link RestTemplate} used by the API client.
   */
  @Bean
  public RestTemplate apiClientRestTemplate() {
    // Use the default set of HTTP message converters provided by RestTemplate. This will result in the RestTemplate
    // setting an Accept request header that will include a (*/xml) media-type supported by the Reporting API service,
    // as the default set of converters includes the JSE's JAXB converter which can read and write XML.
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    restTemplate.setErrorHandler(this.responseErrorHandler());
    return restTemplate;
  }

  /**
   * @return The instance of {@link ClientHttpRequestFactory} to be used to create HTTP requests.
   */
  @Bean
  public ClientHttpRequestFactory clientHttpRequestFactory() {
    // Use the Apache HttpComponents implementation of ClientHttpRequestFactory as it provides an HTTP client that
    // supports the use of authentication, connection pooling and returning HTTP response status codes for RESTful
    // errors without exceptions being thrown.
    return new HttpComponentsClientHttpRequestFactory(this.httpClient());
  }

  /**
   * @return The instance of {@link HttpClient} to be used by {@link ClientHttpRequestFactory} to create client
   * requests. Pre-configured to support basic authentication using externally configured API user credentials, and to
   * utilise the API service's support for HTTP response compression (using gzip).
   */
  @Bean
  public HttpClient httpClient() {
    HttpClientBuilder builder = HttpClients.custom();
    // Configure the basic authentication credentials to use for all requests
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    AuthScope authScope = new AuthScope(this.apiServiceHost, this.apiServicePort);
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(this.apiUserKey, this.apiUserSecret);
    credentialsProvider.setCredentials(authScope, credentials);
    builder.setDefaultCredentialsProvider(credentialsProvider);
    // HttpClient should by default set the Accept-Encoding request header to indicate the client supports HTTP
    // response compression using gzip
    return builder.build();
  }

  /**
   * @return The instance of {@link ResponseErrorHandler} to be used by {@link RestTemplate}.
   */
  @Bean
  public ResponseErrorHandler responseErrorHandler() {
    return new ApiResponseErrorHandler(
        Arrays.asList(new HttpMessageConverter<?>[] { new Jaxb2RootElementHttpMessageConverter() }));
  }
}