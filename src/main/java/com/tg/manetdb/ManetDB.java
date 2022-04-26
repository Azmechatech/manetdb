/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manoj.kumar
 */
public class ManetDB {

    private static MapDBManager con = null;

    static {
        con = new MapDBManager(new File("/"), false);
    }

    public static MapDBManager getConnection() {
        return con;
    }

    public static void main(String[] args) throws Exception {
//        MapDBManager mapDBManager = new MapDBManager(new File("/"), false);

        int port = 8000;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext hc1 = server.createContext("/view", new SystemHealthHandler(con));

        hc1.setAuthenticator(new BasicAuthenticator("get") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("view") && pwd.equals("view@123");
            }
        });

        server.setExecutor(new ForkJoinPool(4)); // creates a default executor
        server.start();

        System.out.println("#Server Started");

        ServerSocket m_ServerSocket = new ServerSocket(1010);
        int id = 1;
        while (true) {
            Socket clientSocket = m_ServerSocket.accept();
//      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
//      cliThread.start();

            IOTListner cliThread = new IOTListner(clientSocket, id++, con);
            cliThread.start();
        }
    }
}
