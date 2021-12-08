package edu.rice.sfs.registry.database.po;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("feature_table_view")
public class FeatureTableViewPO implements Persistable<String> {

  @Id
  private String name;
  private String featureTableName;
  private Collection<String> featureNames;

  @Transient
  private boolean isNew;

  @Override
  public String getId() {
    return name;
  }
}
