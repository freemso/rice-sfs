package edu.rice.sfs.common.client;


import edu.rice.sfs.common.model.Feature;
import edu.rice.sfs.common.model.FeatureTable;
import java.io.Serializable;
import java.util.Collection;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RegistryServiceClient implements Serializable {

  private final RestTemplate restTemplate;
  private final String endpoint;

  public RegistryServiceClient(String endpoint) {
    this.restTemplate = new RestTemplate();
    this.endpoint = endpoint;
  }

  public FeatureTable getFeatureTable(String name) {
    return RequestHelper.retryTemplate().execute(context -> {
      String uri = UriComponentsBuilder.fromHttpUrl(endpoint)
          .path("/featureTable/")
          .path(name)
          .toUriString();
      return restTemplate.getForObject(uri, FeatureTable.class);
    });
  }

  public Feature[] batchGetFeature(Collection<String> names) {
    return RequestHelper.retryTemplate().execute(context -> {
      String uri = UriComponentsBuilder.fromHttpUrl(endpoint)
          .path("/features/")
          .queryParam("names", names)
          .toUriString();
      return restTemplate.getForEntity(uri, Feature[].class)
          .getBody();
    });
  }

}
