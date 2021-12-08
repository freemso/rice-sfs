package edu.rice.sfs.registry.model;

import edu.rice.sfs.common.model.Entity;
import edu.rice.sfs.registry.database.po.EntityPO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityBO implements BO<Entity, EntityPO> {

  private String name;
  private String description;

  public EntityPO toPO(boolean isNew) {
    return EntityPO.builder()
        .name(getName())
        .description(getDescription())
        .isNew(isNew)
        .build();
  }

  public Entity toDTO() {
    return Entity.builder()
        .name(getName())
        .description(getDescription())
        .build();
  }
}
