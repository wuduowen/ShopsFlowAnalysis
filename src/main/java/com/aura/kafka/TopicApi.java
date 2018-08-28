package com.aura.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by zhangxianchao on 2018/8/15.
 */
public class TopicApi {

    public Producer<String, String> getProducer()
    {
        Properties props = getConfig();
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        return producer;

    }

    // config
    public Properties getConfig()
    {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

}
