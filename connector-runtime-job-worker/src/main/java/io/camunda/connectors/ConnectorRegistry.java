package io.camunda.connectors;

import io.camunda.connector.sdk.ConnectorFunction;
import io.camunda.connector.sdk.ZeebeConnector;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

@Component
public class ConnectorRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorRegistry.class);

    private List<ConnectorConfig> connectors = new ArrayList<ConnectorConfig>();
    private List<JobWorker> openedWorkers = new ArrayList<>();

    @Autowired
    private ZeebeClient zeebeClient;

    @PostConstruct
    private void scanForConnectors() {
        try {
            // Way 1 of fingint things: Classpath scanning. Slow if we do it on root level, but probably OK if we limit the packages (or make them configurable)
            /*
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(ZeebeConnector.class));
            provider.addIncludeFilter(new AssignableTypeFilter(ConnectorFunction.class));
            Set<BeanDefinition> beanDefs = provider.findCandidateComponents("");

            for (BeanDefinition bean : beanDefs) {
                String className = bean.getBeanClassName();
                Class<? extends ConnectorFunction> connectorFunctionClass = (Class<? extends ConnectorFunction>) ClassUtils.forName(className, this.getClass().getClassLoader());
                */

            // Way 2: Use a service loader (if all connectors have the service configured)
            ServiceLoader.load(ConnectorFunction.class).stream().forEach( connectorFunction -> {
                Class<? extends ConnectorFunction> connectorFunctionClass = connectorFunction.type();

                // Same from here on:
                ZeebeConnector zeebeConnectorAnnotation =  connectorFunctionClass.getAnnotation(ZeebeConnector.class);
                connectors.add(new ConnectorConfig(
                        zeebeConnectorAnnotation.name(),
                        zeebeConnectorAnnotation.taskType(),
                        zeebeConnectorAnnotation.variablesToFetch(),
                        /* Way1:
                        connectorFunctionClass.getDeclaredConstructor().newInstance()));
                        */
                        // Way 2:
                        connectorFunction.get()));

            });
        } catch (Exception ex) {
            throw new RuntimeException("Could not initialize connectors: " + ex.getMessage(), ex);
        }
    }

    /**
     * Only call start after Spring context is initialized to have ZeebeClient initialized properly, this is why this is not done on @PostContruct
     */
    public void startJobWorkers() {
        connectors.forEach(
                connectorConfig -> {
                    LOGGER.info("Starting worker for connector: " + connectorConfig);
                    JobWorker jobWorker = zeebeClient
                            .newWorker()
                            .jobType(connectorConfig.getTaskType())
                            .handler(new ConnectorJobHandler(connectorConfig.getConnectorFunction()))
                            .timeout(Duration.ofSeconds(10))
                            .name(connectorConfig.getConnectorName())
                            .fetchVariables(connectorConfig.getVariablesToFetch())
                            .open();
                    openedWorkers.add(jobWorker);
                });
    }

    @PreDestroy
    public void stopJobWorkers() {
        try {
            openedWorkers.forEach(worker -> worker.close());
            openedWorkers = new ArrayList<>();
        } catch (Exception ex) {
            // ignore
        }
    }
}
