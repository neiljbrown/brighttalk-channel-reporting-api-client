/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neiljbrown.brighttalk.channels.reportingapi.v1.client.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.http.client.PreemptiveBasicAuthHttpRequestInterceptor;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.jaxb.CustomValidationEventHandler;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.ChannelResource;

/**
 * An instance of a {@link Configuration Spring Java Config} class which declares the objects used by the
 * {@link com.neiljbrown.brighttalk.channels.reportingapi.v1.client.spring.SpringApiClientImpl Spring implementation of
 * the API client}, which are to be managed by the Spring container, using {@link Bean} annotated methods, and wires-up
 * the dependencies of these beans. (The Java equivalent of more traditional XML-based Spring bean configuration).
 * <p>
 * Services which use the Spring implementation of the API client can utilise this bean configuration if they wish by
 * importing it into their own Spring (XML or Java) configuration using &lt;import&gt; or @Import and @Autowired
 * respectively.
 * 
 * @author Neil Brown
 */
@Configuration
@PropertySource("classpath:btalk-cor-api-v1-client-${environment:dev}.properties")
public class AppConfig {

  @Value("${apiService.hostName}")
  private String apiServiceHostName;
  @Value("${apiService.port}")
  private int apiServicePort;
  @Value("${apiUser.key}")
  private String apiUserKey;
  @Value("${apiUser.secret}")
  private String apiUserSecret;

  /**
   * The classes of exception which should be treated as fatal if they occur as the root cause of a marshalling or
   * unmmarshalling error reported to the application's configured JAXB ValidationEventHandler. Defaults to none (empty
   * list), meaning that validation errors are not considered fatal, which is identical to the default behaviour in JAXB
   * 2.0+.
   * 
   * @see CustomValidationEventHandler
   */
  // Add NumberFormatException.class to treat failures to parse integer strings as fatal errors
  // Add IllegalArgumentException.class to treat failures to parse date strings, and possibly others, as fatal errors
  private List<Class<? extends Exception>> marshallingErrorFatalExceptions = new ArrayList<>();

  /**
   * @return The application's {@link PropertySourcesPlaceholderConfigurer}. Resolves ${...} placeholders used within
   * bean definition property values and @Value annotations using the current Spring
   * {@link org.springframework.core.env.Environment} which itself has been loaded from declared PropertySource(s).
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /**
   * @param httpMessageConverters The list of {@link HttpMessageConverter} that the created {@link RestTemplate} should
   * used to read/write HTTP request and response body. Ultimately dictates the set of media-types supported by the
   * client.
   * 
   * @return The instance of {@link RestTemplate} used by the API client.
   */
  @Bean
  @Autowired
  // Note - @Value is needed on the injected parameter to get Spring to use a specific bean of type List rather than its
  // default behaviour of building a List from all the beans of the matching member type
  public RestTemplate apiClientRestTemplate(
      @Value("#{httpMessageConverters}") List<HttpMessageConverter<?>> httpMessageConverters) {
    // Use custom list of HttpMessageConverter rather than the default to allow the marshalling config to be customised
    RestTemplate restTemplate = new RestTemplate(httpMessageConverters);
    restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    restTemplate.setErrorHandler(this.responseErrorHandler());
    return restTemplate;
  }

  /**
   * Creates the list of {@link HttpMessageConverter} that the {@link RestTemplate} should use to read/write HTTP
   * request and response body. Ultimately dictates the set of media-types supported by the client.
   * <p>
   * A custom list of HTTP message converter are created rather than relying on the default ones provided by
   * RestTemplate to support the supply of a custom configured marshaller. The list of created HTTP message converter
   * will include a {@link MarshallingHttpMessageConverter} that uses the supplied {@link Marshaller}.
   * 
   * @param marshaller The pre-configured {@link org.springframework.oxm.Marshaller} that the created
   * {@link MarshallingHttpMessageConverter} should use. Must also support unmarshalling, by implementing
   * {@link org.springframework.oxm.Unmarshaller}.
   * 
   * @return The created list of {@link HttpMessageConverter}.
   */
  // Note - The bean is given a specific name in this case to support the injection of a bean of type List using @Value
  @Bean(name = "httpMessageConverters")
  @Autowired
  public List<HttpMessageConverter<?>> httpMessageConverters(Marshaller marshaller) {
    return Arrays.asList(new HttpMessageConverter<?>[] { new MarshallingHttpMessageConverter(marshaller) });
  }

  /**
   * Creates and configures a {@link Marshaller} to be used for both marshalling and unmarshalling HTTP request and
   * response bodies.
   * <p>
   * The created Marshaller is configured with a custom JAXB {@link javax.xml.bind.ValidationEventHandler} which
   * supports logging not fatal validation errors that occur on unmarshalling, and optionally classifying them as fatal
   * errors depending on the class of causal ('linked') exception.
   * 
   * @return The created {@link Marshaller}.
   */
  @Bean
  public Marshaller marshaller() {
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    CustomValidationEventHandler eventHandler = new CustomValidationEventHandler();
    eventHandler.setFatalLinkedExceptions(this.marshallingErrorFatalExceptions);
    jaxb2Marshaller.setValidationEventHandler(eventHandler);
    Package apiResourcesRootPackage = ChannelResource.class.getPackage();
    jaxb2Marshaller.setPackagesToScan(new String[] { apiResourcesRootPackage.getName() });
    return jaxb2Marshaller;
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
    AuthScope authScope = new AuthScope(this.apiServiceHostName, this.apiServicePort);
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(this.apiUserKey, this.apiUserSecret);
    credentialsProvider.setCredentials(authScope, credentials);
    builder.setDefaultCredentialsProvider(credentialsProvider);
    builder.addInterceptorFirst(new PreemptiveBasicAuthHttpRequestInterceptor());    
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