package com.aura.spark;

import com.google.common.collect.Sets;
import kafka.serializer.StringDecoder;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import redis.clients.jedis.Jedis;
import scala.Tuple2;
import scala.tools.cmd.gen.AnyVals;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhangxianchao on 2018/8/18.
 */
public class SparkStreamingAna {
    public static void main(String[] args) {
        runAna();
    }
    public static void runAna(){

        //kafka
        KafkaApi kafkaApi=new KafkaApi();
        Map<String,String> kafkaParms=kafkaApi.getKafkaParms();

        //ssc
        SparkStreamingApi sparkStreamingApi=new SparkStreamingApi();
        JavaStreamingContext ssc = sparkStreamingApi.getSsc();

        //mysql to boradcast's map
        Map<String,String> shopCityMap= MysqlApi.getMysqlData();
        Broadcast<Map<String,String>> shopCitys=ssc.sparkContext().broadcast(shopCityMap);

        //ssc linkto kafka
        ssc.sparkContext().setLogLevel("WARN");
        String topic=ConfigApi.getTypesafeConfig().getString("streaming.topic");

        JavaPairInputDStream<String,String> input=KafkaUtils.createDirectStream(
                 ssc,
                 String.class,
                 String.class,
                 StringDecoder.class,
                 StringDecoder.class,
                 kafkaParms,
                 Sets.newHashSet(topic));

        //ana
        processByShop(input,shopCitys);
        ssc.start();
        try {
            ssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //over
        System.out.printf("over");
    }


    public static void processByShop(JavaPairInputDStream<String,String> input,Broadcast<Map<String,String>> shopCitys){
        System.out.println("start print");
        //create JavaPairDStream
        JavaPairDStream<String,Long> shopMap=input.mapToPair(x->
        {
            return new Tuple2<String,Long>(x._2().split(",")[0],Long.valueOf(1));
        }).reduceByKey((x,y) -> x + y);

        //shop -> city
        JavaPairDStream<String,Long> cityMap= input.mapToPair(x->
        {
            String city=shopCitys.value().get(x._2().split(",")[0]);
            return new Tuple2<String,Long>(city,Long.valueOf(1));
        }).reduceByKey((x,y) -> x + y);

//        JavaPairDStream<String,Long> cityMap2=cityMap.mapToPair(x->{
//          return new Tuple2<String,Long>(x._1,x_2+1);}
//        );

       //foreachRDD
        shopMap.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> stringLongJavaPairRDD) throws Exception {
                stringLongJavaPairRDD.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> tuple2Iterator) throws Exception {
                        Jedis jedis=RedisApi.getJedisPool().getResource();
                        while(tuple2Iterator.hasNext()){
                            Tuple2<String,Long> pair=tuple2Iterator.next();
                            jedis.hincrBy("shop-jiaoyi",pair._1(),pair._2());
                            System.out.println("shopid:"+pair._1() + "---jiaoyi num:" + pair._2());
                        }
                        jedis.close();
                    }
                });
            }
        });

        cityMap.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> stringLongJavaPairRDD) throws Exception {
                stringLongJavaPairRDD.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> tuple2Iterator) throws Exception {
                        Jedis jedis=RedisApi.getJedisPool().getResource();
                        while(tuple2Iterator.hasNext()){
                            Tuple2<String,Long> pair=tuple2Iterator.next();
                            jedis.hincrBy("city-jiaoyi",pair._1(),pair._2());
                            System.out.println("city:"+pair._1() + "---city num:" + pair._2());
                        }
                        jedis.close();
                    }
                });
            }
        });


        System.out.println("stop print");

    }
}
