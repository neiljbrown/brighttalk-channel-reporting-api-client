/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link WebcastRegistrationResource webcast registration}.
 */
@XmlRootElement(name = "webcastRegistrations")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebcastRegistrationsResource {
  
  /** URI template string for the relative URL of the Webcast Registrations for an identified webcast. */
  public static final String FOR_WEBCAST_RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/webcast/{webcastId}/registrations";  
  
  @XmlElementRef  
  private List<WebcastRegistrationResource> webcastRegistrations;
  
  @XmlElement(name = "link")
  private List<Link> links;
  
  // Private, as only exists only to keep JAXB implementation happy.
  private WebcastRegistrationsResource() {
  }  

  public WebcastRegistrationsResource(List<WebcastRegistrationResource> webcastRegistrations, List<Link> links) {
    this.webcastRegistrations = webcastRegistrations;
    this.links = links;
  }

  public final List<WebcastRegistrationResource> getWebcastRegistrations() {
    return this.webcastRegistrations != null ? this.webcastRegistrations : new ArrayList<WebcastRegistrationResource>();
  }

  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("webcastRegistrations", this.webcastRegistrations)
      .add("links", this.links)      
      .toString();
    /* @formatter:on */
  }
}