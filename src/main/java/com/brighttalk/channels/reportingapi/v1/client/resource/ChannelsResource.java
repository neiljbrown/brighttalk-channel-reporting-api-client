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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link ChannelResource channels}.
 */
@XmlRootElement(name = "channels")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelsResource {
  @XmlElementRef
  private List<ChannelResource> channels;
  @XmlElementRef
  private List<Link> links;

  // Private, as only exists only to keep JAXB implementation happy. 
  private ChannelsResource() {
  }

  public ChannelsResource(List<ChannelResource> channels, List<Link> links) {
    this.channels = channels;
    this.links = links;
  }

  public final List<ChannelResource> getChannels() {
    return this.channels != null ? this.channels : new ArrayList<ChannelResource>();
  }

  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("channels", this.channels)
      .add("links", this.links)
      .toString();    
    /* @formatter:on */
  }
}