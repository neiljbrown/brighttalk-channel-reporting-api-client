/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neiljbrown.brighttalk.channels.reportingapi.v1.client;

/**
 * Encapsulates the paging criteria for all paged reporting APIs, e.g. those which return collections of resources.
 * 
 * @author Neil Brown
 */
public class PageCriteria {
  private final Integer pageSize;
  private final NextPageUrl nextPageUrl;

  /**
   * Creates page criteria for a request for the first page of resources.
   * 
   * @param pageSize The max no. of entries to return in the requested page of resources. Optional. If null then 
   * API requests built from this criteria won't include a page size, instead relying on the API's default page size.
   */
  public PageCriteria(Integer pageSize) {
    this(pageSize, null);
  }
  
  /**
   * Creates page criteria for the next page of resources, using the default page size.
   * 
   * @param nextPageUrl The URL of the next page of a collection of resources, as returned by the API in the 'href'
   * attribute of a link relation of type 'next' in the previous page of resources. Optional, only supplied for API
   * calls for secondary pages of resources.
   */
  public PageCriteria(NextPageUrl nextPageUrl) {
    this(null, nextPageUrl);
  }

  /**
   * Creates page criteria for the next page of resources, with a specified page size.
   * 
   * @param pageSize The max no. of entries to return in the requested page of resources. Optional. If null then 
   * API requests built from this criteria won't include a page size, instead relying on the API's default page size.
   * @param nextPageUrl The URL of the next page of a collection of resources, as returned by the API in the 'href'
   * attribute of a link relation of type 'next' in the previous page of resources. Optional, only supplied for API
   * calls for secondary pages of resources.
   */
  public PageCriteria(Integer pageSize, NextPageUrl nextPageUrl) {
    this.pageSize = pageSize;
    this.nextPageUrl = nextPageUrl;
  }

  public final Integer getPageSize() {
    return this.pageSize;
  }

  public final NextPageUrl getNextPageUrl() {
    return this.nextPageUrl;
  }
}