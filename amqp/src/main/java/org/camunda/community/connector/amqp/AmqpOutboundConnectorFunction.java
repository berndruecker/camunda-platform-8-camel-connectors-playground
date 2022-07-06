package org.camunda.community.connector.amqp;

import io.camunda.connector.sdk.ConnectorContext;
import io.camunda.connector.sdk.ConnectorFailedException;
import io.camunda.connector.sdk.ConnectorFunction;
import io.camunda.connector.sdk.ZeebeConnector;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;

@ZeebeConnector(
        name = "amqp",
        taskType = "io.camunda.community:amqp:1",
        variablesToFetch = {"destinationName","destinationType","username","password"})
public class AmqpOutboundConnectorFunction implements ConnectorFunction {

    public Object execute(ConnectorContext connectorContext) {
        AmqpOutboundVariables variables = connectorContext.getVariablesAsType(AmqpOutboundVariables.class);
        variables.replaceSecrets(connectorContext.getSecretStore());

        CamelContext camelContext = new DefaultCamelContext();
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

        //AMQPComponent amqpComponent = AMQPComponent.amqpComponent(uri, username, password);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(connectorContext.getVariables());
        exchange.setPattern(ExchangePattern.InOut);
        Exchange send = producerTemplate.send(variables.getEndpointUri(), exchange);
        if (null != send.getException()){
            throw new ConnectorFailedException("Could not send AMQP message: " + send.getException().getMessage(),  send.getException());
        }

        return null;
    }

}
