/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link NextPageUrl}.
 */
public class NextPageUrlTest {

  private static final String RESOURCE_URL = "https://api.test.brighttalk.net/v1/channel/1234/subscribers";

  private static final String CURSOR_REQUEST_PARAM_NAME = ApiRequestParamName.Paging.CURSOR.getName();
  
  private static final String FOO_REQUEST_PARAM_NAME = ApiRequestParamName.GetChannelSubscribersApi.SUBSCRIBED.getName();  

  /**
   * Tests {@link NextPageUrl#parse} in the error case where an invalid format URL is supplied.
   */
  @Test(expected = IllegalArgumentException.class)
  public void parseInvalidUrlFormat() {
    NextPageUrl.parse("foo");
  }

  /**
   * Tests {@link NextPageUrl#parse} in the error case where a valid format, non-HTTP URL is supplied.
   */
  @Test(expected = IllegalArgumentException.class)
  public void parseInvalidUrlNonHttpUrl() {
    NextPageUrl.parse("file://api.test.brighttalk.net/v1/channel/1234/subscribers");
  }

  /**
   * Tests {@link NextPageUrl#parse} in the error case where the supplied URL does not contain a cursor request param
   * which is mandatory for a next page URL.
   */
  @Test(expected = IllegalArgumentException.class)
  public void parseInvalidUrlMissingCursorRequestParam() {
    NextPageUrl.parse(RESOURCE_URL);
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the first and only request param in
   * the URL.
   */
  @Test
  public void parseForCursorWhenCursorFirstAndOnlyRequestParam() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor;
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the first but not the only (and hence
   * not last) request param in the URL.
   */
  @Test
  public void parseForCursorWhenCursorFirstButNotOnlyRequestParam() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor + "&foo=x";
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the last of more than one request
   * param.
   */
  @Test
  public void parseForCursorWhenCursorLastOfSeveralRequestParams() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?foo=x&" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor;
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the last of more than one request
   * param.
   */
  @Test
  public void parseForCursorWhenCursorNeitherFirstOrLastRequestParam() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?foo=x&" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor + "&bar=y";
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }
}