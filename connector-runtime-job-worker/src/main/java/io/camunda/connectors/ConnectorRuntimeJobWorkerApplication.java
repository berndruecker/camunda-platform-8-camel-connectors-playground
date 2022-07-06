package io.camunda.connectors;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableZeebeClient
public class ConnectorRuntimeJobWorkerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ConnectorRuntimeJobWorkerApplication.class, args);
        context.getBean(ConnectorRegistry.class).startJobWorkers();
    }
}
