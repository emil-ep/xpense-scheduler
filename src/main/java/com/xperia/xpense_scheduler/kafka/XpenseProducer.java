package com.xperia.xpense_scheduler.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class XpenseProducer<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XpenseProducer.class);
    private final KafkaProducer<K, V> producer;

    public XpenseProducer(KafkaProducer<K, V> producer){
        this.producer = producer;
    }

    public void send(String topic, K key, V value){
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
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
