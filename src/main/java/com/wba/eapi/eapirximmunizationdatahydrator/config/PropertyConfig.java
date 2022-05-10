package com.wba.eapi.eapirximmunizationdatahydrator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * The type Property config.
 */
@RefreshScope
@Configuration
public class PropertyConfig {

    @NotNull
    @Value("${log.logentry}")
    private String logEntry;
    @NotNull
    @Value("${log.organization}")
    private String organization;
    @NotNull
    @Value("${log.environment}")
    private String environment;
    @NotNull
    @Value("${log.microservice}")
    private String microservice;
    @NotNull
    @Value("${log.revision}")
    private String revision;
    @NotNull
    @Value("${log.separator}")
    private String separator;
    @NotNull
    @Value("${log.time.format}")
    private String timeformat;
    @NotNull
    @Value("${log.applicationId}")
    private String applicationId;
    @NotNull
    @Value("${log.output}")
    private String output;
    @NotNull
    @Value("${log.action}")
    private String action;

    public String getLogEntry() {
        return logEntry;
    }

    public String getOrganization() {
        return organization;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getMicroservice() {
        return microservice;
    }

    public String getRevision() {
        return revision;
    }

    public String getSeparator() {
        return separator;
    }

    public String getTimeformat() {
        return timeformat;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getOutput() {
        return output;
    }

    public String getAction() {
        return action;
    }
}
