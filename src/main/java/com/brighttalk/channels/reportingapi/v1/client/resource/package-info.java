/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
/**
 * Package containing the classes which make up the BrightTALK reporting API resource model - the RESTful 
 * resource-oriented view of the BrightTALK domain.
 */
// Use a custom JAXB XmlAdapter for unmarshalling integer strings to overcome a bug in the one supplied in the JAXB RI
@XmlJavaTypeAdapter(type = int.class, value = IntegerXmlAdapter.class)
package com.brighttalk.channels.reportingapi.v1.client.resource;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.brighttalk.channels.reportingapi.v1.client.jaxb.IntegerXmlAdapter;