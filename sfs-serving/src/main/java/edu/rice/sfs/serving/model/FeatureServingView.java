package edu.rice.sfs.serving.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureServingView {

  String name;
  String dynamoTableName;
  String entityCol;
  Collection<String> featureCols;
}
