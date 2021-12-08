package edu.rice.sfs.serving.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.common.base.Joiner;
import java.util.Collection;
import java.util.Map;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServingService {

  private final DynamoDB dynamoDB;
  private final FeatureServingViewService featureServingViewService;

  public ServingService(DynamoDB dynamoDB, FeatureServingViewService featureServingViewService) {
    this.dynamoDB = dynamoDB;
    this.featureServingViewService = featureServingViewService;
  }

  public Mono<Map<String, Object>> getFeature(String featureTableViewName, String entity) {
    return featureServingViewService.getFeatureServingViewWithCache(featureTableViewName)
        .mapNotNull(featureServingView -> dynamoDB.getTable(featureServingView.getDynamoTableName())
            .getItem(featureServingView.getEntityCol(),
                entity, Joiner.on(",").join(featureServingView.getFeatureCols()), null))
        .map(Item::asMap);
  }

  public Flux<Map<String, Object>> batchGetFeature(String featureTableViewName, Collection<String> entities) {
    return Flux.fromIterable(entities).flatMap(e -> getFeature(featureTableViewName, e));
  }
}
