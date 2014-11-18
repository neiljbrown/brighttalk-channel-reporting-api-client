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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Preconditions;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.ApiClient;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.ApiClientException;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.GetChannelSubscribersRequestParamsBuilder;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.GetSubscribersWebcastActivityRequestParamsBuilder;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.GetSurveyResponsesRequestParamsBuilder;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.GetWebcastRegistrationsRequestParamsBuilder;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.GetWebcastViewingsRequestParamsBuilder;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.GetWebcastsRequestParamsBuilder;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.PageCriteria;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.PagingRequestParamsBuilder;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscribersResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.ChannelsResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.SubscribersWebcastActivityResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.SurveyResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.SurveyResponsesResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.SurveysResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.WebcastRegistrationsResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.WebcastResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.WebcastStatus;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.WebcastViewingsResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.WebcastsResource;

/**
 * An {@link ApiClient} implementation that delegates to a pre-configured instance of the Spring framework's
 * {@link RestTemplate} to make the authenticated, synchronous API calls.
 * <p>
 * The {@link RestTemplate} provides management of the underlying HTTP connection, binding of URL variables, marshalling
 * / unmarshalling HTTP request and response bodies, and error handling (adapting HTTP status codes to exceptions).
 * <p>
 * Diagnostic logging of request and responses is provided by the RestTemplate and its configured
 * {@link org.springframework.http.client.ClientHttpRequest} implementation.
 * <p>
 * Thread safe.
 * 
 * @author Neil Brown
 */
public class SpringApiClientImpl implements ApiClient {

  private static final String PROTOCOL_HTTP = "http";
  private static final String PROTOCOL_HTTPS = "https";
  private static final int HTTP_DEFAULT_PORT = 80;
  private static final int HTTPS_DEFAULT_PORT = 443;
  /** Compiled regex for a valid host name. See http://en.wikipedia.org/wiki/Hostname#Restrictions_on_valid_host_names */
  private static final Pattern VALID_HOST_NAME_PATTERN =
      Pattern.compile("[a-zA-Z0-9\\.\\-]{4,253}", Pattern.CASE_INSENSITIVE);

  private final String apiServerProtocol;
  private final String apiServerHostName;
  private final int apiServerPort;
  private final URI apiServerBaseUri;
  private final RestTemplate restTemplate;

  /**
   * Creates an instance of the API client that communicates with an identified API server, using the default protocol
   * and port number.
   * 
   * @param apiServerHostName The host name of the BrightTALK API server. A fully qualified domain name.
   * @param restTemplate The Spring {@link RestTemplate} this API client should use to make HTTP requests and process
   * the resulting HTTP response. The object must be fully configured with a connection factory supporting the required
   * API authentication and marshalling of all supported API resources to/from HTTP request and response bodies.
   * @see #SpringApiClientImpl(String, String, Integer, RestTemplate)
   */
  public SpringApiClientImpl(String apiServerHostName, RestTemplate restTemplate) {
    this(null, apiServerHostName, null, restTemplate);
  }

  /**
   * Creates an instance of the API client that communicates with a specified API server host.
   * 
   * @param apiServerProtocol The protocol used to communicate with the BrightTALK API server. One of "http" or "https".
   * Optional. If null defaults to "https".
   * @param apiServerHostName The host name of the BrightTALK API server. A fully qualified domain name.
   * @param apiServerPort The port of the BrightTALK API server. Optional. If null defaults to 80 or 443 depending on 
   * {@code apiServerProtocol}. 
   * @param restTemplate The Spring {@link RestTemplate} this API client should use to make HTTP requests and process
   * the resulting HTTP response. The object must be fully configured with a connection factory supporting the required
   * API authentication and marshalling of all supported API resources to/from HTTP request and response bodies.
   */
  public SpringApiClientImpl(String apiServerProtocol, String apiServerHostName, Integer apiServerPort,
      RestTemplate restTemplate) {
    if (apiServerProtocol == null) {
      apiServerProtocol = PROTOCOL_HTTPS;
    }
    Preconditions.checkArgument(
        PROTOCOL_HTTP.equalsIgnoreCase(apiServerProtocol) || PROTOCOL_HTTPS.equalsIgnoreCase(apiServerProtocol),
        "API server protocol must be one or '%s' or '%s', not [%s]", PROTOCOL_HTTP, PROTOCOL_HTTPS, apiServerProtocol);
    this.apiServerProtocol = apiServerProtocol.toLowerCase();

    Preconditions.checkNotNull(apiServerHostName, "API server host name must not be null.");
    Preconditions.checkArgument(VALID_HOST_NAME_PATTERN.matcher(apiServerHostName).matches(),
        "Invalid API server host name [%s].", apiServerHostName);
    this.apiServerHostName = apiServerHostName;

    if (apiServerPort == null) {
      apiServerPort = PROTOCOL_HTTP.equalsIgnoreCase(apiServerProtocol) ? HTTP_DEFAULT_PORT : HTTPS_DEFAULT_PORT;
    }
    Preconditions.checkArgument(apiServerPort > 0, "API server port must be a positive number, not [%s]", apiServerPort);
    this.apiServerPort = apiServerPort;

    this.restTemplate = Preconditions.checkNotNull(restTemplate, "RestTemplate must not be null.");

    this.apiServerBaseUri = initApiServerBaseUri(this.apiServerProtocol, this.apiServerHostName, this.apiServerPort);
  }

  /** {@inheritDoc} */
  @Override
  public ChannelsResource getMyChannels(PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, ChannelsResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public ChannelsResource getUserChannels(int userId, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        ChannelsResource.USER_CHANNELS_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, ChannelsResource.class, userId);
  }

  /** {@inheritDoc} */
  @Override
  public ChannelSubscribersResource getChannelSubscribers(int channelId, Boolean subscribed, Date subscribedSince,
      Date unsubscribedSince, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new GetChannelSubscribersRequestParamsBuilder(subscribed,
        subscribedSince, unsubscribedSince, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        ChannelSubscribersResource.RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, ChannelSubscribersResource.class, channelId);
  }

  /** {@inheritDoc} */
  @Override
  public SubscribersWebcastActivityResource getSubscribersWebcastActivityForChannel(int channelId, Date since,
      Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new GetSubscribersWebcastActivityRequestParamsBuilder(since,
        expandChannelSurveyResponse, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        SubscribersWebcastActivityResource.FOR_CHANNEL_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, SubscribersWebcastActivityResource.class, channelId);
  }

  /** {@inheritDoc} */
  @Override
  public SubscribersWebcastActivityResource getSubscribersWebcastActivityForWebcast(int channelId, int webcastId,
      Date since, Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new GetSubscribersWebcastActivityRequestParamsBuilder(since,
        expandChannelSurveyResponse, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        SubscribersWebcastActivityResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, SubscribersWebcastActivityResource.class, channelId,
        webcastId);
  }

  /** {@inheritDoc} */
  @Override
  public SurveysResource getSurveysForChannel(int channelId) throws ApiClientException {
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        SurveysResource.FOR_CHANNELS_RELATIVE_URI_TEMPLATE, null);
    return this.restTemplate.getForObject(absResourceUrlTemplate, SurveysResource.class, channelId);
  }

  /** {@inheritDoc} */
  @Override
  public SurveyResource getSurvey(int surveyId) throws ApiClientException {
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri, SurveyResource.RELATIVE_URI_TEMPLATE,
        null);
    return this.restTemplate.getForObject(absResourceUrlTemplate, SurveyResource.class, surveyId);
  }

  /** {@inheritDoc} */
  @Override
  public SurveyResponsesResource getSurveyResponses(int surveyId, Date since, PageCriteria pageCriteria)
      throws ApiClientException {
    Map<String, List<String>> requestParams = new GetSurveyResponsesRequestParamsBuilder(since, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        SurveyResponsesResource.RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, SurveyResponsesResource.class, surveyId);
  }

  /** {@inheritDoc} */
  @Override
  public WebcastsResource getWebcastsForChannel(int channelId, Date since, PageCriteria pageCriteria)
      throws ApiClientException {
    Map<String, List<String>> requestParams = new GetWebcastsRequestParamsBuilder(since, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri, WebcastsResource.RELATIVE_URI_TEMPLATE,
        requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, WebcastsResource.class, channelId);
  }

  /** {@inheritDoc} */
  @Override
  public WebcastResource getWebcast(int channelId, int webcastId) throws ApiClientException {
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri, WebcastResource.RELATIVE_URI_TEMPLATE,
        null);
    return this.restTemplate.getForObject(absResourceUrlTemplate, WebcastResource.class, channelId, webcastId);
  }

  /** {@inheritDoc} */
  @Override
  public WebcastRegistrationsResource getWebcastRegistrationsForWebcast(int channelId, int webcastId, Date since,
      Boolean viewed, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new GetWebcastRegistrationsRequestParamsBuilder(since, viewed,
        pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        WebcastRegistrationsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, WebcastRegistrationsResource.class, channelId,
        webcastId);
  }

  /** {@inheritDoc} */
  @Override
  public WebcastViewingsResource getWebcastViewingsForChannel(int channelId, Date since, WebcastStatus webcastStatus,
      PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new GetWebcastViewingsRequestParamsBuilder(since, webcastStatus,
        pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        WebcastViewingsResource.FOR_CHANNEL_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, WebcastViewingsResource.class, channelId);
  }

  /** {@inheritDoc} */
  @Override
  public WebcastViewingsResource getWebcastViewingsForWebcast(int channelId, int webcastId, Date since,
      WebcastStatus webcastStatus, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new GetWebcastViewingsRequestParamsBuilder(since, webcastStatus,
        pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUri,
        WebcastViewingsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, WebcastViewingsResource.class, channelId, webcastId);
  }

  /**
   * @return the apiServerProtocol
   */
  public final String getApiServerProtocol() {
    return this.apiServerProtocol;
  }

  /**
   * @return the apiServerHostName
   */
  public final String getApiServerHostName() {
    return this.apiServerHostName;
  }

  /**
   * @return the apiServerPort
   */
  public final int getApiServerPort() {
    return this.apiServerPort;
  }

  /**
   * @return The base (protocol, host name and optional port) {@link URI} of the API server which this client is
   * currently configured to use. This is an environment specific value.
   */
  public final URI getApiServerBaseUri() {
    try {
      // Return a new URI to preserve immutability
      return new URI(this.apiServerBaseUri.toString());
    } catch (URISyntaxException e) {
      throw new ApiClientException(e);
    }
  }

  private static URI initApiServerBaseUri(String protocol, String hostName, int port) {
    try {
      return new URI(protocol + "://" + hostName + ":" + port);
    } catch (URISyntaxException e) {
      throw new ApiClientException(e);
    }
  }

  /**
   * Builds an absolute HTTP URL from a supplied base URI, a relative path an an optional map of request parameters.
   * <p>
   * Supports building URLs before URL template variables have been expanded - template variable placeholders ({...})
   * will _not_ be encoded.
   * 
   * @param baseUri The {@link URL base URI}.
   * @param relativeUrlPath The relative path to be appended to the base URI.
   * @param requestParams An optional, map representation of request parameters to be appended to the URL. Can be null.
   * @return A String representation of the absolute URL. A return type of String rather than {@link java.net.URI} is
   * used to avoid encoding the URL before any template variables have been replaced.
   */
  private static String buildAbsoluteHttpUrl(URI baseUri, String relativeUrlPath,
      Map<String, List<String>> requestParams) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(baseUri);
    uriBuilder.path(relativeUrlPath);
    if (requestParams != null) {
      for (String paramName : requestParams.keySet()) {
        for (String paramValue : requestParams.get(paramName)) {
          uriBuilder.queryParam(paramName, paramValue);
        }
      }
    }
    UriComponents uriComponents = uriBuilder.build();
    return uriComponents.toUriString();
  }
}