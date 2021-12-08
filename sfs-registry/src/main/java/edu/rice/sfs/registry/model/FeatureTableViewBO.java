package edu.rice.sfs.registry.model;

import edu.rice.sfs.common.model.FeatureTableView;
import edu.rice.sfs.registry.database.po.FeatureTableViewPO;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureTableViewBO implements BO<FeatureTableView, FeatureTableViewPO> {

  private String name;
  private String featureTableName;
  private Collection<String> featureNames;

  public FeatureTableViewPO toPO(boolean isNew) {
    return FeatureTableViewPO.builder()
        .name(getName())
        .featureTableName(getFeatureTableName())
        .featureNames(getFeatureNames())
        .isNew(isNew)
        .build();
  }

  public FeatureTableView toDTO() {
    return FeatureTableView.builder()
        .name(getName())
        .featureTableName(getFeatureTableName())
        .featureNames(getFeatureNames())
        .build();
  }
}
