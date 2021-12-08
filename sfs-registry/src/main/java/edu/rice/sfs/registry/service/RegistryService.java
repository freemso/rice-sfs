package edu.rice.sfs.registry.service;

import edu.rice.sfs.registry.database.po.EntityPO;
import edu.rice.sfs.registry.database.po.FeaturePO;
import edu.rice.sfs.registry.database.po.FeatureTablePO;
import edu.rice.sfs.registry.database.po.FeatureTableViewPO;
import edu.rice.sfs.registry.database.repo.EntityRepo;
import edu.rice.sfs.registry.database.repo.FeatureRepo;
import edu.rice.sfs.registry.database.repo.FeatureTableRepo;
import edu.rice.sfs.registry.database.repo.FeatureTableViewRepo;
import edu.rice.sfs.registry.exception.AlreadyExistException;
import edu.rice.sfs.registry.exception.NotFoundException;
import edu.rice.sfs.registry.model.EntityBO;
import edu.rice.sfs.registry.model.FeatureBO;
import edu.rice.sfs.registry.model.FeatureTableBO;
import edu.rice.sfs.registry.model.FeatureTableViewBO;
import edu.rice.sfs.registry.request.CreateOrUpdateEntityRequest;
import edu.rice.sfs.registry.request.CreateOrUpdateFeatureRequest;
import edu.rice.sfs.registry.request.CreateOrUpdateFeatureTableRequest;
import edu.rice.sfs.registry.request.CreateOrUpdateFeatureTableViewRequest;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class RegistryService {

  private final EntityRepo entityRepo;
  private final FeatureRepo featureRepo;
  private final FeatureTableRepo featureTableRepo;
  private final FeatureTableViewRepo featureTableViewRepo;

  private final DynamoDBService dynamoDBService;

  public RegistryService(
      EntityRepo entityRepo,
      FeatureRepo featureRepo,
      FeatureTableRepo featureTableRepo,
      FeatureTableViewRepo featureTableViewRepo,
      DynamoDBService dynamoDBService) {
    this.entityRepo = entityRepo;
    this.featureRepo = featureRepo;
    this.featureTableRepo = featureTableRepo;
    this.featureTableViewRepo = featureTableViewRepo;
    this.dynamoDBService = dynamoDBService;
  }

  // ============ Entity ==============

  public EntityBO createEntity(CreateOrUpdateEntityRequest request) {
    if (entityRepo.existsById(request.getName())) {
      throw AlreadyExistException.entityAlreadyExists(request.getName());
    }
    var bo = EntityBO.builder()
        .name(request.getName())
        .description(request.getDescription())
        .build();
    return convertToBO(entityRepo.save(bo.toPO(true)));
  }

  public Collection<EntityBO> listAllEntities() {
    return StreamSupport.stream(entityRepo.findAll().spliterator(), false)
        .map(this::convertToBO)
        .collect(Collectors.toList());
  }

  public Collection<EntityBO> getEntities(Iterable<String> names) {
    return StreamSupport.stream(entityRepo.findAllById(names).spliterator(), false)
        .map(this::convertToBO)
        .collect(Collectors.toList());
  }

  public EntityBO getEntity(String name) {
    return entityRepo.findById(name).map(this::convertToBO)
        .orElseThrow(() -> NotFoundException.entityNotFound(name));
  }

  public EntityBO updateEntity(CreateOrUpdateEntityRequest request) {
    return entityRepo.findById(request.getName()).map(this::convertToBO)
        .map(existed -> {
          Optional.ofNullable(request.getDescription())
              .ifPresent(existed::setDescription);
          entityRepo.save(existed.toPO(false));
          return existed;
        })
        .orElseThrow(() -> NotFoundException.entityNotFound(request.getName()));
  }

  private EntityBO convertToBO(EntityPO po) {
    return EntityBO.builder()
        .name(po.getName())
        .description(po.getDescription())
        .build();
  }

  // ============ Feature ==============

  public FeatureBO createFeature(CreateOrUpdateFeatureRequest request) {
    if (featureRepo.existsById(request.getName())) {
      throw AlreadyExistException.featureAlreadyExists(request.getName());
    }
    var bo = FeatureBO.builder()
        .name(request.getName())
        .description(request.getDescription())
        .valueType(request.getValueType())
        .deltaTableColName(request.getDeltaTableColName())
        .dynamoTableColName(request.getDynamoTableColName())
        .build();
    return convertToBO(featureRepo.save(bo.toPO(true)));
  }

  public Collection<FeatureBO> listAllFeatures() {
    return StreamSupport.stream(featureRepo.findAll().spliterator(), false)
        .map(this::convertToBO)
        .collect(Collectors.toList());
  }

  public Collection<FeatureBO> getFeatures(Iterable<String> names) {
    return StreamSupport.stream(featureRepo.findAllById(names).spliterator(), false)
        .map(this::convertToBO)
        .collect(Collectors.toList());
  }

  public FeatureBO getFeature(String name) {
    return featureRepo.findById(name).map(this::convertToBO)
        .orElseThrow(() -> NotFoundException.featureNotFound(name));
  }

  public FeatureBO updateFeature(CreateOrUpdateFeatureRequest request) {
    return featureRepo.findById(request.getName()).map(this::convertToBO)
        .map(existed -> {
          Optional.ofNullable(request.getDescription())
              .ifPresent(existed::setDescription);
          Optional.ofNullable(request.getDeltaTableColName())
              .ifPresent(existed::setDeltaTableColName);
          Optional.ofNullable(request.getDynamoTableColName())
              .ifPresent(existed::setDynamoTableColName);
          featureRepo.save(existed.toPO(false));
          return existed;
        })
        .orElseThrow(() -> NotFoundException.featureNotFound(request.getName()));
  }

  private FeatureBO convertToBO(FeaturePO po) {
    return FeatureBO.builder()
        .name(po.getName())
        .description(po.getDescription())
        .valueType(po.getValueType())
        .deltaTableColName(po.getDeltaTableColName())
        .dynamoTableColName(po.getDynamoTableColName())
        .build();
  }

  // ============ FeatureTable ==============

  public FeatureTableBO createFeatureTable(CreateOrUpdateFeatureTableRequest request)
      throws InterruptedException {
    if (featureTableRepo.existsById(request.getName())) {
      throw AlreadyExistException.featureTableAlreadyExists(request.getName());
    }
    // at least one entity
    Assert.state(!ObjectUtils.isEmpty(request.getEntity()), "entity must not be null");

    var bo = FeatureTableBO.builder()
        .name(request.getName())
        .description(request.getDescription())
        .deltaTablePath(request.getDeltaTablePath())
        .dynamoTableName(request.getDynamoTableName())
        .build();
    // set entity
    bo.setEntity(getEntity(request.getEntity()));
    // add feature
    request.getFeatures().forEach(featureName -> bo.addFeature(getFeature(featureName)));
    dynamoDBService.createFeatureTable(bo);
    featureTableRepo.save(bo.toPO(true));
    return bo;
  }

  public Collection<FeatureTableBO> listAllFeatureTables() {
    return StreamSupport.stream(featureTableRepo.findAll().spliterator(), false)
        .map(this::convertToBO)
        .collect(Collectors.toList());
  }

  public Collection<FeatureTableBO> getFeatureTables(Iterable<String> names) {
    return StreamSupport.stream(featureTableRepo.findAllById(names).spliterator(), false)
        .map(this::convertToBO)
        .collect(Collectors.toList());
  }

  public FeatureTableBO getFeatureTable(String name) {
    return featureTableRepo.findById(name).map(this::convertToBO)
        .orElseThrow(() -> NotFoundException.featureTableNotFound(name));
  }

  public FeatureTableBO updateFeatureTable(CreateOrUpdateFeatureTableRequest request) {
    return featureTableRepo.findById(request.getName()).map(this::convertToBO)
        .map(existed -> {
          Optional.ofNullable(request.getDescription())
              .ifPresent(existed::setDescription);
          Optional.ofNullable(request.getDeltaTablePath())
              .ifPresent(existed::setDeltaTablePath);
          Optional.ofNullable(request.getDynamoTableName())
              .ifPresent(existed::setDynamoTableName);
          featureTableRepo.save(existed.toPO(false));
          return existed;
        })
        .orElseThrow(() -> NotFoundException.featureTableNotFound(request.getName()));
  }

  public FeatureTableBO addFeatureToTable(String featureTableName, String featureName) {
    return featureTableRepo.findById(featureTableName).map(this::convertToBO)
        .map(existed -> {
          FeatureBO feature = featureRepo.findById(featureName).map(this::convertToBO)
              .orElseThrow(() -> NotFoundException.featureNotFound(featureName));
          existed.addFeature(feature);
          featureTableRepo.save(existed.toPO(false));
          return existed;
        })
        .orElseThrow(() -> NotFoundException.featureTableNotFound(featureTableName));
  }

  public FeatureTableBO removeFeatureFromTable(String featureTableName, String featureName) {
    return featureTableRepo.findById(featureTableName).map(this::convertToBO)
        .map(existed -> {
          existed.removeFeature(featureName);
          featureTableRepo.save(existed.toPO(false));
          return existed;
        })
        .orElseThrow(() -> NotFoundException.featureTableNotFound(featureTableName));
  }

  private FeatureTableBO convertToBO(FeatureTablePO po) {
    var bo = FeatureTableBO.builder()
        .name(po.getName())
        .description(po.getDescription())
        .deltaTablePath(po.getDeltaTablePath())
        .dynamoTableName(po.getDynamoTableName())
        .build();
    // add entity
    bo.setEntity(getEntity(po.getEntity()));
    // add feature
    po.getFeatures().forEach(featureName -> bo.addFeature(getFeature(featureName)));
    return bo;
  }

  // ============ FeatureTableView ==============

  public FeatureTableViewBO createFeatureTableView(CreateOrUpdateFeatureTableViewRequest request) {
    if (featureTableViewRepo.existsById(request.getName())) {
      throw AlreadyExistException.featureTableViewAlreadyExists(request.getName());
    }
    var bo = FeatureTableViewBO.builder()
        .name(request.getName())
        .featureTableName(request.getFeatureTableName())
        .featureNames(request.getFeatureNames())
        .build();
    return convertToBO(featureTableViewRepo.save(bo.toPO(true)));
  }

  public Collection<FeatureTableViewBO> listAllFeatureTableViews() {
    return StreamSupport.stream(featureTableViewRepo.findAll().spliterator(), false)
        .map(this::convertToBO)
        .collect(Collectors.toList());
  }

  public FeatureTableViewBO getFeatureTableView(String name) {
    return featureTableViewRepo.findById(name).map(this::convertToBO)
        .orElseThrow(() -> NotFoundException.featureTableViewNotFound(name));
  }

  public void deleteFeatureTableView(String name) {
    featureTableViewRepo.deleteById(name);
  }

  private FeatureTableViewBO convertToBO(FeatureTableViewPO po) {
    return FeatureTableViewBO.builder()
        .name(po.getName())
        .featureTableName(po.getFeatureTableName())
        .featureNames(po.getFeatureNames())
        .build();
  }
}
