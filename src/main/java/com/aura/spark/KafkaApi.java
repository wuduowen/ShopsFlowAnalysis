package com.aura.spark;

/**
 * Created by zhangxianchao on 2018/8/18.
 */

import com.typesafe.config.Config;

import java.util.HashMap;
import java.util.Map;

public class KafkaApi {

    public Map<String,String> getKafkaParms(){
        Config kafkaConfig= ConfigApi.getTypesafeConfig().getConfig("kafka");
        Map<String,String> kafkaParms=new HashMap<String,String>();
        kafkaParms.put("metadata.broker.list",kafkaConfig.getString("metadata.broker.list"));
        kafkaParms.put("auto.offset.reset",kafkaConfig.getString("auto.offset.reset"));
        kafkaParms.put("group.id",kafkaConfig.getString("group.id"));
        return kafkaParms;
    }

}
