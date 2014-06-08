/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ApiDateTimeFormatter}.
 */
public class ApiDateTimeFormatterTest {
  
  private ApiDateTimeFormatter uut;

  @Before
  public void setUp() {
    this.uut = new ApiDateTimeFormatter();
  }

  /**
   * Tests {@link ApiDateTimeFormatter#formatAsDateTime} for a non-null date.
   */
  @Test
  public void testFormatAsDateTime() {
    Date date = new Date();
    String expectedFormatted = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_TIME_PATTERN).format(date);
    String actualFormatted = this.uut.formatAsDateTime(date);
    assertThat(actualFormatted, is(expectedFormatted));
  }
  
  /**
   * Tests {@link ApiDateTimeFormatter#formatAsDateOnly} for a non-null date.
   */
  @Test
  public void testFormatAsDateOnly() {
    Date date = new Date();
    String expectedFormatted = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_ONLY_PATTERN).format(date);
    String actualFormatted = this.uut.formatAsDateOnly(date);
    assertThat(actualFormatted, is(expectedFormatted));
  }
    
  /**
   * Tests {@link ApiDateTimeFormatter#parse} for a date/time string which conforms to the format expected by the 
   * reporting APIs and comprises all component fields - date, time (full resolution) and time zone indicator.
   * 
   * @throws Exception If an unexpected exception occurs.
   */
  @Test
  public void testParseValidFormatDateTimeString() throws Exception {
    String dateTimeString = "2014-04-30T21:32:21Z";
    Date expectedDate = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_TIME_PATTERN).parse(dateTimeString);
    Date actualDate = this.uut.parse(dateTimeString);
    assertThat(actualDate, is(expectedDate));
  }
  
  /**
   * Tests {@link ApiDateTimeFormatter#parse} for a date only string which conforms to the format expected by the 
   * reporting APIs.
   * 
   * @throws Exception If an unexpected exception occurs.
   */
  @Test
  public void testParseValidFormatDateOnlyString() throws Exception {
    String dateTimeString = "2014-04-30";
    Date expectedDate = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_ONLY_PATTERN).parse(dateTimeString);
    Date actualDate = this.uut.parse(dateTimeString);
    assertThat(actualDate, is(expectedDate));
  }
  
  private static SimpleDateFormat createSimpleDateFormat(String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    return sdf;
  } 
}