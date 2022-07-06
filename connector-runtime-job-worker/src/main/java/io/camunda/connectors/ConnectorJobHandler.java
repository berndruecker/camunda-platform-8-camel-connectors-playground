package io.camunda.connectors;

import io.camunda.connector.sdk.ConnectorFunction;
import io.camunda.connector.sdk.SecretProvider;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectorJobHandler implements JobHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorJobHandler.class);

  private final ConnectorFunction connectorFunction;
  private final SecretProvider secretProvider;

  public ConnectorJobHandler(ConnectorFunction connectorFunction, SecretProvider secretProvider) {
    this.connectorFunction = connectorFunction;
    this.secretProvider = secretProvider;
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) {
    LOGGER.debug("Received job {}", job.getKey());
    // TODO: I think of using the normal Spring execution handling including failure handling
    // around retries, backoff, etc...
    // See https://github.com/camunda-community-hub/spring-zeebe/blob/075884c7b34ed18355bfdeb00233fa4b9f98d2fd/client/spring-zeebe/src/main/java/io/camunda/zeebe/spring/client/jobhandling/CommandWrapper.java#L13

    try {

      Object result = connectorFunction.execute(new ConnectorJobHandlerContext(job, secretProvider));
      client.newCompleteCommand(job).variables(result).send().join();
      LOGGER.info("Completed job {}", job.getKey());

    } catch (Exception error) {

      LOGGER.info("Failed to process job {}", job.getKey(), error);
      // Why no retries?
      client.newFailCommand(job).retries(0).errorMessage(error.getMessage()).send().join();

    }
  }




}
