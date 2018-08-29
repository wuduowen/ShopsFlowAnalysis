package com.aura.spark.dataframe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HotShop {

    private static String driverName = "com.mysql.jdbc.Driver";

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        //replace "hive" here with the name of the user the queries should run as
        Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/koubei", "bigdata", "bigdata");

        Statement stmt = con.createStatement();
        String dropSql = "drop table if exists hot_shop";
        stmt.execute(dropSql);
        String createSql = "create table if not exists koubei.hot_shop (\n" +
                " shop_id string,\n" +
                " city_name string,\n" +
                " hot double\n" +
                ")ROW FORMAT DELIMITED\n" +
                " FIELDS TERMINATED BY ','";
        stmt.execute(createSql);
        String sql = "insert into hot_shop " +
                "SELECT shop_id, city_name, 0.7 *(score/5) + 0.3*per_pay/20 as hot \n" +
                "FROM shop_info WHERE city_name in ('北京', '上海', '广州') AND cate_2_name LIKE '%火锅%' ORDER BY hot desc";
        stmt.execute(sql);
    }

}
