package io.camunda.connectors;

import io.camunda.connector.sdk.SecretProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultSpringSecretProvider implements SecretProvider {

    @Value("#{${zeebe.connector.secrets:null}}")
    private Map<String, String> configuredSecrets;

    @Override
    public String getSecret(String value) {
        if (configuredSecrets!=null && configuredSecrets.containsKey(value)) {
            return configuredSecrets.get(value);
        } else {
            // Fallback to environment variables
            return System.getenv(value);
        }
    }
}
