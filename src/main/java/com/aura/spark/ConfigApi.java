package com.aura.spark;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zhangxianchao on 2018/8/18.
 */
public  class ConfigApi {
    public  static Config getTypesafeConfig(){
        return  ConfigFactory.parseResources("conf/kafka-spark-redis.conf");
    }

}
