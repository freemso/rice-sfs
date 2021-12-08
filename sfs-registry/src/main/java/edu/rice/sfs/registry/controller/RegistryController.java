package edu.rice.sfs.registry.controller;

import edu.rice.sfs.common.model.Entity;
import edu.rice.sfs.common.model.Feature;
import edu.rice.sfs.common.model.FeatureTable;
import edu.rice.sfs.common.model.FeatureTableView;
import edu.rice.sfs.registry.model.EntityBO;
import edu.rice.sfs.registry.model.FeatureBO;
import edu.rice.sfs.registry.model.FeatureTableBO;
import edu.rice.sfs.registry.model.FeatureTableViewBO;
import edu.rice.sfs.registry.request.CreateOrUpdateEntityRequest;
import edu.rice.sfs.registry.request.CreateOrUpdateFeatureRequest;
import edu.rice.sfs.registry.request.CreateOrUpdateFeatureTableRequest;
import edu.rice.sfs.registry.request.CreateOrUpdateFeatureTableViewRequest;
import edu.rice.sfs.registry.service.RegistryService;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class RegistryController {

  private final RegistryService registryService;

  public RegistryController(RegistryService registryService) {
    this.registryService = registryService;
  }

  // ============ Entity ==============

  @PostMapping("/entities")
  public Entity createEntity(@RequestBody CreateOrUpdateEntityRequest request) {
    return registryService.createEntity(request).toDTO();
  }

  @GetMapping("/entities")
  public Collection<Entity> batchGetEntities(
      @RequestParam(required = false) Collection<String> names) {
    var entities = ObjectUtils.isEmpty(names)
        ? registryService.listAllEntities()
        : registryService.getEntities(names);
    return entities.stream().map(EntityBO::toDTO).collect(Collectors.toList());
  }

  @GetMapping("/entity/{name}")
  public Entity getEntity(@PathVariable String name) {
    return registryService.getEntity(name).toDTO();
  }

  @PutMapping("/entity/{name}")
  public Entity updateEntity(
      @PathVariable String name,
      @RequestBody CreateOrUpdateEntityRequest request) {
    Assert.isTrue(Objects.equals(name, request.getName()), "Entity name should match");
    return registryService.updateEntity(request).toDTO();
  }

  // ============ Feature ==============

  @PostMapping("/features")
  public Feature createFeature(@RequestBody CreateOrUpdateFeatureRequest request) {
    return registryService.createFeature(request).toDTO();
  }

  @GetMapping("/features")
  public Collection<Feature> batchGetFeatures(
      @RequestParam(required = false) Collection<String> names) {
    var features = ObjectUtils.isEmpty(names)
        ? registryService.listAllFeatures()
        : registryService.getFeatures(names);
    return features.stream().map(FeatureBO::toDTO).collect(Collectors.toList());
  }

  @GetMapping("/feature/{name}")
  public Feature getFeature(@PathVariable String name) {
    return registryService.getFeature(name).toDTO();
  }

  @PutMapping("/feature/{name}")
  public Feature updateFeature(
      @PathVariable String name,
      @RequestBody CreateOrUpdateFeatureRequest request) {
    Assert.isTrue(Objects.equals(name, request.getName()), "Feature name should match");
    return registryService.updateFeature(request).toDTO();
  }

  // ============ FeatureTable ==============

  @PostMapping("/featureTables")
  public FeatureTable createFeatureTable(@RequestBody CreateOrUpdateFeatureTableRequest request)
      throws InterruptedException {
    return registryService.createFeatureTable(request).toDTO();
  }

  @GetMapping("/featureTables")
  public Collection<FeatureTable> batchGetFeatureTables(
      @RequestParam(required = false) Collection<String> names) {
    var featureTables = ObjectUtils.isEmpty(names)
        ? registryService.listAllFeatureTables()
        : registryService.getFeatureTables(names);
    return featureTables.stream().map(FeatureTableBO::toDTO).collect(Collectors.toList());
  }

  @GetMapping("/featureTable/{name}")
  public FeatureTable getFeatureTable(@PathVariable String name) {
    return registryService.getFeatureTable(name).toDTO();
  }

  @PutMapping("/featureTable/{name}")
  public FeatureTable updateFeatureTable(
      @PathVariable String name,
      @RequestBody CreateOrUpdateFeatureTableRequest request) {
    Assert.isTrue(Objects.equals(name, request.getName()), "FeatureTable name should match");
    return registryService.updateFeatureTable(request).toDTO();
  }

  @PostMapping("/featureTable/{featureTableName}/feature/{featureName}")
  public FeatureTable addFeatureToTable(
      @PathVariable String featureTableName,
      @PathVariable String featureName) {
    return registryService.addFeatureToTable(featureTableName, featureName).toDTO();
  }

  @DeleteMapping("/featureTable/{featureTableName}/feature/{featureName}")
  public FeatureTable removeFeatureFromTable(
      @PathVariable String featureTableName,
      @PathVariable String featureName) {
    return registryService.removeFeatureFromTable(featureTableName, featureName).toDTO();
  }

  // ============ FeatureTableView ==============

  @PostMapping("/featureTableViews")
  public FeatureTableView createFeatureTableView(
      @RequestBody CreateOrUpdateFeatureTableViewRequest request) {
    return registryService.createFeatureTableView(request).toDTO();
  }

  @GetMapping("/featureTableViews")
  public Collection<FeatureTableView> batchGetAllFeatureTableViews() {
    return registryService.listAllFeatureTableViews().stream()
        .map(FeatureTableViewBO::toDTO).collect(Collectors.toList());
  }

  @GetMapping("/featureTableView/{name}")
  public FeatureTableView getFeatureTableView(@PathVariable String name) {
    return registryService.getFeatureTableView(name).toDTO();
  }

  @DeleteMapping("/featureTableView/{name}")
  public void deleteFeatureTableView(@PathVariable String name) {
    registryService.deleteFeatureTableView(name);
  }
}
