package com.aura.kafka;



import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by zhangxianchao on 2018/8/15.
 */
public class UserPayProducer {
    public static void main(String[] args) {
        //hdfsreader
        String filename=args[0];
        HdfsApi hdfsApi=new HdfsApi(filename);
        BufferedReader hdfsReader=hdfsApi.getReader();

        //kafkatopic
        TopicApi kafkaApi=new TopicApi();
        Producer kafkaProducer=kafkaApi.getProducer();


        System.out.println("starting ok");
        //hdfs-->kafakaTopic
        String UserPayRecord=null;
        try {
            while ((UserPayRecord=hdfsReader.readLine())!=null){
                String[] str= UserPayRecord.split(",");
                ProducerRecord producerRecord=new ProducerRecord("user_pay",str[0],str[1]+","+str[2]);
                kafkaProducer.send(producerRecord);
                System.out.println("Sending record to the Topic of the user_pay...");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
