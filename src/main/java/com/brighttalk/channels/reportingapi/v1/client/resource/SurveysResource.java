/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link SurveyResource surveys}.
 */
@XmlRootElement(name = "surveys")
@XmlAccessorType(XmlAccessType.FIELD)
public class SurveysResource {
  private final List<SurveyResource> surveys;
  private final List<Link> links;
  
  public SurveysResource(List<SurveyResource> surveys, List<Link> links) {
    this.surveys = surveys;
    this.links = links;
  }
  
  public final List<SurveyResource> getSurveys() {
    return this.surveys != null ? this.surveys : new ArrayList<SurveyResource>();
  }
  
  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("surveys", this.surveys)
      .add("links", this.links)      
      .toString();
    /* @formatter:on */    
  }   
}