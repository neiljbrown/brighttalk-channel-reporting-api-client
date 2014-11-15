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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Log4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.ChannelsResource;

/**
 * Functional tests for {@link SpringApiClientImpl}.
 * <p>
 * These tests supplement the integration tests for {@link SpringApiClientImpl} which contain the bulk of the test
 * coverage. However, as noted in the integration test case, those tests don't exercise the underlying HTTP client as
 * they're implemented using the Spring MVC Test framework, which substitutes the
 * {@link org.springframework.http.client.ClientHttpRequestFactory} to support stubbing and verification. These
 * functional tests are instead implemented with a stubbed HTTP server (using WireMock) which additionally test that the
 * class' configured HTTP client behaves as expected.
 * 
 * @author Neil Brown
 * @see SpringApiClientImplIntegrationTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class SpringApiClientImplFunctionalTest {

  private static final String API_SERVICE_PROTOCOL = "http"; // Use HTTP instead of HTTPS to avoid need for certs
  private static final String API_SERVICE_HOST = "localhost";
  private static final int API_SERVICE_PORT = 8081;

  /** Instance of class under test */
  private SpringApiClientImpl apiClient;

  /** Instance of {@link RestTemplate}, as configured for production. */
  @Autowired
  @Qualifier("apiClientRestTemplate")
  private RestTemplate restTemplate;

  /** Automate the startup and shutdown of the mock HTTP server before and after the execution of each test */
  @Rule
  public WireMockRule wireMockRule =
      new WireMockRule(wireMockConfig().bindAddress(API_SERVICE_HOST).port(API_SERVICE_PORT).notifier(
          new Log4jNotifier()));

  /**
   * @throws Exception If an unexpected exception occurs.
   */
  @Before
  public void setUp() throws Exception {
    this.apiClient = new SpringApiClientImpl(new URL(API_SERVICE_PROTOCOL, API_SERVICE_HOST, API_SERVICE_PORT, ""),
        this.restTemplate);
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when the API user has zero channels.
   */
  @Test
  public void getMyChannelsWhenZeroChannels() {
    String expectedRequestUrl = ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    WireMock.stubFor(get(urlEqualTo(expectedRequestUrl)).willReturn(
        aResponse().withStatus(200).withHeader("Content-Type", MediaType.APPLICATION_XML.toString()).withBody(
            "<channels/>")));

    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(0));
    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests that the API client performs preemptive basic authentication - includes authentication credentials in all its
   * requests, for the configured target host and post, rather than waiting to be challenged by the API server, thereby
   * avoiding the overhead of an extra request/response cycle.
   */
  @Test
  public void preemptiveBasicAuthentication() {
    String expectedRequestUrl = ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    WireMock.stubFor(get(urlEqualTo(expectedRequestUrl)).willReturn(
        aResponse().withStatus(200).withHeader("Content-Type", MediaType.APPLICATION_XML.toString()).withBody(
            "<channels/>")));

    this.apiClient.getMyChannels(null);

    WireMock.verify(getRequestedFor(urlEqualTo(expectedRequestUrl)).withHeader("Authorization", matching("Basic.*")));
  }

  /**
   * Tests that the API client takes advantage of the API server's support for HTTP (response) compression by include
   * HTTP header 'Accept-Encoding: gzip' in its requests.
   */
  @Test
  public void httpCompression() {
    String expectedRequestUrl = ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    WireMock.stubFor(get(urlEqualTo(expectedRequestUrl)).willReturn(
        aResponse().withStatus(200).withHeader("Content-Type", MediaType.APPLICATION_XML.toString()).withBody(
            "<channels/>")));

    this.apiClient.getMyChannels(null);

    WireMock.verify(getRequestedFor(urlEqualTo(expectedRequestUrl)).withHeader("Accept-Encoding", matching(".*gzip.*")));
  }
}