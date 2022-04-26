/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.TreeSet;

/**
 *
 * @author Manoj
 */
public class SystemHealthHandler extends CustomHandler  {

    MapDBManager mapDBManager;
    
    public SystemHealthHandler(MapDBManager mapDBManager){
        this.mapDBManager=mapDBManager;
    }
    
    @Override
    public void handle(HttpExchange t) throws IOException {

        StringBuilder data = new StringBuilder();
        data.append("<html><meta http-equiv=\"refresh\" content=\"5\"><h1>ManetDB Debug View</h1> "
                + "<a href=\"/purge\" target=\"_blank\">Click here to purge data</a>   "
         
                //DeviceState
                + "<hr>");
        TreeSet<Long> tst=new TreeSet();
        mapDBManager.uploadChunk.entrySet().forEach(kv -> {
            //Date date=new Date(kv.getKey());
            //data.append(date + ":" + kv.getValue()).append("<br>");
            tst.add(kv.getKey());
        });
        
        tst.stream().forEach(obj->{
           // mapDBManager.uploadChunk.get(obj);
            Date date=new Date(obj);
            data.append(date+">> "+mapDBManager.uploadChunk.get((Long)obj)).append("<br>");
            
        });
        
        data.append("</html>");
        //System.out.println(response.toString());
        byte[] result = data.toString().getBytes();
        t.sendResponseHeaders(200, result.length);
        OutputStream os = t.getResponseBody();
        os.write(result);
        os.close();
    }
    
}
