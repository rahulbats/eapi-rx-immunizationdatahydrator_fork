package com.wba.eapi.eapirximmunizationdatahydrator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wba.eapi.eapirximmunizationdatahydrator.common.LoggingUtils;
import com.wba.eapi.eapirximmunizationdatahydrator.constant.Constants;
import com.wba.eapi.eapirximmunizationdatahydrator.exception.InvalidDateException;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RefreshScope
@Configuration
public class RxImmunizationStreamer {

    @Value("${spring.cloud.stream.bindings.process-in-0.destination.rx-raw-topic}")
    public static String rxRawTopic="rx";
    @Value("${spring.cloud.stream.bindings.process-in-0.destination.fill-raw-topic}")
    public static String fillRawTopic="fill";
    @Value("${spring.cloud.stream.bindings.process-in-0.destination.drug-raw-topic}")
    private String drugRawTopic;
    @Value("${spring.cloud.stream.bindings.process-out-0.destination.rx-immunization-topic}")
    public static String rxImmuTopic="immu";
    @Value("${custom.enable.aggregatorlog}")
    private Boolean enableAgglog;
    @Value("${custom.batch.log.size}")
    private long batchLogSize;

    @Autowired
    public ObjectMapper mapper;
    @Autowired
    public LoggingUtils loggingUtil;

    private static final Logger log = LoggerFactory.getLogger(RxImmunizationStreamer.class);

    static long batchCounter = 0;

    long startTime = 0;

    public String SCHEMA_REGISTRY_URL="";
    @Bean
    public KStream<String, GenericRecord> kStream(StreamsBuilder kStreamBuilder, RxImmunizationProcessor rxImmunizationProcessor) {
        KStream<String, GenericRecord> rxStream = null;
        startTime = System.nanoTime();
        log.info("startTime : {} ", startTime);
        GenericAvroSerde genericAvroSerde= new GenericAvroSerde();
        // Configure Serdes to use the same mock schema registry URL
        Map<String, String> config = new HashMap<>();

        config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);
        genericAvroSerde.configure(config, false);

        try {
            rxStream = kStreamBuilder.stream(rxRawTopic, Consumed.with(Serdes.String(), genericAvroSerde ));
            KStream<String, GenericRecord> fillStream = kStreamBuilder.stream(fillRawTopic, Consumed.with(Serdes.String(), genericAvroSerde ));
            //Convert RXStream to KTable as RX_RAW_TABLE
            KStream<String, String> rxRawStream = rxStream.filter((k, v) -> rxImmunizationProcessor.validateMsg(v, Constants.RX))
                    .mapValues(v -> v.get(Constants.DATA).toString());
                   // .toTable(Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(Constants.RX_RAW_TABLE).withValueSerde(Serdes.String()));
            //loggingUtil.info("RX topic is streamed to rx table. ", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
            //Convert FillStream to KTable as FILL_RAW_TABLE
            KStream<String, String> fillRawStream = fillStream.filter((k, v) -> rxImmunizationProcessor.validateMsg(v, Constants.FILL))
                    .mapValues(v -> v.get(Constants.DATA).toString());
                    //.toTable(Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(Constants.FILL_RAW_TABLE).withValueSerde(Serdes.String()));
            //loggingUtil.info("FILL topic is streamed to fill table. ", Constants.CORRELATION_ID, Constants.TX_ID, startTime);

            //Join the KTables by Merging the data
            KStream<String, String> mergedRxStream = fillRawStream.join(rxRawStream,
                    new ValueJoiner<String, String, String>() {

                @SuppressWarnings("unchecked")
                @Override
                public String apply(String fill,String rx) {
                    return rxImmunizationProcessor.mergeRxFill(rx, fill);
                }
            },
                    JoinWindows.of(Duration.ofDays(7))/*,
                    Joined.with(Serdes.String(), Serdes.String(), Serdes.String())*/)
                    .map((k,v)-> {
                        try {
                            return new KeyValue<>(mapper.readTree(v).get(Constants.PAT_ID).asText(), v);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
            //Group Merged table with PatId as key and merged record as value.
            KTable<String, String> finalRecord = mergedRxStream.groupByKey().
                    reduce(
                            (aggValue, newValue) -> {
                                /*if (enableAgglog) {
                                    loggingUtil.debug("Agg Value before :  " + aggValue, Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                                    loggingUtil.debug("New Value before :  " + newValue, Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                                }*/
                                if (aggValue == null || aggValue.equals("")) {
                                    aggValue = newValue;
                                } else {
                                    try {
                                        aggValue = rxImmunizationProcessor.concatenateWithDedup(aggValue, newValue);
                                    } catch (ParseException | InvalidDateException e) {
                                        loggingUtil.error("Parse exception in aggregation: " + e.getMessage(), Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                                    }
                                }
                               /* if (enableAgglog) {
                                    loggingUtil.debug("Agg Value after :  " + aggValue, Constants.CORRELATION_ID, Constants.TX_ID, startTime);
                                }*/

                                return aggValue;
                            }, Materialized.as("patid"));

            //if (enableAgglog)
                finalRecord.toStream().print(Printed.toSysOut());
            //Write to output topic
            finalRecord.toStream().map((k, v) -> rxImmunizationProcessor.formatRecord(k, v, batchLogSize)).to(rxImmuTopic, Produced.with(Serdes.String(), Serdes.String()));

           // loggingUtil.info("Merged RX and FILL tables.", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
        } catch (Exception e) {
           // loggingUtil.error(Constants.FAILURE + Constants.SVC_ERROR + e,
             //       Constants.CORRELATION_ID, Constants.TX_ID, startTime);
            e.printStackTrace();
        }
        return rxStream;
    }
}