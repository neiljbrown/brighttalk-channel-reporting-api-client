/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

/**
 * ...
 * <p>
 * Use {@link #name()} for the literal value of the request parameter name to be used in URLs.
 */
public class ApiRequestParamName {

  /**
   * Enumeration of names of request params used to support aspects of paging through collection of API resources.
   */
  public enum Paging {

    PAGE_SIZE("pageSize"), CURSOR("cursor");

    private String name;

    private Paging(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 
   * TODO
   */
  enum GetChannelSubscribersApi {

    SUBSCRIBED("subscribed"), SUBSCRIBED_SINCE("subscribedSince"), UNSUBSCRIBED_SINCE("unsubscribedSince");

    private String name;

    private GetChannelSubscribersApi(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
    
    @Override
    public String toString() {
      return this.name;
    }    
  }
}