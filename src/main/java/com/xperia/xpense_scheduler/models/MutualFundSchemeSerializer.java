package com.xperia.xpense_scheduler.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.xperia.models.MutualFundSchemeConsumerModel;

public class MutualFundSchemeSerializer implements Serializer<MutualFundSchemeConsumerModel> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public byte[] serialize(String topic, MutualFundSchemeConsumerModel mutualFundScheme) {
        try {
            return objectMapper.writeValueAsBytes(mutualFundScheme);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing MutualFundScheme", e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, MutualFundSchemeConsumerModel data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
