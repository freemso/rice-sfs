package edu.rice.sfs.registry.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrUpdateFeatureTableRequest {

  String name;
  String description;
  String entity;
  Collection<String> features;
  String deltaTablePath;
  String dynamoTableName;
}
