package edu.rice.sfs.serving.service;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import edu.rice.sfs.common.model.Feature;
import edu.rice.sfs.common.model.FeatureTable;
import edu.rice.sfs.common.model.FeatureTableView;
import edu.rice.sfs.serving.config.ExternalServiceProperties;
import edu.rice.sfs.serving.model.FeatureServingView;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FeatureServingViewService {

  private final AsyncLoadingCache<String, FeatureServingView> cache;

  private final WebClient webClient;

  public FeatureServingViewService(ExternalServiceProperties externalServiceProperties) {
    this.webClient = WebClient.builder()
        .baseUrl(externalServiceProperties.getRegistryServiceEndpoint())
        .build();
    this.cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(2))
        .maximumSize(1000)
        .buildAsync(new AsyncCacheLoader<>() {
          @Override
          public @NonNull CompletableFuture<FeatureServingView> asyncLoad(
              @NonNull String featureTableViewName, @NonNull Executor executor) {
            return getFeatureServingView(featureTableViewName).toFuture();
          }
        });
  }

  public Mono<FeatureServingView> getFeatureServingViewWithCache(String featureTableViewName) {
    return Mono.fromFuture(cache.get(featureTableViewName));
  }

  public Mono<FeatureServingView> getFeatureServingView(String featureTableViewName) {
    return getFeatureTableView(featureTableViewName).flatMap(featureTableView ->
        getFeatureTable(featureTableView.getFeatureTableName()).map(featureTable ->
            FeatureServingView.builder()
                .name(featureTableViewName)
                .dynamoTableName(featureTable.getDynamoTableName())
                .entityCol(featureTable.getEntity().getName())
                .featureCols(featureTableView.getFeatureNames().stream()
                    .map(f -> getDynamoTableCol(featureTable, f))
                    .filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList()))
                .build()));
  }

  public Mono<FeatureTable> getFeatureTable(String featureTableName) {
    return webClient.get()
        .uri(String.format("/featureTable/%s", featureTableName))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {
        });
  }

  public Mono<FeatureTableView> getFeatureTableView(String featureTableViewName) {
    return webClient.get()
        .uri(String.format("/featureTableView/%s", featureTableViewName))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {
        });
  }

  private Optional<String> getDynamoTableCol(FeatureTable featureTable, String feature) {
    return featureTable.getFeatures().stream()
        .filter(f -> f.getName().equals(feature))
        .findAny().map(Feature::getDynamoTableColName);
  }
}
