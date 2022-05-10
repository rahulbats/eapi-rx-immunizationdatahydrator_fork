package com.wba.eapi.eapirximmunizationdatahydrator.exception;

import com.wba.eapi.eapirximmunizationdatahydrator.constant.Constants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate;

/**
 * The type Wba deserialization exception handler.
 * An Extension of LogAndContinueExceptionHandler to make the logging
 * compatible with WBA logging standard.
 * Handles deserialization exceptions that happens outside application logic.
 */
@Component
public class WBADeserializationExceptionHandler implements DeserializationExceptionHandler {

    /**
     * The Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializationExceptionHandler.class);


    /**
     * Instantiates a new Wba deserialization exception handler.
     */
    public WBADeserializationExceptionHandler() {
    }

    @Override
    public DeserializationHandlerResponse handle(ProcessorContext processorContext, ConsumerRecord<byte[], byte[]> consumerRecord, Exception e) {

        String logMessage = "LogEntry##ERROR##" + getCurrentDate() + "##wba##Pharmacy##eapi-rx-immunizationdatahydrator##v1_0_0##null##null##eapirximmunizationdatahydrator"
                + "##" + consumerRecord.topic() + "##" + Constants.DATA_VALIDATION_ERROR + "##";
        StringBuilder message = new StringBuilder().append(logMessage).append("##").append("Exception caught during converting kafka message to java object for offset:" + consumerRecord.offset() +
                " in Partition: " + consumerRecord.partition() + " of topic: " + consumerRecord.topic());
        LOGGER.error(message.toString());
        return DeserializationHandlerResponse.CONTINUE;
    }

    public void configure(Map<String, ?> map) {

    }
}
