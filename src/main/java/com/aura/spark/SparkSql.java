package com.aura.spark;


import org.apache.spark.sql.SparkSession;

/**
 * Created by zhangxianchao on 2018/8/25.
 */
public class SparkSql {
    public static void main(String[] args) {
        SparkSession sparkSession=SparkSession.builder().master("local").appName("sparksql").getOrCreate();

    }
}
