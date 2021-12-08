package edu.rice.sfs.registry.model;

import edu.rice.sfs.common.model.Feature;
import edu.rice.sfs.common.model.ValueType;
import edu.rice.sfs.registry.database.po.FeaturePO;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class FeatureBO {

  private String name;
  private String description;
  private ValueType valueType;
  private @Nullable String deltaTableColName;
  private @Nullable String dynamoTableColName;

  public FeaturePO toPO(boolean isNew) {
    return FeaturePO.builder()
        .name(getName())
        .description(getDescription())
        .valueType(getValueType())
        .deltaTableColName(getDeltaTableColName())
        .dynamoTableColName(getDynamoTableColName())
        .isNew(isNew)
        .build();
  }

  public Feature toDTO() {
    return Feature.builder()
        .name(getName())
        .description(getDescription())
        .valueType(getValueType())
        .deltaTableColName(getDeltaTableColName())
        .dynamoTableColName(getDynamoTableColName())
        .build();
  }
}
