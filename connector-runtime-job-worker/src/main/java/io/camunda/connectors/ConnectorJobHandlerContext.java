package io.camunda.connectors;

import io.camunda.connector.sdk.ConnectorContext;
import io.camunda.connector.sdk.SecretProvider;
import io.camunda.connector.sdk.SecretStore;
import io.camunda.zeebe.client.api.response.ActivatedJob;

import java.util.ServiceLoader;

class ConnectorJobHandlerContext implements ConnectorContext {

    private final ActivatedJob job;

    public ConnectorJobHandlerContext(ActivatedJob job) {
        this.job = job;
    }

    @Override
    public SecretStore getSecretStore() {
        return new SecretStore(getSecretProvider());
    }

    @Override
    public <T extends Object> T getVariablesAsType(Class<T> cls) {
        return job.getVariablesAsType(cls);
    }

    @Override
    public String getVariables() {
        return job.getVariables();
    }

    protected SecretProvider getSecretProvider() {
        return ServiceLoader.load(SecretProvider.class).findFirst().orElse(getEnvSecretProvider());
    }

    protected SecretProvider getEnvSecretProvider() {
        return new SecretProvider() {
            @Override
            public String getSecret(String value) {
                return System.getenv(value);
            }
        };
    }
}