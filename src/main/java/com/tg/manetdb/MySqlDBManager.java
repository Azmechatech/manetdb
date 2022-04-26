/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manoj.kumar
 */
public class MySqlDBManager {

    private static Connection con = null;

    static {
        String url = "jdbc:mysql://localhost:3306/org?autoReconnect=true&useSSL=false";
        String user = "root";
        //String pass = "root";//local
        String pass = "Root@123";//Stage
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() {
        return con;
    }

    public static void main(String... args) throws SQLException {

        DatabaseMetaData dmd = MySqlDBManager.getConnection().getMetaData();
        System.out.println("DatabaseProductName :\t" + dmd.getDatabaseProductName());
        System.out.println("DatabaseProductVersion :\t" + dmd.getDatabaseProductVersion());
        System.out.println("DatabaseMajorVersion :\t" + dmd.getDatabaseMajorVersion());
        System.out.println("DatabaseMinorVersion :\t" + dmd.getDatabaseMinorVersion());
        System.out.println("DriverName :\t" + dmd.getDriverName());
        System.out.println("DriverVersion :\t" + dmd.getDriverVersion());
        System.out.println("DriverMajorVersion :\t" + dmd.getDriverMajorVersion());
        System.out.println("DriverMinorVersion :\t" + dmd.getDriverMinorVersion());

    }
}
