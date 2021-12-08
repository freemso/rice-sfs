package edu.rice.sfs.serving.controller;

import edu.rice.sfs.serving.service.ServingService;
import java.util.Collection;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class ServingController {

  private final ServingService servingService;

  public ServingController(ServingService servingService) {
    this.servingService = servingService;
  }

  @GetMapping("/getFeature")
  public Mono<Map<String, Object>> getFeature(
      @RequestParam String featureTableView,
      @RequestParam String entity
  ) {
    return servingService.getFeature(featureTableView, entity);
  }

  @GetMapping("/batchGetFeature")
  public Flux<Map<String, Object>> getFeature(
      @RequestParam String featureTableView,
      @RequestParam Collection<String> entities
  ) {
    return servingService.batchGetFeature(featureTableView, entities);
  }
}
