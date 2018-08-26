package com.aura.spark.dataframe;

import java.sql.*;

public class HiveJDBCTopTransShop {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

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
        // select * query
        String sql = "SELECT s.shop_id, (s.per_pay * b.total_count) /(b.total_day * 1.0) as total_trans\n" +
                "FROM(\n" +
                "SELECT a.shop_id, COUNT(a.partition_date) as total_day, SUM(a.tran_count) as total_count FROM\n" +
                "(select v.shop_id, v.partition_date, COUNT(*) as tran_count FROM user_pay_p v GROUP BY v.shop_id, v.partition_date) a\n" +
                "GROUP BY a.shop_id\n" +
                ") b\n" +
                "join shop_info s ON b.shop_id = s.shop_id\n" +
                "ORDER BY total_trans desc limit 10";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
        }
    }


}