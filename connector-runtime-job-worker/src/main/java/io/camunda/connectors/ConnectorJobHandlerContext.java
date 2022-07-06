package io.camunda.connectors;

import io.camunda.connector.sdk.ConnectorContext;
import io.camunda.connector.sdk.SecretProvider;
import io.camunda.connector.sdk.SecretStore;
import io.camunda.zeebe.client.api.response.ActivatedJob;

import java.util.Map;

public class ConnectorJobHandlerContext implements ConnectorContext {

    private final ActivatedJob job;
    private SecretProvider secretProvider;

    public ConnectorJobHandlerContext(ActivatedJob job, SecretProvider secretProvider) {
        this.job = job;
        this.secretProvider = secretProvider;
    }

    @Override
    public SecretStore getSecretStore() {
        return new SecretStore(secretProvider);
    }

    @Override
    public <T extends Object> T getVariablesAsType(Class<T> cls) {
        return job.getVariablesAsType(cls);
    }


    @Override
    public Map<String, Object> getVariablesAsMap() {
        return job.getVariablesAsMap();
    }

    @Override
    public String getVariables() {
        return job.getVariables();
    }

}