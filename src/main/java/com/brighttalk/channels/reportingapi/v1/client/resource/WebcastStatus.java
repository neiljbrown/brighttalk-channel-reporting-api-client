/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

/**
 * The status of a webcast.
 */
public enum WebcastStatus {
  
  /** The webcast is scheduled to go live at a future start date/time. */
  UPCOMING, 
  
  CANCELLED, 
  
  /** The webcast is currently live. */
  LIVE,
  
  /** The webcast has ended and is being prepared for on-demand viewing. */
  PROCESSING,
  
  /** The webcast is available for on-demand viewing. */
  RECORDED,
  
  ERROR;

  /** 
   * {@inheritDoc}
   * <p>
   * This implementation returns the enum constant name in lowercase for consistency with the string representation 
   * used in the resource representations returned and accepted by the API service.  
   */
  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }           
}