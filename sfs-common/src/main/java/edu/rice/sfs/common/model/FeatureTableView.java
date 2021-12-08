package edu.rice.sfs.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureTableView {

  String name;
  String featureTableName;
  Collection<String> featureNames;
}
