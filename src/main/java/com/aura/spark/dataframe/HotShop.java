package com.aura.spark.dataframe;

import java.sql.*;

public class HotShop {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
        String insertSql = "insert into hot_shop " +
                "SELECT shop_id, city_name, 0.7 *score/5 + 0.3*per_pay/20 as hot \n" +
                "FROM shop_info WHERE city_name in ('北京', '上海', '广州') AND cate_2_name LIKE '%火锅%' ORDER BY hot desc";
        stmt.execute(insertSql);
        con.close();
    }

}
