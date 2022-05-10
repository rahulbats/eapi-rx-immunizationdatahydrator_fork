package com.wba.eapi.eapirximmunizationdatahydrator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wba.eapi.eapirximmunizationdatahydrator.common.LoggingUtils;
import com.wba.eapi.eapirximmunizationdatahydrator.constant.Constants;
import com.wba.eapi.eapirximmunizationdatahydrator.exception.InvalidDateException;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
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


    @Bean
    public KStream<String, GenericRecord> kStream(StreamsBuilder kStreamBuilder, RxImmunizationProcessor rxImmunizationProcessor) {
        KStream<String, GenericRecord> rxStream = null;
        startTime = System.nanoTime();
        log.info("startTime : {} ", startTime);

        try {
            rxStream = kStreamBuilder.stream(rxRawTopic);
            KStream<String, GenericRecord> fillStream = kStreamBuilder.stream(fillRawTopic);
            //Convert RXStream to KTable as RX_RAW_TABLE
            KTable<String, String> rxRawTable = rxStream.filter((k, v) -> rxImmunizationProcessor.validateMsg(v, Constants.RX))
                    .mapValues(v -> v.get(Constants.DATA).toString())
                    .toTable(Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(Constants.RX_RAW_TABLE).withValueSerde(Serdes.String()));
            //loggingUtil.info("RX topic is streamed to rx table. ", Constants.CORRELATION_ID, Constants.TX_ID, startTime);
            //Convert FillStream to KTable as FILL_RAW_TABLE
            KTable<String, String> fillRawTable = fillStream.filter((k, v) -> rxImmunizationProcessor.validateMsg(v, Constants.FILL))
                    .mapValues(v -> v.get(Constants.DATA).toString())
                    .toTable(Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(Constants.FILL_RAW_TABLE).withValueSerde(Serdes.String()));
            //loggingUtil.info("FILL topic is streamed to fill table. ", Constants.CORRELATION_ID, Constants.TX_ID, startTime);

            //Join the KTables by Merging the data
            KTable<String, String> mergedRxTable = rxRawTable.join(fillRawTable, new ValueJoiner<String, String, String>() {

                @SuppressWarnings("unchecked")
                @Override
                public String apply(String rx, String fill) {
                    return rxImmunizationProcessor.mergeRxFill(rx, fill);
                }
            }, Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(Constants.RX_FILL_JOIN).withKeySerde(Serdes.String()).withValueSerde(Serdes.String()));
            //Group Merged table with PatId as key and merged record as value.
            KTable<String, String> finalRecord = mergedRxTable.groupBy((k, v) -> rxImmunizationProcessor.filterPatId(k, v), Grouped.with(Serdes.String(), Serdes.String())).
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
                            },
                            (aggValue, oldValue) -> aggValue
                            , Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(Constants.PAT_ID_AGGREGATOR).withKeySerde(Serdes.String()).withValueSerde(Serdes.String()));

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