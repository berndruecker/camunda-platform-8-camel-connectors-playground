
```bash
java -cp "C:\DEV\Camunda\camunda-platform-8-camel-connectors-playground\amqp\target\amqp-connector-0.0.1-SNAPSHOT.jar;C:\DEV\Camunda\connector-framework\connector-runtime-job-worker\target\connector-runtime-job-worker-0.0.1-SNAPSHOT.jar" -DZEEBE_CONNECTOR_CAMELAMQP_TYPE="io.camunda.community:amqp:1" -DZEEBE_CONNECTOR_CAMELAMQP_VARIABLES="destinationName,destinationType,username,password" -DZEEBE_CONNECTOR_CAMELAMQP_FUNCTION="org.camunda.community.connector.amqp.AmqpOutboundConnectorFunction" io.camunda.connector.runtime.jobworker.Main     
```


```bash
docker run --name zeebe -p 26500-26502:26500-26502 camunda/zeebe:latest
docker run -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```