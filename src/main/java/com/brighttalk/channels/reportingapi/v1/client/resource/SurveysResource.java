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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link SurveyResource surveys}.
 */
@XmlRootElement(name = "surveys")
@XmlAccessorType(XmlAccessType.FIELD)
public class SurveysResource {
  
  /** URI template string for the relative URL of the Surveys resource for an identified channel. */
  public static final String FOR_CHANNELS_RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/surveys";
  
  @XmlElementRef
  private List<SurveyResource> surveys;
  
  // Private, as only exists only to keep JAXB implementation happy. 
  private SurveysResource() {
  }
  
  public SurveysResource(List<SurveyResource> surveys) {
    this.surveys = surveys;
  }
  
  public final List<SurveyResource> getSurveys() {
    return this.surveys != null ? this.surveys : new ArrayList<SurveyResource>();
  }
    
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("surveys", this.surveys)
      .toString();
    /* @formatter:on */    
  }   
}