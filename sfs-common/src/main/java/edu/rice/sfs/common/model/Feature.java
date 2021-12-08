package edu.rice.sfs.common.model;

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
public class Feature {

  String name;
  String description;
  ValueType valueType;
  String deltaTableColName;
  String dynamoTableColName;
}
