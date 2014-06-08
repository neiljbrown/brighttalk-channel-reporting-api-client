/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2012.
 * All Rights Reserved.
 * $Id: codetemplates.xml 41756 2012-02-02 11:15:06Z rgreen $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

/**
 * An enumeration of request parameter names used to support aspects of paging through collection of API resources.
 */
public enum PagingApiRequestParamName {

  PAGE_SIZE("pageSize"), CURSOR("cursor");

  private String name;

  private PagingApiRequestParamName(String name) {
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