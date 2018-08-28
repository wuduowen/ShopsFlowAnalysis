package com.aura.spark.dataframe;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HotShop {

    private static String driverName = "com.mysql.jdbc.Driver";

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://bigdata:3306/koubei";
        String username = "root";
        String password = "root";
        Connection con = null;
        try {
            Class.forName(driverName); //classLoader,加载对应驱动
            con = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //replace "hive" here with the name of the user the queries should run as
//        Connection con = DriverManager.getConnection("jdbc:mysql://bigdata:3306/koubei", "bigdata", "bigdata");
        Statement stmt = con.createStatement();
        // select * query
        String sql = "SELECT shop_id, city_name, 0.7 *(score/5) + 0.3*(per_pay/((SELECT MAX(CAST(per_pay AS SIGNED)) FROM shop_info )* 1.0)) as hot\n" +
                "FROM shop_info WHERE city_name in ('北京', '上海', '广州') AND cate_2_name LIKE '%火锅%' ORDER BY hot desc LIMIT 10;";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
        }
    }

}
