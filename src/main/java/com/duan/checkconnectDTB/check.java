 package com.duan.checkconnectDTB;

 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 import org.testng.annotations.Test;

 public class check {

     @Test
     public void testDatabaseConnection() throws SQLException {
         String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=DuAn2";
         String username = "sa";
         String password = "123";

         try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
             assert connection != null;
             System.out.println("Kết nối đến cơ sở dữ liệu thành công.");
         }
     }
 }