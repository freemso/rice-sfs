package edu.rice.sfs.registry.database.po;

import edu.rice.sfs.common.model.ValueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("feature")
public class FeaturePO implements Persistable<String> {

  @Id
  private String name;
  private String description;
  private ValueType valueType;
  private @Nullable String deltaTableColName;
  private @Nullable String dynamoTableColName;

  @Transient
  private boolean isNew;

  @Override
  public String getId() {
    return name;
  }
}
