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
 * A collection of {@link ChannelSubscriber channel subscribers}.
 */
@XmlRootElement(name = "channelSubscribers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelSubscribersResource {
  
  /** URI template string for the relative URL of this API resource. @see http://tools.ietf.org/html/rfc6570 */
  public static final String RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/subscribers";
  
  @XmlElementRef
  private List<ChannelSubscriberResource> channelSubscribers = new ArrayList<>();
  @XmlElement(name = "link")
  private List<Link> links = new ArrayList<>();
  
  // Private, as only exists only to keep JAXB implementation happy.
  private ChannelSubscribersResource() {
  }  
  
  public ChannelSubscribersResource(List<ChannelSubscriberResource> channelSubscribers, List<Link> links) {
    this.channelSubscribers = channelSubscribers;
    this.links = links;
  }

  public final List<ChannelSubscriberResource> getChannelSubscribers() {
    return this.channelSubscribers;
  }

  public final List<Link> getLinks() {
    return this.links;
  }    
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("channelSubscribers", this.channelSubscribers)
      .add("links", this.links)
      .toString();
    /* @formatter:on */    
  }  
}