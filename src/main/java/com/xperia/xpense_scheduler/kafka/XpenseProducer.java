package com.xperia.xpense_scheduler.kafka;

import com.xperia.xpense_scheduler.jobs.models.MutualFundScheme;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.Future;

@Component
public class XpenseProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(XpenseProducer.class);
    private final KafkaProducer<String, MutualFundScheme> producer;

    public XpenseProducer(KafkaProducer<String, MutualFundScheme> producer){
        this.producer = producer;
    }

    public void send(String topic, String key, MutualFundScheme value){
        ProducerRecord<String, MutualFundScheme> record = new ProducerRecord<>(topic, key, value);
        try{
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get();
            LOGGER.debug("Sent record to topic :{} partition : {} offset : {}", metadata.topic(), metadata.partition(), metadata.offset());
        }catch (Exception ex){
            LOGGER.error("Error while sending message : {}", ex.getMessage(), ex);
        }
    }

    public void close(){
        this.producer.close();
    }
}
