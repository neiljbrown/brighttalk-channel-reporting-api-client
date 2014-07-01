/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.base.Objects;

/**
 * A link to an API resource.
 * <p>
 * Models relationships between API resources with semantic meaning which API clients can navigate.
 * <p>
 * Modelled on the link element used in HTML and Atom (but without the namespace) microformats. Comprises mandatory
 * properties for the target URL ('href') and relation type ('rel').
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public class Link {

  @XmlAttribute
  private String href;
  @XmlAttribute
  private String rel;
  @XmlAttribute
  private String title;

  // Private, as only exists only to keep JAXB implementation happy.
  private Link() {
  }

  public Link(String href, String rel) {
    this(href, rel, null);
  }

  public Link(String href, String rel, String title) {
    this.href = href;
    this.rel = rel;
    this.title = title;
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("href", this.href)
      .add("rel", this.rel)      
      .add("title", this.title)
      .toString();
    /* @formatter:on */
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.href == null) ? 0 : this.href.hashCode());
    result = prime * result + ((this.rel == null) ? 0 : this.rel.hashCode());
    result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Link other = (Link) obj;
    if (this.href == null) {
      if (other.href != null) {
        return false;
      }
    } else if (!this.href.equals(other.href)) {
      return false;
    }
    if (this.rel == null) {
      if (other.rel != null) {
        return false;
      }
    } else if (!this.rel.equals(other.rel)) {
      return false;
    }
    if (this.title == null) {
      if (other.title != null) {
        return false;
      }
    } else if (!this.title.equals(other.title)) {
      return false;
    }
    return true;
  }   
}