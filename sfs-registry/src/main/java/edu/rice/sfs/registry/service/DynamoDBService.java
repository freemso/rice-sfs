package edu.rice.sfs.registry.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import edu.rice.sfs.registry.model.FeatureTableBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DynamoDBService {

  private final DynamoDB client;

  public DynamoDBService(DynamoDB client) {
    this.client = client;
  }

  public void createFeatureTable(FeatureTableBO featureTable) throws InterruptedException {
    var createTableRequest = new CreateTableRequest()
        .withTableName(featureTable.getDynamoTableName())
        .withKeySchema(new KeySchemaElement(featureTable.getEntityName(), KeyType.HASH))
        .withAttributeDefinitions(
            new AttributeDefinition(featureTable.getEntityName(), ScalarAttributeType.S))
        .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L));
    var table = client.createTable(createTableRequest);
    try {
      log.info("Attempting to create table; please wait...");
      table.waitForActive();
      log.info("Success.  Table status: " + table.getDescription().getTableStatus());
    } catch (Exception e) {
      log.error("Error when creating table {}.", featureTable, e);
      throw e;
    }
  }
}
