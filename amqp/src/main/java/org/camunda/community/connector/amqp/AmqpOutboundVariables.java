package org.camunda.community.connector.amqp;

import io.camunda.connector.sdk.SecretStore;

public class AmqpOutboundVariables {

    private String amqpUrl;
    private String destinationName;
    private String destinationType;
    private String username;
    private String password;

    public void replaceSecrets(SecretStore secretStore) {
        username = secretStore.replaceSecret(username);
        password = secretStore.replaceSecret(password);
    }
    public String getEndpointUri() {
        return "amqp:" + getDestinationType() + ":" + getDestinationName() + "?disableReplyTo=true";
        //+ "?username=" + getUsername() + "&password=RAW(" + getPassword() + ")";
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getAmqpUrl() {
        return amqpUrl;
    }

    public void setAmqpUrl(String amqpUrl) {
        this.amqpUrl = amqpUrl;
    }
}
