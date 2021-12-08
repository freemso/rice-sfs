package edu.rice.sfs.registry.model;

import edu.rice.sfs.common.model.FeatureTable;
import edu.rice.sfs.registry.database.po.FeatureTablePO;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureTableBO {

  private String name;
  private String description;
  private EntityBO entity;
  private Collection<FeatureBO> features;
  private String deltaTablePath;
  private String dynamoTableName;

  public String getEntityName() {
    return entity.getName();
  }

  public Collection<String> getFeatureNames() {
    return Optional.ofNullable(features).orElse(Collections.emptyList())
        .stream().map(FeatureBO::getName).collect(Collectors.toList());
  }

  public void addFeature(FeatureBO feature) {
    if (features == null) {
      features = new HashSet<>();
    }
    if (!features.contains(feature)) {
      features.add(feature);
    }
  }

  public void removeFeature(String feature) {
    features.removeIf(f -> Objects.equals(f.getName(), feature));
  }

  public FeatureTablePO toPO(boolean isNew) {
    return FeatureTablePO.builder()
        .name(getName())
        .description(getDescription())
        .entity(getEntityName())
        .features(getFeatureNames())
        .deltaTablePath(getDeltaTablePath())
        .dynamoTableName(getDynamoTableName())
        .isNew(isNew)
        .build();
  }

  public FeatureTable toDTO() {
    return FeatureTable.builder()
        .name(getName())
        .description(getDescription())
        .entity(getEntity().toDTO())
        .features(getFeatures().stream().map(FeatureBO::toDTO).collect(Collectors.toList()))
        .deltaTablePath(getDeltaTablePath())
        .dynamoTableName(getDynamoTableName())
        .build();
  }
}
