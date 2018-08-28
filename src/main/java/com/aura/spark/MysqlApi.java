package com.aura.spark;

import com.typesafe.config.Config;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxianchao on 2018/8/20.
 */
public class MysqlApi {

     @Test
    public  static Map<String,String> getMysqlData(){
        Config mysqlConfig=ConfigApi.getTypesafeConfig().getConfig("mysql");
        Map<String,String> shopCitys=new HashMap<String,String>();
        try {
            Class.forName(mysqlConfig.getString("driver"));
            Connection conn= DriverManager.getConnection(
                    mysqlConfig.getString("url"),
                    mysqlConfig.getString("user"),
                    mysqlConfig.getString("password"));
            Statement stmt=conn.createStatement();
            String sql=mysqlConfig.getString("sql");
            ResultSet rs= stmt.executeQuery(sql);
            while(rs.next()){
                shopCitys.put(rs.getString("shop_id"),rs.getString("city_name"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopCitys;
    }

}
