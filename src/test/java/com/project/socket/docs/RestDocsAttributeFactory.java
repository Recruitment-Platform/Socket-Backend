package com.project.socket.docs;

import org.junit.jupiter.api.Disabled;
import org.springframework.restdocs.snippet.Attributes.Attribute;

@Disabled
public class RestDocsAttributeFactory {

  public static Attribute constraintsField(String description) {
    return new Attribute("constraints", description);
  }

}
