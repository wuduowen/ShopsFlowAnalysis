package com.aura.spark;

import com.typesafe.config.Config;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zhangxianchao on 2018/8/18.
 */
public class RedisApi {
    private static int MAX_IDLE = 200;
    private static int TIMEOUT = 10000;
    private static boolean TEST_ON_BORROW = true;
    private static Config redisConfig=ConfigApi.getTypesafeConfig().getConfig("redis");
    private static JedisPool jedisPool=null;

    public static JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(MAX_IDLE);
        jedisPoolConfig.setTestOnBorrow(TEST_ON_BORROW);
        return  jedisPoolConfig;
    }
    public static  JedisPool getJedisPool(){

        if (jedisPool==null) {
//            synchronized (RedisApi.class) {
                jedisPool = new JedisPool(getJedisPoolConfig(),
                        redisConfig.getString("server"),
                        redisConfig.getInt("port"),
                        TIMEOUT);
//            }
        }
        return  jedisPool;
    }
}
