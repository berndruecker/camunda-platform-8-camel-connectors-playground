package io.camunda.connectors;

import io.camunda.connector.sdk.ConnectorFunction;

import java.util.Arrays;

public class ConnectorConfig {

    private String connectorName;
    private String taskType;
    private String[] variablesToFetch;
    private ConnectorFunction connectorFunction;

    public ConnectorConfig(String connectorName, String taskType, String[] variablesToFetch, ConnectorFunction connectorFunction) {
        this.connectorName = connectorName;
        this.taskType = taskType;
        this.variablesToFetch = variablesToFetch;
        this.connectorFunction = connectorFunction;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public String getTaskType() {
        return taskType;
    }

    public String[] getVariablesToFetch() {
        return variablesToFetch;
    }

    public ConnectorFunction getConnectorFunction() {
        return connectorFunction;
    }

    @Override
    public String toString() {
        return "ConnectorConfig{" +
                "connectorName='" + connectorName + '\'' +
                ", taskType='" + taskType + '\'' +
                ", variablesToFetch=" + Arrays.toString(variablesToFetch) +
                ", connectorFunction=" + connectorFunction +
                '}';
    }
}
