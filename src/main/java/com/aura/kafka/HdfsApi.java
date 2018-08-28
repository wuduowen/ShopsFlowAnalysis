package com.aura.kafka;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zhangxianchao on 2018/8/15.
 */
public class HdfsApi {
    String url="/alibaba/";
//    String url="/alibaba";
    public HdfsApi(String filename){
        this.url=url + filename;
    }

    public BufferedReader getReader(){
        FileSystem fs=null ;
        BufferedReader bufferedReader=null;
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS","hdfs://bigdata:9000");
            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

            Path myPath = new Path(url);
            fs = myPath.getFileSystem(conf);
            if(fs.exists(myPath)){
                System.out.println("Reading from" + url + " on hdfs...");
                FSDataInputStream in=fs.open(myPath);
                bufferedReader=new BufferedReader(new InputStreamReader(in));
                return bufferedReader;
            }else{
                System.out.println(url + "is not exists");
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        } finally {
//            if(fs != null)
//                try{
//                    fs.close();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
        }
        return bufferedReader;
    }
}
