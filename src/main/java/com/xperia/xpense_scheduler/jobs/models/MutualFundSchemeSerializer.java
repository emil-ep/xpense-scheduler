package com.xperia.xpense_scheduler.jobs.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class MutualFundSchemeSerializer implements Serializer<MutualFundScheme> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public byte[] serialize(String topic, MutualFundScheme mutualFundScheme) {
        try {
            return objectMapper.writeValueAsBytes(mutualFundScheme);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing MutualFundScheme", e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, MutualFundScheme data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
