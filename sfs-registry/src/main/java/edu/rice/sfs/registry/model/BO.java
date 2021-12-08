package edu.rice.sfs.registry.model;

public interface BO<DTO, PO> {

  DTO toDTO();

  PO toPO(boolean isNew);
}
