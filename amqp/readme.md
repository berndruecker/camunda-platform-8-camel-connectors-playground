
To use AMQP with RabbitMQ, you need to enable the AMQP Plugin:

Dockerfile:
```
FROM rabbitmq:3-management
RUN echo '[rabbitmq_management,rabbitmq_management_visualiser,rabbitmq_amqp1_0].' > enabled_plugins
RUN rabbitmq-plugins enable rabbitmq_amqp1_0
```


```bash
docker run --name zeebe -p 26500-26502:26500-26502 camunda/zeebe:latest
docker run -p 15672:15672 -p 5672:5672 >>>YOUROWNIMAGE<<<
```

Now you can see RabbitMQ:
* http://localhost:15672/#/queues/
* User: guest
* Password: guest



OLD STUFF:

```bash
java -cp "C:\DEV\Camunda\camunda-platform-8-camel-connectors-playground\amqp\target\amqp-connector-0.0.1-SNAPSHOT.jar;C:\DEV\Camunda\connector-framework\connector-runtime-job-worker\target\connector-runtime-job-worker-0.0.1-SNAPSHOT.jar" -DZEEBE_CONNECTOR_CAMELAMQP_TYPE="io.camunda.community:amqp:1" -DZEEBE_CONNECTOR_CAMELAMQP_VARIABLES="destinationName,destinationType,username,password" -DZEEBE_CONNECTOR_CAMELAMQP_FUNCTION="org.camunda.community.connector.amqp.AmqpOutboundConnectorFunction" io.camunda.connector.runtime.jobworker.Main     
```
