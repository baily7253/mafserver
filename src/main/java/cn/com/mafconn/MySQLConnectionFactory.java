package cn.com.mafconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cn.com.mafcofig.Global;


public class MySQLConnectionFactory {
	
    public  static Global prop=new Global();
    public  static  Connection createConnection() {
        Connection conn = null;
        try {
            String url = prop.getProperty("url").trim();
            String userName = prop.getProperty("userName").trim();
            String password = prop.getProperty("password").trim();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQLDriver not found");
        } catch (SQLException e) {
            System.out.println("database user or password is not correct");
        }
        return conn;
    }

}
