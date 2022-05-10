package com.wba.eapi.eapirximmunizationdatahydrator.config;

import com.wba.eapi.eapirximmunizationdatahydrator.common.CommonUtils;
import com.wba.eapi.eapirximmunizationdatahydrator.exception.WBADeserializationExceptionHandler;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RefreshScope
public class ProcessorConfig {
    public static final String DEFAULT_STREAMS_CONFIG_BEAN_NAME = "getStreamsBuilderFactoryBean";
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.cloud.stream.bindings.streams.default.group}")
    private String kafkaConsumerGroupid = "";
    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig = "";
    @Value("${spring.kafka.properties.security.protocol}")
    private String secProtocol = "";
    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism = "";
    @Value("${spring.kafka.properties.ssl.endpoint.identification.algorithm}")
    private String https = "HTTPS";
    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryURL;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.auto.commit.interval.ms}")
    private String autoCommitInterval;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.max.poll.records}")
    private String maxPollRecords;
    @Value("${spring.cloud.stream.bindings.streams.default.producer.max.request.size}")
    private String maxRequestSize;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.session.timeout.ms}")
    private String sessionTimeoutMs;
    @Value("${custom.kafka-password-path}")
    private String kafkaPassword;
    @Value("${custom.kafka-username-path}")
    private String kafkaUserName;
    @Value("${custom.sr-password-path}")
    private String srPassword;
    @Value("${custom.sr-username-path}")
    private String srUserName;
    @Value("${custom.streams.threads}")
    private String threads;
    @Value("${spring.cloud.stream.kafka.streams.bindings.process-in-0.consumer.keySerde}")
    private String keySerde;
    @Value("${spring.cloud.stream.kafka.streams.bindings.process-in-0.consumer.valueSerde}")
    private String valueSerde;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.autocommit.enable}")
    private String enableAutoCommit;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.isolation.level}")
    private String isolationLevel;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.reconnect.backoff.ms}")
    private int reconnectBackOffMs;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.default.api.timeout}")
    private int apiTimeOut;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.request.timeout.ms}")
    private int reqTimeout;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.max.task.idle.ms}")
    private int maxTaskIdleMs;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.exactly.once}")
    private Boolean exactlyOnce;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.replication.factor}")
    private int replicationFactor;
    @Value("${spring.cloud.stream.bindings.streams.default.consumer.configuration.cache.max.bytes.buffering}")
    private int cacheMaxBytes;
    @Value("${custom.pod.name}")
    private String podName;



    public KafkaStreamsConfiguration getKafkaStreamsConfiguration() {
        Map props = new HashMap<String, Object>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaConsumerGroupid);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.EXACTLY_ONCE, true);
        props.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        if (!"0".equals(threads)) {
            props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threads);
        }
        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, getJaasConfig());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, keySerde);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, valueSerde);
        if (exactlyOnce!=null && exactlyOnce == true )
            props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once");
        if (replicationFactor != 0)
            props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, WBADeserializationExceptionHandler.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryURL);
        props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, getSchemaRegistryUserInfo());
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG,"read_committed");
        props.put("group.instance.id",podName);

        if (isolationLevel != null && !"".equals(isolationLevel))
            props.put(ConsumerConfig.DEFAULT_ISOLATION_LEVEL, isolationLevel);
        if (enableAutoCommit != null && !"".equals(enableAutoCommit))
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        if(apiTimeOut != 0)
            props.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, apiTimeOut);
        if(reconnectBackOffMs != 0)
            props.put(StreamsConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, reconnectBackOffMs);
        if(reqTimeout != 0)
            props.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, reqTimeout);
        if(maxTaskIdleMs != 0)
            props.put(StreamsConfig.MAX_TASK_IDLE_MS_CONFIG,maxTaskIdleMs);
        props.put (StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, cacheMaxBytes);
        props.put (StreamsConfig.TOPOLOGY_OPTIMIZATION_CONFIG, StreamsConfig.OPTIMIZE);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilderFactoryBean getStreamsBuilderFactoryBean() {
        return new StreamsBuilderFactoryBean(getKafkaStreamsConfiguration());
    }


    public String getJaasConfig() {
        String jaasConfig = saslJaasConfig.replace("{kafka-user}", CommonUtils.getSecretFromPath(kafkaUserName));
        jaasConfig = jaasConfig.replace("{kafka-password}", CommonUtils.getSecretFromPath(kafkaPassword));
        System.setProperty("spring.kafka.properties.sasl.jaas.config", jaasConfig);
        return jaasConfig;
    }



    public String getSchemaRegistryUserInfo() {
        String userinfoConfig = CommonUtils.getSecretFromPath(srUserName) + ":" + CommonUtils.getSecretFromPath(srPassword);
        System.setProperty("spring.kafka.properties.schema.registry.basic.auth.user.info", userinfoConfig);
        return userinfoConfig;
    }
}
