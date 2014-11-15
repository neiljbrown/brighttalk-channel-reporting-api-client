/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.neiljbrown.brighttalk.channels.reportingapi.v1.client.http.client;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link HttpRequestInterceptor} which when added to an HttpClient ensures that requests are sent
 * with any basic authentication credentials with a matching auth scope that are registered with the client's
 * credentials provider, rather than waiting for an authentication challenge from the server. This improves efficiency
 * of the HTTP client by avoiding an extra handshake (request/response cycle), with sacrificing security.
 */
public class PreemptiveBasicAuthHttpRequestInterceptor implements HttpRequestInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(PreemptiveBasicAuthHttpRequestInterceptor.class);

  /**
   * {@inheritDoc}
   * <p>
   * Performs the steps necessary to ensure that all HTTP requests will include basic authentication credentials, if 
   * some have been previously registered with a matching authentication scope (target host and port).
   */
  @Override
  public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    // If no auth scheme has been set for the current request, try to initialize it preemptively
    AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);
    if (authState.getAuthScheme() == null) {
      // Get the credentials which have been registered for the target host and port
      HttpHost targetHost = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
      CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(HttpClientContext.CREDS_PROVIDER);
      // For added security only preemptively send credentials which have been registered with a matching auth scope
      Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
      if (creds != null) {
        authState.update(new BasicScheme(), creds);
        logger.debug("Added authentication credentials to request for preemptive authentication.");
      } else {
        logger.debug("Unable add authentication credentials to request for preemptive authentication. No matching "
            + "credentials found for auth scope of current request - host [{0}] and port [{1}]",
            targetHost.getHostName(), targetHost.getPort());
      }
    }
  }
}
