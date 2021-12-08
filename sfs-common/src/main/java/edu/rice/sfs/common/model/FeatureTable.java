package edu.rice.sfs.common.model;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class FeatureTable {

  String name;
  String description;
  Entity entity;
  Collection<Feature> features;
  String deltaTablePath;
  String dynamoTableName;
}
