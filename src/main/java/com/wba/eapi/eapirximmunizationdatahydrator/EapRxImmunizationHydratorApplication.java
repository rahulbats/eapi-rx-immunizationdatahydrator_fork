package com.wba.eapi.eapirximmunizationdatahydrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.kafka.utils.DlqPartitionFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.CleanupConfig;

import java.io.IOException;

/**
 * The Eapirximmunizationhydrator application.
 */
@SpringBootApplication
public class EapRxImmunizationHydratorApplication {

  /**
   * Process consumer. This generates the lambda function which is used in binder for Kafka streams.
   *
   * @return the consumer
   */


  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws IOException the io exception
   */

  public static void main(String[] args) throws IOException {
    SpringApplication.run(EapRxImmunizationHydratorApplication.class, args);
  }


 /* @Bean
  public StreamsBuilderFactoryBean defaultKafkaStreamsBuilder(KafkaStreamsConfiguration streamsConfig) {
    return new StreamsBuilderFactoryBean(streamsConfig, new CleanupConfig(true, false));
  }*/

  /**
   * dlq partition bean. Overrides default config and Sets number of DLQs to 1 despite the number of input topics
   *
   * @return the dlq partition function
   */
  @Bean
  public DlqPartitionFunction partitionFunction() {
    return (group, record, ex) -> 0;
  }


}
