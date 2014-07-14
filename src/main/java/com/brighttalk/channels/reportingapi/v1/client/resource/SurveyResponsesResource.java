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
 * A collection of {@link SurveyResponseResource survey response}.
 */
@XmlRootElement(name = "surveyResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class SurveyResponsesResource {
  
  /** URI template string for the relative URL of the SurveyResponses resource. */
  public static final String RELATIVE_URI_TEMPLATE = "/v1/survey/{surveyId}/responses";
  
  @XmlElementRef
  private List<SurveyResponseResource> surveyResponses;
  @XmlElement(name = "link")
  private List<Link> links;
  
  // Private, as only exists only to keep JAXB implementation happy.
  private SurveyResponsesResource() {
  }  
  
  public SurveyResponsesResource(List<SurveyResponseResource> surveyResponses, List<Link> links) {
    this.surveyResponses = surveyResponses;
    this.links = links;
  }

  public final List<SurveyResponseResource> getSurveyResponses() {
    return this.surveyResponses != null ? this.surveyResponses : new ArrayList<SurveyResponseResource>();
  }

  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("surveyResponses", this.surveyResponses)      
      .add("links", this.links)      
      .toString();
    /* @formatter:on */    
  }   
}