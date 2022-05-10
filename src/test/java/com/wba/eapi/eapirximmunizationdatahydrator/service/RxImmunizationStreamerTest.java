package com.wba.eapi.eapirximmunizationdatahydrator.service;

import com.wba.eapi.eapirximmunizationdatahydrator.common.LoggingUtils;
import com.wba.eapi.eapirximmunizationdatahydrator.constant.Constants;
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.tomcat.util.bcel.Const;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RxImmunizationStreamerTest {
    RxImmunizationStreamer rxImmunizationStreamer = new RxImmunizationStreamer();
    private static final String SCHEMA_REGISTRY_SCOPE = RxImmunizationStreamerTest.class.getName();
    private static final String MOCK_SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE;
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, GenericRecord> rxTopic;
    private TestInputTopic<String, GenericRecord> fillTopic;
    private TestOutputTopic<String, String> immunizationTopic;
    Schema rxSchema = null;
    Schema fillSchema = null;

    @InjectMocks
    RxImmunizationProcessor rxImmunizationProcessor;

    @Mock
    LoggingUtils loggingUtil;

    @Before
    public void beforeEach() throws Exception {
        // Create topology to handle stream of users

        StreamsBuilder builder = new StreamsBuilder();

        new RxImmunizationStreamer().kStream(builder, rxImmunizationProcessor);
        Topology topology = builder.build();

        // Dummy properties needed for test diver
        Properties props = new Properties();
        Random rand = new Random();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,""+ rand.nextInt(100000));
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);

        // Create test driver
        testDriver = new TopologyTestDriver(topology, props);

        // Create Serdes used for test record keys and values
        Serde<String> stringSerde = Serdes.String();
        rxSchema = new Schema.Parser().parse(new File("/Users/rahul/Downloads/eapi-rx-immunizationdatahydrator_fork/eapi-rx-immunizationdatahydrator_fork/src/test/schema-prod-eapi-prescription-immune-tbf0_rx_replica-value-v1.avsc"));
        fillSchema = new Schema.Parser().parse(new File("/Users/rahul/Downloads/eapi-rx-immunizationdatahydrator_fork/eapi-rx-immunizationdatahydrator_fork/src/test/schema-prod-eapi-prescription-immune-fill_replica-value-v2.avsc"));

        final Serde<GenericRecord> genericAvroSerde = new GenericAvroSerde();

        // Configure Serdes to use the same mock schema registry URL
        Map<String, String> config = new HashMap<>();

        config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        genericAvroSerde.configure(config, false);


        // Define input and output topics to use in tests
        rxTopic = testDriver.createInputTopic(
                RxImmunizationStreamer.rxRawTopic,
                stringSerde.serializer(),
                genericAvroSerde.serializer());
        fillTopic = testDriver.createInputTopic(
                RxImmunizationStreamer.fillRawTopic,
                stringSerde.serializer(),
                genericAvroSerde.serializer());
        immunizationTopic = testDriver.createOutputTopic(
                RxImmunizationStreamer.rxImmuTopic,
                stringSerde.deserializer(),
                stringSerde.deserializer());

    }

    @AfterEach
    void afterEach() {
        testDriver.close();
        MockSchemaRegistry.dropScope(SCHEMA_REGISTRY_SCOPE);
    }

    @Test
    public void testHydrator(){
        GenericRecord rxRecord = new GenericData.Record(rxSchema);

        /*GenericRecord rxHeaders = new GenericData.Record(rxSchema.getField("headers").schema());
        GenericData.EnumSymbol insertOperation = new GenericData.EnumSymbol( rxSchema.getField("headers").schema().getField("operation").schema(), "INSERT");
        rxHeaders.put("operation", insertOperation);
        rxRecord.put("headers", rxHeaders);*/
        GenericRecord rxData = new GenericData.Record(rxSchema.getField(Constants.DATA).schema());
        rxData.put(Constants.STORE_NBR, "store1");
        rxData.put(Constants.RX_NBR, "rx1");
        rxData.put(Constants.RX_IMMU_IND,"Y");
        rxData.put(Constants.PAT_ID,"patient");
        rxData.put(Constants.UPDATE_DTTM,"2015-10-09 16:01:39");
        rxRecord.put(Constants.DATA, rxData);
        GenericRecord fillRecord = new GenericData.Record(fillSchema);
        GenericRecord fillData = new GenericData.Record(fillSchema.getField(Constants.DATA).schema());
        fillData.put(Constants.STORE_NBR, "store1");
        fillData.put(Constants.RX_NBR, "rx1");
        fillData.put(Constants.UPDATE_DTTM,"2015-10-09 16:01:39");
        fillRecord.put(Constants.DATA, fillData);

        rxTopic.pipeInput("store1_rx1", rxRecord, System.currentTimeMillis());
        fillTopic.pipeInput("store1_rx1", fillRecord, System.currentTimeMillis());
        assertFalse(immunizationTopic.isEmpty());
        System.out.println(immunizationTopic.readRecord());
    }

    @Test
    public void testDedup(){
        GenericRecord rxRecord = new GenericData.Record(rxSchema);

        /*GenericRecord rxHeaders = new GenericData.Record(rxSchema.getField("headers").schema());
        GenericData.EnumSymbol insertOperation = new GenericData.EnumSymbol( rxSchema.getField("headers").schema().getField("operation").schema(), "INSERT");
        rxHeaders.put("operation", insertOperation);
        rxRecord.put("headers", rxHeaders);*/
        GenericRecord rxData = new GenericData.Record(rxSchema.getField(Constants.DATA).schema());
        rxData.put(Constants.STORE_NBR, "store1");
        rxData.put(Constants.RX_NBR, "rx1");
        rxData.put(Constants.RX_IMMU_IND,"Y");
        rxData.put(Constants.PAT_ID,"patient");
        rxData.put(Constants.UPDATE_DTTM,"2015-10-09 16:01:39");
        rxRecord.put(Constants.DATA, rxData);
        GenericRecord fillRecord = new GenericData.Record(fillSchema);
        GenericRecord fillData = new GenericData.Record(fillSchema.getField(Constants.DATA).schema());
        fillData.put(Constants.STORE_NBR, "store1");
        fillData.put(Constants.RX_NBR, "rx1");
        fillData.put(Constants.UPDATE_DTTM,"2015-10-09 16:01:39");
        fillRecord.put(Constants.DATA, fillData);

        rxTopic.pipeInput("store1_rx1", rxRecord, System.currentTimeMillis());
        fillTopic.pipeInput("store1_rx1", fillRecord, System.currentTimeMillis());

        GenericRecord rxRecord2 = new GenericData.Record(rxSchema);
        GenericRecord rxData2 = new GenericData.Record(rxSchema.getField(Constants.DATA).schema());
        rxData2.put(Constants.STORE_NBR, "store1");
        rxData2.put(Constants.RX_NBR, "rx1");
        rxData2.put(Constants.RX_IMMU_IND,"Y");
        rxData2.put(Constants.PAT_ID,"patient");
        rxData2.put(Constants.UPDATE_DTTM,"2015-10-09 16:01:39");
        rxRecord2.put(Constants.DATA, rxData2);
        GenericRecord fillRecord2 = new GenericData.Record(fillSchema);
        GenericRecord fillData2 = new GenericData.Record(fillSchema.getField(Constants.DATA).schema());
        fillData2.put(Constants.STORE_NBR, "store1");
        fillData2.put(Constants.RX_NBR, "rx1");
        fillData2.put(Constants.UPDATE_DTTM,"2015-10-09 16:01:39");
        fillRecord2.put(Constants.DATA, fillData2);

        rxTopic.pipeInput("store1_rx1", rxRecord, System.currentTimeMillis());
        fillTopic.pipeInput("store1_rx1", fillRecord2, System.currentTimeMillis());


        assertFalse(immunizationTopic.isEmpty());
        System.out.println(immunizationTopic.readRecord());
        JSONObject immuObject = new JSONObject(immunizationTopic.readRecord().value());
        JSONArray array =  (JSONArray) immuObject.get("immunizations");
        assertTrue(array.length()==1);
    }
}
