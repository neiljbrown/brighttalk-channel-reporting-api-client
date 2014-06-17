/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.spring;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.brighttalk.channels.reportingapi.v1.client.ApiClient;
import com.brighttalk.channels.reportingapi.v1.client.ApiClientException;
import com.brighttalk.channels.reportingapi.v1.client.GetChannelSubscribersRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.v1.client.PageCriteria;
import com.brighttalk.channels.reportingapi.v1.client.PagingRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscribersResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelsResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.MyChannelsResource;
import com.google.common.base.Preconditions;

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
 */
public class SpringApiClientImpl implements ApiClient {

  private final URL apiServerBaseUrl;
  private final RestTemplate restTemplate;

  /**
   * Creates a new instance of the API client.
   * <p>
   * TODO - Consider adding support for supplying the media type to use for the request Accept header. What is
   * RestTemplate's default behaviour if an Accept header is not explicitly set? The +getForXXX() methods set an Accept
   * header by default based on the configured underlying HTTP message converts, but how does it decide which of the
   * media types take precedence?
   * 
   * @param apiServerBaseUrl A {@link URL} specifying the protocol, hostname and optional port of the API server for a
   * given environment.
   * 
   * @param restTemplate The Spring {@link RestTemplate} this API client should use to make HTTP requests and process
   * the resulting HTTP response. The object must be fully configured with a connection factory supporting the required
   * API authentication and marshalling of all supported API resources to/from HTTP request and response bodies.
   */
  public SpringApiClientImpl(URL apiServerBaseUrl, RestTemplate restTemplate) {
    Preconditions.checkNotNull(apiServerBaseUrl, "API server base URL must be non-null.");
    this.apiServerBaseUrl = initApiServerBaseUrl(apiServerBaseUrl);
    this.restTemplate = Preconditions.checkNotNull(restTemplate, "RestTemplate must be non-null.");
  }

  /** {@inheritDoc} */
  @Override
  public ChannelsResource getMyChannels(PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUrl,
        ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, ChannelsResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public ChannelsResource getUserChannels(int userId, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUrl,
        ChannelsResource.USER_CHANNELS_RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, ChannelsResource.class, userId);
  }

  /** {@inheritDoc} */
  @Override
  public ChannelSubscribersResource getChannelSubscribers(int channelId, Boolean subscribed, Date subscribedSince,
      Date unsubscribedSince, PageCriteria pageCriteria) throws ApiClientException {
    Map<String, List<String>> requestParams = new GetChannelSubscribersRequestParamsBuilder(subscribed,
        subscribedSince, unsubscribedSince, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServerBaseUrl,
        ChannelSubscribersResource.RELATIVE_URI_TEMPLATE, requestParams);
    return this.restTemplate.getForObject(absResourceUrlTemplate, ChannelSubscribersResource.class, channelId);
  }

  public final RestTemplate getRestTemplate() {
    return this.restTemplate;
  }

  public final URL getApiServerBaseUrl() {
    try {
      // Return a new URL to preserve immutability
      return new URL(this.apiServerBaseUrl.toString());
    } catch (MalformedURLException e) {
      throw new ApiClientException(e);
    }
  }

  private static URL initApiServerBaseUrl(URL url) {
    try {
      return new URL(trimTraillingSlash(url.toString()));
    } catch (MalformedURLException e) {
      throw new ApiClientException(e);
    }
  }

  private static String trimTraillingSlash(String value) {
    return value.replaceFirst("(.*)\\/$", "$1");
  }

  /**
   * Builds an absolute HTTP URL from a supplied base URL, a relative path an an optional map of request parameters.
   * <p>
   * Supports building URLs before URL template variables have been expanded - template variable placeholders ({...})
   * will _not_ be encoded.
   * 
   * @param baseUrl The {@link URL base URL}.
   * @param relativeUrlPath The relative path to be appended to the base URL.
   * @param requestParams An optional, map representation of request parameters to be appended to the URL.
   * @return A String representation of the absolute URL. A return type of String rather than {@link java.net.URI} is
   * used to avoid encoding the URL before any template variables have been replaced.
   */
  private static String buildAbsoluteHttpUrl(URL baseUrl, String relativeUrlPath,
      Map<String, List<String>> requestParams) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl.toString());
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