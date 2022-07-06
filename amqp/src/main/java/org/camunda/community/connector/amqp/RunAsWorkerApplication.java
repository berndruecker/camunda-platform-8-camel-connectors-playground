package org.camunda.community.connector.amqp;

import io.camunda.connectors.ConnectorRuntimeJobWorkerApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
//@Configuration(class=ConnectorRuntimeJobWorkerApplication.class)
public class RunAsWorkerApplication extends ConnectorRuntimeJobWorkerApplication {
}
