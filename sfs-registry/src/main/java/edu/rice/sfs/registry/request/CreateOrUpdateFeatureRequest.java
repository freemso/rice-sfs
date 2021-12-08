package edu.rice.sfs.registry.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.rice.sfs.common.model.ValueType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrUpdateFeatureRequest {

  String name;
  String description;
  ValueType valueType;
  String deltaTableColName;
  String dynamoTableColName;
}
