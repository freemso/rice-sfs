package edu.rice.sfs.serving.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

@Configuration
public class DynamoDBConfig {

  @Value("${amazon.dynamodb.endpoint}")
  private String amazonDynamoDBEndpoint;

  @Value("${amazon.aws.access-key}")
  private String amazonAWSAccessKey;

  @Value("${amazon.aws.secret-key}")
  private String amazonAWSSecretKey;

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());

    if (!ObjectUtils.isEmpty(amazonDynamoDBEndpoint)) {
      amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
    }

    return amazonDynamoDB;
  }

  @Bean
  public DynamoDB dynamoDB(AmazonDynamoDB client) {
    return new DynamoDB(client);
  }

  @Bean
  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
  }
}
