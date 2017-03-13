/*
 */
package org.tweb.translation.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Translation {

  public String lang;
  public String id;

  private Map<String, String> props = new LinkedHashMap<>();

  public Translation() {
  }

  public Translation(String id, String lang) {
    this.lang = lang;
    this.id = id;
  }

  public Map<String, String> getProps() {
    return props;
  }

}
