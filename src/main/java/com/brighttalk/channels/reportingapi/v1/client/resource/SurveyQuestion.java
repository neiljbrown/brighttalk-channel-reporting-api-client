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
import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.base.Objects;

/**
 * A question in a Survey.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SurveyQuestion {
  @XmlAttribute
  private final int id;
  private final String reportingId;
  private final String type;
  private final String text;
  private final List<String> options;
  
  public SurveyQuestion(int id, String reportingId, String type, String text, List<String> options) {
    this.id = id;
    this.reportingId = reportingId;
    this.type = type;
    this.text = text;
    this.options = options;
  }

  public final int getId() {
    return this.id;
  }

  public final String getReportingId() {
    return this.reportingId;
  }

  public final String getType() {
    return this.type;
  }

  public final String getText() {
    return this.text;
  }

  public final List<String> getOptions() {    
    return this.options != null ? this.options : new ArrayList<String>();
  }
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .add("reportingId", this.reportingId)      
      .add("type", this.type)
      .add("text", this.text)
      .add("options", this.options)      
      .toString();
    /* @formatter:on */    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.id;
    result = prime * result + ((this.options == null) ? 0 : this.options.hashCode());
    result = prime * result + ((this.reportingId == null) ? 0 : this.reportingId.hashCode());
    result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
    result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
    SurveyQuestion other = (SurveyQuestion) obj;
    if (this.id != other.id) {
      return false;
    }
    if (this.options == null) {
      if (other.options != null) {
        return false;
      }
    } else if (!this.options.equals(other.options)) {
      return false;
    }
    if (this.reportingId == null) {
      if (other.reportingId != null) {
        return false;
      }
    } else if (!this.reportingId.equals(other.reportingId)) {
      return false;
    }
    if (this.text == null) {
      if (other.text != null) {
        return false;
      }
    } else if (!this.text.equals(other.text)) {
      return false;
    }
    if (this.type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!this.type.equals(other.type)) {
      return false;
    }
    return true;
  }    
}