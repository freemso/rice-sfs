package edu.rice.sfs.serving.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("external.service")
@SuppressWarnings("initialization.fields.uninitialized")
public class ExternalServiceProperties {

  private String registryServiceEndpoint;
}
