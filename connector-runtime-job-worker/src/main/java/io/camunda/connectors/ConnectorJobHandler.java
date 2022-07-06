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
    // TODO: I think of using the normal Spring execution handling including failure handling
    // around retries, backoff, etc...
    // See https://github.com/camunda-community-hub/spring-zeebe/blob/075884c7b34ed18355bfdeb00233fa4b9f98d2fd/client/spring-zeebe/src/main/java/io/camunda/zeebe/spring/client/jobhandling/CommandWrapper.java#L13

    try {

      Object result = connectorFunction.execute(new ConnectorJobHandlerContext(job));
      client.newCompleteCommand(job).variables(result).send().join();
      LOGGER.debug("Completed job {}", job.getKey());

    } catch (Exception error) {

      LOGGER.debug("Failed to process job {}", job.getKey(), error);
      // Why no retries? 
      client.newFailCommand(job).retries(0).errorMessage(error.getMessage()).send().join();

    }
  }




}
