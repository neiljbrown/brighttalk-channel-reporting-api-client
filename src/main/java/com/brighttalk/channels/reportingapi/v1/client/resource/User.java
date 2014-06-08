/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.base.Objects;

/**
 * The set of BrightTALK subscriber's registered user details which are accessible to a Channel owner.
 */
public class User {
  @XmlAttribute
  private final int id;
  private final String email;
  private final UserRealm realm;
  private final String realmUserId;
  private final String firstName;
  private final String lastName;
  private final String phone;
  private final String jobTitle;
  private final String level;
  private final String companyName;
  private final String companySize;
  private final String industry;
  private final String country;
  private final String stateProvince;
  
  public User(int id, String email, UserRealm realm, String realmUserId, String firstName, String lastName,
      String phone, String jobTitle, String level, String companyName, String companySize, String industry,
      String country, String stateProvince) {
    this.id = id;
    this.email = email;
    this.realm = realm;
    this.realmUserId = realmUserId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.jobTitle = jobTitle;
    this.level = level;
    this.companyName = companyName;
    this.companySize = companySize;
    this.industry = industry;
    this.country = country;
    this.stateProvince = stateProvince;
  }

  public final int getId() {
    return this.id;
  }

  public final String getEmail() {
    return this.email;
  }

  public final UserRealm getRealm() {
    return this.realm;
  }

  public final String getRealmUserId() {
    return this.realmUserId;
  }

  public final String getFirstName() {
    return this.firstName;
  }

  public final String getLastName() {
    return this.lastName;
  }

  public final String getPhone() {
    return this.phone;
  }

  public final String getJobTitle() {
    return this.jobTitle;
  }

  public final String getLevel() {
    return this.level;
  }

  public final String getCompanyName() {
    return this.companyName;
  }

  public final String getCompanySize() {
    return this.companySize;
  }

  public final String getIndustry() {
    return this.industry;
  }

  public final String getCountry() {
    return this.country;
  }

  public final String getStateProvince() {
    return this.stateProvince;
  }
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .add("email", this.email)
      .add("realm", this.realm)      
      .add("realmUserId", this.realmUserId) 
      .add("firstName", this.firstName)      
      .add("lastName", this.lastName)
      .add("phone", this.phone)      
      .add("jobTitle", this.jobTitle)      
      .add("level", this.level)
      .add("companyName", this.companyName)      
      .add("companySize", this.companySize)
      .add("industry", this.industry)
      .add("country", this.country)
      .add("stateProvince", this.stateProvince)      
      .toString();
    /* @formatter:on */    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.companyName == null) ? 0 : this.companyName.hashCode());
    result = prime * result + ((this.companySize == null) ? 0 : this.companySize.hashCode());
    result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
    result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
    result = prime * result + ((this.firstName == null) ? 0 : this.firstName.hashCode());
    result = prime * result + this.id;
    result = prime * result + ((this.industry == null) ? 0 : this.industry.hashCode());
    result = prime * result + ((this.jobTitle == null) ? 0 : this.jobTitle.hashCode());
    result = prime * result + ((this.lastName == null) ? 0 : this.lastName.hashCode());
    result = prime * result + ((this.level == null) ? 0 : this.level.hashCode());
    result = prime * result + ((this.phone == null) ? 0 : this.phone.hashCode());
    result = prime * result + ((this.realm == null) ? 0 : this.realm.hashCode());
    result = prime * result + ((this.realmUserId == null) ? 0 : this.realmUserId.hashCode());
    result = prime * result + ((this.stateProvince == null) ? 0 : this.stateProvince.hashCode());
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
    User other = (User) obj;
    if (this.companyName == null) {
      if (other.companyName != null) {
        return false;
      }
    } else if (!this.companyName.equals(other.companyName)) {
      return false;
    }
    if (this.companySize == null) {
      if (other.companySize != null) {
        return false;
      }
    } else if (!this.companySize.equals(other.companySize)) {
      return false;
    }
    if (this.country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!this.country.equals(other.country)) {
      return false;
    }
    if (this.email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!this.email.equals(other.email)) {
      return false;
    }
    if (this.firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!this.firstName.equals(other.firstName)) {
      return false;
    }
    if (this.id != other.id) {
      return false;
    }
    if (this.industry == null) {
      if (other.industry != null) {
        return false;
      }
    } else if (!this.industry.equals(other.industry)) {
      return false;
    }
    if (this.jobTitle == null) {
      if (other.jobTitle != null) {
        return false;
      }
    } else if (!this.jobTitle.equals(other.jobTitle)) {
      return false;
    }
    if (this.lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!this.lastName.equals(other.lastName)) {
      return false;
    }
    if (this.level == null) {
      if (other.level != null) {
        return false;
      }
    } else if (!this.level.equals(other.level)) {
      return false;
    }
    if (this.phone == null) {
      if (other.phone != null) {
        return false;
      }
    } else if (!this.phone.equals(other.phone)) {
      return false;
    }
    if (this.realm == null) {
      if (other.realm != null) {
        return false;
      }
    } else if (!this.realm.equals(other.realm)) {
      return false;
    }
    if (this.realmUserId == null) {
      if (other.realmUserId != null) {
        return false;
      }
    } else if (!this.realmUserId.equals(other.realmUserId)) {
      return false;
    }
    if (this.stateProvince == null) {
      if (other.stateProvince != null) {
        return false;
      }
    } else if (!this.stateProvince.equals(other.stateProvince)) {
      return false;
    }
    return true;
  }    
}