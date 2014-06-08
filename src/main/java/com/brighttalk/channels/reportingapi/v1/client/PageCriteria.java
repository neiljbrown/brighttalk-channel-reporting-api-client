/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

/**
 * Encapsulates the paging criteria for all paged reporting APIs, e.g. those which return collections of resources.
 * <P>
 * TODO - Consider whether to support next page API calls through the supply of a ResourceLink DTO (with validation 
 * of its relation type) rather than the next page URL. Does that have the benefit of avoiding the need for user of 
 * API client to have to extract URL from href attribute? Do we want to couple this class to the ResourceLink DTO?  
 */
public class PageCriteria {
  private final Integer pageSize;
  private final String nextPageUrl;

  /**
   * Creates page criteria for a request for the first page of resources.
   * 
   * @param pageSize The max no. of entries to return in the requested page of resources. Optional. If null the API's
   * default page size will apply.
   */
  public PageCriteria(Integer pageSize) {
    this(pageSize, null);
  }

  /**
   * Creates page criteria for the next page of resources, using the default page size.
   * 
   * @param nextPageUrl The URL of the next page of a collection of resources, as retured by the API in the 'href'
   * attribute of a link relation of type 'next' in the previous page of resources. Optional, only supplied for API
   * calls for secondary pages of resources.
   */
  public PageCriteria(String nextPageUrl) {
    this(null, nextPageUrl);
  }

  /**
   * Creates page criteria for the next page of resources, with a specified page size.
   * 
   * @param pageSize The max no. of entries to return in the requested page of resources. Optional. If null the API's
   * default page size will apply.
   * @param nextPageUrl The URL of the next page of a collection of resources, as retured by the API in the 'href'
   * attribute of a link relation of type 'next' in the previous page of resources. Optional, only supplied for API
   * calls for secondary pages of resources.
   */
  // TODO - Consider adding validation of nextPageUrl
  public PageCriteria(Integer pageSize, String nextPageUrl) {
    this.pageSize = pageSize;
    this.nextPageUrl = nextPageUrl;
  }

  public final Integer getPageSize() {
    return this.pageSize;
  }

  public final String getNextPageUrl() {
    return this.nextPageUrl;
  }
}