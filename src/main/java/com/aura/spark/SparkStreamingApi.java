package com.aura.spark;


import com.typesafe.config.Config;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkStreamingApi {
    public JavaStreamingContext getSsc(){
        JavaStreamingContext ssc=null;
        Config sparkStreamingConfig= ConfigApi.getTypesafeConfig().getConfig("streaming");
        SparkConf conf = new SparkConf();
        conf.setAppName(sparkStreamingConfig.getString("name"));
        conf.set("spark.streaming.stopGracefullyOnShutdown", "true");
        conf.setMaster(sparkStreamingConfig.getString("master"));
        Duration batchInterval = Durations.seconds(sparkStreamingConfig.getLong("interval"));
        ssc = new JavaStreamingContext(conf, batchInterval);
        return  ssc;
    }

}
