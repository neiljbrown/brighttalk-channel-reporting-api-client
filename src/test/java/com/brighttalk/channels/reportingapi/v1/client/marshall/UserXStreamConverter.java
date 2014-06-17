/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.marshall;


import com.brighttalk.channels.reportingapi.v1.client.resource.User;
import com.brighttalk.channels.reportingapi.v1.client.resource.UserRealm;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of {@link User}.
 */
public class UserXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")  
  public boolean canConvert(Class type) {
    return type.equals(User.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    int id = Integer.valueOf(reader.getAttribute("id"));
    String email = null;
    UserRealm userRealm = null;
    String realmUserId = null;
    String firstName = null;
    String lastName = null;
    String phone = null;
    String jobTitle = null;
    String level = null;
    String companyName = null;
    String companySize = null;
    String industry = null;
    String country = null;
    String stateProvince = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("email".equals(nodeName)) {
        email = reader.getValue();
      } else if ("realm".equals(nodeName)) {
        userRealm = new UserRealm(Integer.valueOf(reader.getAttribute("id")));
      } else if ("realmUserId".equals(nodeName)) {
        realmUserId = reader.getValue();
      } else if ("firstName".equals(nodeName)) {
        firstName = reader.getValue();
      } else if ("lastName".equals(nodeName)) {
        lastName = reader.getValue();
      } else if ("phone".equals(nodeName)) {
        phone = reader.getValue();
      } else if ("jobTitle".equals(nodeName)) {
        jobTitle = reader.getValue();
      } else if ("level".equals(nodeName)) {
        level = reader.getValue();
      } else if ("companyName".equals(nodeName)) {
        companyName = reader.getValue();        
      } else if ("companySize".equals(nodeName)) {
        companySize = reader.getValue();
      } else if ("industry".equals(nodeName)) {
        industry = reader.getValue();
      } else if ("country".equals(nodeName)) {
        country = reader.getValue();
      } else if ("stateProvince".equals(nodeName)) {
        stateProvince = reader.getValue();
      }
      reader.moveUp();
    }
    return new User(id, email, userRealm, realmUserId, firstName, lastName, phone, jobTitle, level, companyName, 
        companySize, industry, country, stateProvince);    
  }
}
