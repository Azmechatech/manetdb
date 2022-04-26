/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

import com.sun.net.httpserver.HttpHandler;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Manoj
 */
public abstract class CustomHandler  implements HttpHandler  {

    
    
    
      /**
     *
     * @param query
     * @return
     */
    public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
