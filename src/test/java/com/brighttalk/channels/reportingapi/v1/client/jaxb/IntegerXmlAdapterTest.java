/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.jaxb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link IntegerXmlAdapter}.
 */
public class IntegerXmlAdapterTest {

  private IntegerXmlAdapter xmlAdapter = new IntegerXmlAdapter();

  /**
   * Tests {@link IntegerXmlAdapter#unmarshal} for a valid integer string.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testUnmarshalIntegerString() throws Exception {
    int i = 1;
    assertThat(this.xmlAdapter.unmarshal(String.valueOf(i)), is(i));
  }

  /**
   * Tests {@link IntegerXmlAdapter#unmarshal} for a non-integer string.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test(expected = NumberFormatException.class)
  public void testUnmarshalNonIntegerString() throws Exception {
    this.xmlAdapter.unmarshal(String.valueOf("foo"));
  }
  
  /**
   * Tests {@link IntegerXmlAdapter#marshal} for a null string.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testMarshalNull() throws Exception {
    assertThat(this.xmlAdapter.marshal(null), is(nullValue()));
  }  
}