/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.marshall;

import java.util.ArrayList;
import java.util.List;

import com.brighttalk.channels.reportingapi.v1.client.resource.SurveyResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveysResource;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of
 * {@link SurveysResource} - a collection of {@link SurveyResource}.
 * 
 * @see SurveyResourceXStreamConverter
 */
public class SurveysResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(SurveysResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    List<SurveyResource> surveys = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      if ("survey".equals(reader.getNodeName())) {
        if (surveys == null) {
          surveys = new ArrayList<>();
        }
        SurveyResource survey = (SurveyResource) context.convertAnother(null, SurveyResource.class);
        surveys.add(survey);
      }
      reader.moveUp();
    }
    return new SurveysResource(surveys);
  }
}