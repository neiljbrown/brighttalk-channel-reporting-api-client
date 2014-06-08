/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Class of non-checked (runtime) exception thrown to report an HTTP error response, including I/O errors such as
 * connection and read timeouts.
 * <p>
 * Encapsulates the status code, headers and raw body of the response.
 * <p>
 * Inspired by org.springframework.web.client.HttpStatusCodeException.
 */
public abstract class HttpErrorResponseException extends ApiClientException {

  private static final Charset HTTP_RESPONSE_DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

  private final int statusCode;
  private final String statusText;
  private final Map<String, List<String>> responseHeaders;
  private final Charset responseCharset;
  private final byte[] responseBody;

  /**
   * Constructs an instance of the exception using a default response character set.
   * 
   * @param statusCode The HTTP response status code.
   * @param statusText The HTTP response status text.
   * @param responseHeaders The HTTP response headers.
   * @param responseBody The HTTP response body.
   */
  public HttpErrorResponseException(int statusCode, String statusText, Map<String, List<String>> responseHeaders,
      byte[] responseBody) {
    this(statusCode, statusText, responseHeaders, HTTP_RESPONSE_DEFAULT_CHARSET, responseBody);
  }

  /**
   * @param statusCode The HTTP response status code.
   * @param statusText The HTTP response status text.
   * @param responseHeaders The HTTP response headers.
   * @param responseCharset The character set of the HTTP response body.
   * @param responseBody The HTTP response body.
   */
  public HttpErrorResponseException(int statusCode, String statusText, Map<String, List<String>> responseHeaders,
      Charset responseCharset, byte[] responseBody) {
    super(statusCode + " " + statusText);
    this.statusCode = statusCode;
    this.statusText = statusText;
    this.responseHeaders = responseHeaders;
    this.responseCharset = responseCharset;
    this.responseBody = responseBody;
  }

  public static final Charset getHttpResponseDefaultCharset() {
    return HTTP_RESPONSE_DEFAULT_CHARSET;
  }

  public final int getStatusCode() {
    return this.statusCode;
  }

  public final String getStatusText() {
    return this.statusText;
  }

  public final Map<String, List<String>> getResponseHeaders() {
    return this.responseHeaders;
  }

  public final Charset getResponseCharset() {
    return this.responseCharset;
  }

  public final byte[] getResponseBody() {
    return this.responseBody;
  }

  /**
   * @return The response body as a string, encoded using the character set supplied on construction.
   * @see #getResponseBody()
   */
  public String getResponseBodyAsString() {
    return new String(this.responseBody, this.responseCharset);
  }
}