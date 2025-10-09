package com.xperia.xpense_scheduler.kafka;

import com.xperia.xpense_scheduler.models.MutualFundSchemeSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.xperia.models.MutualFundSchemeConsumerModel;

import java.util.Properties;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap.server}")
    private String serverAddress;

    @Primary
    @Bean
    public Properties getProducerProperties(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MutualFundSchemeSerializer.class.getName());
        return props;
    }

    @Bean
    public KafkaProducer<String, MutualFundSchemeConsumerModel> kafkaProducer(Properties kafkaProducerProperties){
        return new KafkaProducer<>(kafkaProducerProperties);
    }
}
