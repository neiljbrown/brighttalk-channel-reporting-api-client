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

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link SubscriberWebcastActivity subscriber webcast activity}.
 */
@XmlRootElement(name = "subscribersWebcastActivity")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscribersWebcastActivityResource {
  private List<SubscriberWebcastActivityResource> subscriberWebcastActivities = new ArrayList<>();
  private List<Link> links = new ArrayList<>();

  public SubscribersWebcastActivityResource(List<SubscriberWebcastActivityResource> subscriberWebcastActivities,
      List<Link> links) {
    this.subscriberWebcastActivities = subscriberWebcastActivities;
    this.links = links;
  }

  /**
   * @return the subscriberWebcastActivities
   */
  public final List<SubscriberWebcastActivityResource> getSubscriberWebcastActivities() {
    return this.subscriberWebcastActivities != null ? this.subscriberWebcastActivities
        : new ArrayList<SubscriberWebcastActivityResource>();
  }

  /**
   * @return the links
   */
  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("subscriberWebcastActivities", this.subscriberWebcastActivities)
      .add("links", this.links)
      .toString();
    /* @formatter:on */
  }
}