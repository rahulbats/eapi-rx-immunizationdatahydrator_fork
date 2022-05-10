package com.wba.eapi.eapirximmunizationdatahydrator.common;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import static com.wba.eapi.eapirximmunizationdatahydrator.constant.Constants.*;

@RefreshScope
@Component
public class LoggingUtils {
    private static final Logger LOGGER = ESAPI.getLogger(LoggingUtils.class);

    public String appDetails() {
        return ("##" + ORGANIZATION + "##" + ENV + "##" + MICROSERVICE + "##" + REVISION);
    }

    public void info(String msg, String correlationId, String transactionId, long startTime) {
        String statusMsg = "##" + "##" + "##" + "##" + msg;
        LOGGER.info(Logger.EVENT_SUCCESS, "LogEntry##INFO####EAPI-INFO" + appDetails() + "##" + transactionId + "##"
                + correlationId + "##" + APP_V + "##" + statusMsg + "##" + (System.currentTimeMillis() - startTime) + "ms - ## Time : " + startTime);
    }

    public void debug(String msg, String correlationId, String transactionId, long startTime) {
        String statusMsg = "##" + "##" + "##" + "##" + msg;
        LOGGER.debug(Logger.EVENT_SUCCESS, "LogEntry##DEBUG####EAPI-DEBUG" + appDetails() + "##" + transactionId + "##"
                + correlationId + "##" + APP_V + "##" + statusMsg + "##" + (System.currentTimeMillis() - startTime) + "ms - ## Time: " + startTime);
    }

    public void error(String msg, String correlationId, String transactionId, long startTime) {
        String statusMsg = "##" + "##" + "##" + "##" + msg;
        LOGGER.error(Logger.EVENT_SUCCESS, "LogEntry##ERROR####EAPI-ERROR" + appDetails() + "##" + transactionId + "##"
                + correlationId + "##" + APP_V + "##" + statusMsg + "##" + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void infoKFKFlow(String msg, String correlationId, String transactionId, long startTime) {
        String statusMsg = "##" + "##" + "##" + "##" + msg;
        LOGGER.info(Logger.EVENT_SUCCESS, "LogEntry##INFO####KFK-INFO" + appDetails() + "##" + transactionId + "##" +
                correlationId + "##" + APP_V + "##" + statusMsg + "##" + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void debugKFKFlow(String msg, String correlationId, String transactionId, long startTime) {
        String statusMsg = "##" + "##" + "##" + "##" + msg;
        LOGGER.debug(Logger.EVENT_SUCCESS, "LogEntry##DEBUG####KFK-DEBUG" + appDetails() + "##" + transactionId + "##" +
                correlationId + "##" + APP_V + "##" + statusMsg + "##" + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void errorKFKFlow(String msg, String correlationId, String transactionId, long startTime) {
        String statusMsg = "##" + "##" + "##" + "##" + msg;
        LOGGER.error(Logger.EVENT_FAILURE, "LogEntry##ERROR####KFK-ERROR" + appDetails() + "##" + transactionId + "##" +
                correlationId + "##" + APP_V + "##" + statusMsg + "##" + (System.currentTimeMillis() - startTime) + "ms");
    }
}