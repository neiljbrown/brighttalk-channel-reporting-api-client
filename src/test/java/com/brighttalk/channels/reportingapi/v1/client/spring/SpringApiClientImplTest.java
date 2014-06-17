/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import java.net.URL;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.brighttalk.channels.reportingapi.v1.client.ApiDateTimeFormatter;
import com.brighttalk.channels.reportingapi.v1.client.ApiErrorResponseException;
import com.brighttalk.channels.reportingapi.v1.client.NextPageUrl;
import com.brighttalk.channels.reportingapi.v1.client.PageCriteria;
import com.brighttalk.channels.reportingapi.v1.client.marshall.ChannelResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.v1.client.marshall.ChannelSubscriberResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.v1.client.marshall.ChannelSubscribersResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.v1.client.marshall.ChannelsResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.v1.client.marshall.EmbedXStreamConverter;
import com.brighttalk.channels.reportingapi.v1.client.marshall.LinkXStreamConverter;
import com.brighttalk.channels.reportingapi.v1.client.marshall.UserXStreamConverter;
import com.brighttalk.channels.reportingapi.v1.client.resource.ApiError;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscriberResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscribersResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelsResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.Embed;
import com.brighttalk.channels.reportingapi.v1.client.resource.Link;
import com.brighttalk.channels.reportingapi.v1.client.resource.User;
import com.thoughtworks.xstream.XStream;

/**
 * Unit tests for {@link SpringApiClientImpl}.
 * <p>
 * This test case is implemented as Spring-based integration test - it loads the configured Spring application context
 * using the {@link SpringJUnit4ClassRunner}. In addition to testing the Spring bean configuration, this supports using
 * the production configured instance of the RestTemplate as a test fixture.
 * <p>
 * The test case uses the Spring MVC Test framework’s {@link MockRestServiceServer} to provide a dynamically mocked
 * implementation of the Reporting API service, and a fluent API for asserting expected API requests, and specifying
 * stubbed responses.
 * <p>
 * When using the MockRestServiceServer, the RestTemplate is exercised exactly as it would be if it were making HTTP
 * requests to the real API service. The test coverage provided by these unit tests therefore includes marshalling and
 * unmarshalling the HTTP request and response bodies, exercising the API resource objects (DTOs) and their configured
 * marshallers and marshalling annotations in the process.
 * <p>
 * The MockRestServiceServer works by substituting the Spring {@link ClientHttpRequestFactory} for a mock
 * implementation. As a consequence, the coverage of these unit tests does NOT extend as far as testing the production
 * HTTP client (and its configuration). This requires a separate set of integration tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class SpringApiClientImplTest {

  private SpringApiClientImpl apiClient;
  private MockRestServiceServer mockReportingApiService;
  @Autowired(required = false)
  // Use a RestTemplate with default configuration if one is not injected
  private RestTemplate restTemplate = new RestTemplate();

  private XStream xstream;

  /**
   * @throws Exception If an unexpected exception occurs.
   */
  @Before
  public void setUp() throws Exception {
    this.mockReportingApiService = MockRestServiceServer.createServer(this.restTemplate);
    this.apiClient = new SpringApiClientImpl(new URL("https://api.test.brighttalk.net/"), this.restTemplate);
    initXStream();
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when the API user has zero channels.
   */
  @Test
  public void getMyChannelsWhenZeroChannels() {
    String expectedRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<channels/>", MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(0));
    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when the first page of returned resources contains multiple
   * channels, and the total no. of channels is greater than the API page size, resulting in a 'next' page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getMyChannelsWhenMultipleChannelsWithNextPage() throws Exception {
    String expectedRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getMyChannelsMultipleChannelsWithNextPage-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    ChannelsResource expectedChannelsResource = (ChannelsResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelResource expectedChannel = expectedChannelsResource.getChannels().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(channelsResource.getChannels().get(0), is(expectedChannel));

    // For the second of the two returned Resource just check its not the same as the first
    assertThat(channelsResource.getChannels().get(0).getId(), not(channelsResource.getChannels().get(1).getId()));

    assertThat(channelsResource.getLinks(), hasSize(1));
    assertThat(channelsResource.getLinks().get(0), is(expectedChannelsResource.getLinks().get(0)));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} with paging criteria that includes a next page link and a
   * non-default page size. The API responds with the final (last) page containing a single Channel.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getMyChannelsWhenNextPageWithNonDefaultPageSizeReturnsLastPage() throws Exception {
    // Construct page criteria to use in test
    int pageSize = 50;
    String nextPageUrlString = "https://api.test.brighttalk.net/v1/user/1/channels?cursor=1234&amp;pageSize=200";
    NextPageUrl nextPageUrl = NextPageUrl.parse(nextPageUrlString);
    PageCriteria pageCriteria = new PageCriteria(pageSize, nextPageUrlString);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    String expectedRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE + "?cursor=" + nextPageUrl.getCursor() + "&pageSize="
        + pageSize;
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getMyChannelsNextPageWithNonDefaultPageSizeReturnsLastPageSingleChannel-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    ChannelsResource expectedChannelsResource = (ChannelsResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelResource expectedChannel = expectedChannelsResource.getChannels().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(channelsResource.getChannels().get(0), is(expectedChannel));

    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} in the case where client error HTTP 401 Unauthorized is returned,
   * which can occur if API user credentials were not supplied in the request, or they were but they're incorrect.
   */
  @Test
  public void getMyChannelsWhenAuthenticationFailure() {
    String expectedRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withUnauthorizedRequest());

    try {
      this.apiClient.getMyChannels(null);
      fail("Expected exception to be thrown.");
    } catch (ApiErrorResponseException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    this.mockReportingApiService.verify();
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} in the case where the API service returns an HTTP 500 Internal
   * Server error, as a result of an unexpected system error.
   */
  @Test
  public void getMyChannelsWhenInternalServerError() {
    String expectedRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withServerError());

    try {
      this.apiClient.getMyChannels(null);
    } catch (ApiErrorResponseException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    this.mockReportingApiService.verify();
  }

  /**
   * Test {@link SpringApiClientImpl#getUserChannels(int, PageCriteria)} in the case where the identified user has zero
   * channels.
   */
  @Test
  public void getUserChanelsWhenZeroChannels() {
    int userId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelsResource.USER_CHANNELS_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(userId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<channels/>", MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getUserChannels(userId, null);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(0));
    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Test {@link SpringApiClientImpl#getChannelSubscribers} in the case where there identified channel has zero
   * subscribers.
   */
  @Test
  public void getChannelSubscribersWhenZeroSubscribers() {
    int channelId = 1;
    Boolean subscribed = false;
    String expectedTemplateRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE + "?subscribed=" + subscribed;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<channelSubscribers/>", MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, subscribed, null,
        null, null);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(0));
    assertThat(subscribersResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when retrieving all current and past subscribers and the first page
   * of returned resources contains multiple subscribers, and the total no. of subscribers is greater than the API page
   * size, resulting in a 'next' page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getChannelSubscribersWhenMultipleCurrentAndPastSusbcribersWithNextPage() throws Exception {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getChannelSubscribersWhenMultipleCurrentAndPastSusbcribersWithNextPage-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, null, null, null,
        null);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    ChannelSubscribersResource expectedSubscribersResource = (ChannelSubscribersResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelSubscriberResource expectedSubscriber = expectedSubscribersResource.getChannelSubscribers().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(subscribersResource.getChannelSubscribers().get(0), is(expectedSubscriber));

    // For the second of the two returned Resource just check its not the same as the first
    assertThat(subscribersResource.getChannelSubscribers().get(0).getId(),
        not(subscribersResource.getChannelSubscribers().get(1).getId()));

    assertThat(subscribersResource.getLinks(), hasSize(1));
    assertThat(subscribersResource.getLinks().get(0), is(expectedSubscribersResource.getLinks().get(0)));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when a request is made for the next page of all of the current
   * subscribers that subscribed since a specified date/time.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getChannelSubscribersWhenSubscribedSinceNextPage() throws Exception {
    int channelId = 1;
    Boolean subscribed = true;
    String subscribedSinceAsString = "2014-06-14T19:26:10Z";
    Date subscribedSinceDate = new ApiDateTimeFormatter().parse(subscribedSinceAsString);
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE + "?subscribed=" + subscribed + "&subscribedSince="
        + subscribedSinceAsString + "&cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    PageCriteria pageCriteria = new PageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getChannelSubscribersWhenSubscribedSinceNextPage-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, subscribed,
        subscribedSinceDate, null, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    ChannelSubscribersResource expectedSubscribersResource = (ChannelSubscribersResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelSubscriberResource expectedSubscriberResource = expectedSubscribersResource.getChannelSubscribers().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(subscribersResource.getChannelSubscribers().get(0), is(expectedSubscriberResource));

    assertThat(subscribersResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when a request is made for the next page of all of the past
   * subscribers that have unsubscribed since a specified date/time.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getChannelSubscribersWhenUnsubscribedSinceNextPage() throws Exception {
    int channelId = 1;
    String unsubscribedSinceAsString = "2014-06-16T22:06:40Z";
    Date unsubscribedSinceDate = new ApiDateTimeFormatter().parse(unsubscribedSinceAsString);
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE + "?unsubscribedSince=" + unsubscribedSinceAsString
        + "&cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    PageCriteria pageCriteria = new PageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getChannelSubscribersWhenSubscribedSinceNextPage-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, null, null, 
        unsubscribedSinceDate, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    ChannelSubscribersResource expectedSubscribersResource = (ChannelSubscribersResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelSubscriberResource expectedSubscriberResource = expectedSubscribersResource.getChannelSubscribers().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(subscribersResource.getChannelSubscribers().get(0), is(expectedSubscriberResource));

    assertThat(subscribersResource.getLinks(), hasSize(0));
  }

  /**
   * Test {@link SpringApiClientImpl#getChannelSubscribers} in the case when the requested channel does not exist.
   */
  @Test
  public void getChannelSubscribersWhenChannelNotFound() {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServerBaseUrl()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of Channel resources read from file
    ApiError apiError = new ApiError("ChannelNotFound", "Channel [" + channelId + "] not found");
    String responseBody = "<?xml version='1.0' encoding='UTF-8'?><error><code>" + apiError.getCode()
        + "</code><message>" + apiError.getMessage() + "</message></error>";
    MediaType mediaType = MediaType.APPLICATION_XML;
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withBadRequest().body(responseBody).contentType(mediaType));

    // Perform the test
    try {
      this.apiClient.getChannelSubscribers(channelId, null, null, null, null);
    } catch (ApiErrorResponseException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
      assertThat(e.getApiError(), is(apiError));
    }

    this.mockReportingApiService.verify();
  }
  
  /**
   * Creates and configures the {@link XStream} instance the test uses to unamrshall (deserialise) canned API response
   * payloads.
   */
  private void initXStream() {
    this.xstream = new XStream();
    this.xstream.alias("channels", ChannelsResource.class);
    this.xstream.alias("channel", ChannelResource.class);
    this.xstream.alias("link", Link.class);
    this.xstream.registerConverter(new ChannelsResourceXStreamConverter());
    this.xstream.registerConverter(new ChannelResourceXStreamConverter());
    this.xstream.registerConverter(new LinkXStreamConverter());

    this.xstream.alias("channelSubscribers", ChannelSubscribersResource.class);
    this.xstream.alias("channelSubscriber", ChannelSubscriberResource.class);
    this.xstream.alias("embed", Embed.class);
    this.xstream.alias("user", User.class);
    this.xstream.registerConverter(new ChannelSubscribersResourceXStreamConverter());
    this.xstream.registerConverter(new ChannelSubscriberResourceXStreamConverter());
    this.xstream.registerConverter(new EmbedXStreamConverter());
    this.xstream.registerConverter(new UserXStreamConverter());

  }
}