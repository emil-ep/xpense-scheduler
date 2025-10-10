package com.xperia.xpense_scheduler.kafka;

import com.xperia.xpense_scheduler.models.MutualFundSchemeSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public Properties getBaseProducerProperties(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    @Bean(name = "mutualFundProducer")
    public KafkaProducer<String, MutualFundSchemeConsumerModel> mutualFundProducer(Properties baseProducerProps){
        Properties props = new Properties();
        props.putAll(baseProducerProps);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MutualFundSchemeSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    @Bean(name = "mutualFundDetailProducer")
    public KafkaProducer<String, String> mutualFundDetailProducer(Properties baseProducerProps){
        Properties props = new Properties();
        props.putAll(baseProducerProps);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    @Bean("mutualFundSchemeXpenseProducer")
    public XpenseProducer<String, MutualFundSchemeConsumerModel> mutualFundSchemeXpenseProducer(
            @Qualifier("mutualFundProducer") KafkaProducer<String, MutualFundSchemeConsumerModel> kafkaProducer){
        return new XpenseProducer<>(kafkaProducer);
    }

    @Bean("mutualFundDetailXpenseProducer")
    public XpenseProducer<String, String> mutualFundDetailXpenseProducer(
            @Qualifier("mutualFundDetailProducer") KafkaProducer<String, String> kafkaProducer){
        return new XpenseProducer<>(kafkaProducer);
    }
}
