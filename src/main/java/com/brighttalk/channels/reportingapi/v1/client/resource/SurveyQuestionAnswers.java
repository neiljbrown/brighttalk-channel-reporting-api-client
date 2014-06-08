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

import com.google.common.base.Objects;

/**
 * One or more answers to a survey question. 
 */
public class SurveyQuestionAnswers extends SurveyQuestion {
  private final List<String> answers;

  public SurveyQuestionAnswers(int id, String reportingId, String type, String text, List<String> options,
      List<String> answers) {
    super(id, reportingId, type, text, options);
    this.answers = answers;
  }

  public final List<String> getAnswers() {
    return this.answers != null ? this.answers : new ArrayList<String>();
  }
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.getId())
      .add("reportingId", this.getReportingId())      
      .add("type", this.getType())
      .add("text", this.getText())
      .add("options", this.getOptions())      
      .add("answers", this.getAnswers())      
      .toString();
    /* @formatter:on */    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.answers == null) ? 0 : this.answers.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SurveyQuestionAnswers other = (SurveyQuestionAnswers) obj;
    if (this.answers == null) {
      if (other.answers != null) {
        return false;
      }
    } else if (!this.answers.equals(other.answers)) {
      return false;
    }
    return true;
  }    
}