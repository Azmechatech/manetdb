/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manoj.kumar
 */
public class IOTListner extends Thread {

    Socket clientSocket;
    int clientID = 1;
    boolean running = true;
    Connection con;
    String dbURL = "";
    MapDBManager mapDBManager;
    public static final ConcurrentHashMap<String, String> messageQueue = new ConcurrentHashMap<>();//Actual queue can be an option

    IOTListner(Socket s, int i, MapDBManager mapDBManager) {
        clientSocket = s;
        clientID = i;
        this.mapDBManager = mapDBManager;
        // AlertService.load();//Load cache. Need to restart this as and when new devices are added.
    }

    public void run() {

        String sqls2 = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            try {

                while (running) {

                    //Write back to client at will
                    if (messageQueue.size() > 0) {//In future add the client id
                        //write object to Socket
                        messageQueue.entrySet().forEach(kv -> {

                            try {
                                //out.write("Care Smart >> " + kv.getValue());//
                                oos.writeObject("Care Smart >> " + kv.getValue());
                            } catch (Exception ex) {
                                Logger.getLogger(IOTListner.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });
                        //out.flush();
                        oos.flush();
                    }

                    String clientCommand = in.readLine();
                    if (clientCommand != null) {
                        //ByteBuffer buffer = StandardCharsets.UTF_8.encode(clientCommand); 
                        //clientCommand = StandardCharsets.UTF_8.decode(buffer).toString();
                        System.out.println(clientCommand);
                        System.out.println(clientCommand.startsWith("msg=device_data"));
                        System.out.println(clientCommand.contains("msg=device_data"));
                        System.out.println(clientCommand.indexOf("msg=device_data"));
                        System.out.println(clientCommand.split(",").length);
                        System.out.println(clientCommand.split(",")[0]);
                        System.out.println(clientCommand.split(",")[0].contentEquals("msg=device_data"));
                        if (clientCommand.startsWith("msg=device_data")) {
                           // String dataRaw = clientCommand.substring("device_data".length());
                            //msg=device_data,device_id=123,date_time=12:12:12,data_poll_datetime=12:12:12,tot_volume=10
                            //msg=device_data,device_id=123,date_time=2022-03-12 14:21:32.0,data_poll_datetime=2022-03-12 14:21:32.0,tot_volume=10
                            String eachCell[] = clientCommand.split(",");
                            HashMap<String, String> queryParam = new HashMap();
                            String valueString="'";
                            for (String cell : eachCell) {
                                String kv[] = cell.split("=");
                                queryParam.put(kv[0], kv[1]);
                                System.out.println(cell+">"+kv[0]+"=>>"+queryParam.get(kv[0]));
                                if(!kv[0].equalsIgnoreCase("msg"))
                                valueString=valueString+kv[1]+"',";
                            }
                            
                            queryParam.entrySet().forEach(kv->{System.out.println(kv.getKey());});
                            System.out.println(queryParam.containsKey("date_time2"));
                            System.out.println("date_time2".equalsIgnoreCase("date_time2"));
                           
                           
                              System.out.println("INSERT INTO `device_data`(device_id,date_time,data_poll_datetime,tot_volume) VALUE ('"
                                        + queryParam.get("device_id") + "','"
                                        + queryParam.get("date_time") + "','"
                                        + queryParam.get("data_poll_datetime") + "','"
                                        + queryParam.get("tot_volume") + "')");

                               queryParam.entrySet().forEach(kv->{System.out.println(kv.getKey()+" => "+ kv.getValue());});
                               
                               
                            Connection c = MySqlDBManager.getConnection();
                            Statement s = c.createStatement();
                            /**
                             * device_id INT , date_time DATETIME,
                             * data_poll_datetime DATETIME, tot_volume
                             * DECIMAL(4,2)
                             */
                            if (eachCell.length == 5)
                                    try {
                                      
                                        
                                s.executeUpdate("INSERT INTO `device_data`(device_id,date_time,data_poll_datetime,tot_volume) VALUE ('"
                                        + queryParam.get("device_id") + "','"
                                        + queryParam.get("date_time") + "','"
                                        + queryParam.get("data_poll_datetime") + "','"
                                        + queryParam.get("tot_volume") + "')");
                            } finally {
                                try {
                                    s.close();
                                } catch (Throwable ignore) {
                                    /* Propagate the original exception
instead of this one that you may want just logged */ }
                            }
                        }
                        //System.out.println("Client Says :" + clientCommand);

                        //String[] str = clientCommand.split("#");
                        // Decode and write the data
                        mapDBManager.uploadChunk.put(System.currentTimeMillis(), clientSocket.getRemoteSocketAddress().toString() + ": " + clientCommand);
                        mapDBManager.commit();
                    }
                    if (clientCommand != null && clientCommand.equalsIgnoreCase("quit")) {
                        running = false;
                    } else {

                    }

                    //create ObjectOutputStream object
                    //Clear the messages // In future only delete relevant messages.
                    //messageQueue.clear();
                    //close resources
                }

                //  oos.close();
                con.close();
            } //end try //end try
            catch (Exception exception) {
                exception.printStackTrace();
                if (exception.getMessage().contains("java.net.UnknownHostException") || exception.getMessage().contains("The connection to the host")) {
                    System.exit(0);
                } else if (exception.getMessage().contains("Login failed for user")) {
                    exception.printStackTrace();
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
