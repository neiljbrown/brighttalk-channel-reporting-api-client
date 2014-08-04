/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.brighttalk.channels.reportingapi.v1.client.resource.WebcastStatus;

/**
 * Unit tests for {@link GetWebcastViewingsRequestParamsBuilder}.
 */
public class GetWebcastViewingsRequestParamsBuilderTest {

  /**
   * Tests {@link GetWebcastViewingsRequestParamsBuilder#GetWebcastViewingsRequestParamsBuilder} in the case where the
   * supplied webcast status is one of the set of status supported by the API - neither 'live' or 'recorded'.
   */
  @Test
  public void testGetWebcastViewingsRequestParamsBuilderUnsupportedWebcastStatus() {
    WebcastStatus unsupportedWebcastStatus = WebcastStatus.UPCOMING;
    try {
      new GetWebcastViewingsRequestParamsBuilder(null, unsupportedWebcastStatus, null);
      fail("Exepected IllegalArgumentException to be thrown.");
    } catch (IllegalArgumentException e) {
      assertTrue("Unexpected exception [" + e.toString() + "].",
          e.getMessage().matches(".*" + unsupportedWebcastStatus + ".*"));
    }
  }
}