package edu.rice.sfs.common.client;

import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RequestHelper {

  public static RetryTemplate retryTemplate() {
    return retryTemplate(5, 10);
  }

  public static RetryTemplate retryTemplate(int maxAttempts, int backoffPeriod) {
    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(maxAttempts);

    FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(backoffPeriod);

    RetryTemplate template = new RetryTemplate();
    template.setRetryPolicy(retryPolicy);
    template.setBackOffPolicy(backOffPolicy);

    return template;
  }

}
