package io.camunda.connectors;

import io.camunda.connector.sdk.ConnectorFunction;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectorJobHandler implements JobHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorJobHandler.class);

  private final ConnectorFunction connectorFunction;

  public ConnectorJobHandler(ConnectorFunction connectorFunction) {
    this.connectorFunction = connectorFunction;
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) {
    LOGGER.debug("Received job {}", job.getKey());

    try {

      Object result = connectorFunction.execute(new ConnectorJobHandlerContext(job));
      client.newCompleteCommand(job).variables(result).send().join();
      LOGGER.debug("Completed job {}", job.getKey());

    } catch (Exception error) {

      LOGGER.debug("Failed to process job {}", job.getKey(), error);
      client.newFailCommand(job).retries(0).errorMessage(error.getMessage()).send().join();

    }
  }




}
