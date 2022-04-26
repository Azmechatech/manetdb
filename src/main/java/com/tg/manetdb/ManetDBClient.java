/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

import static j2html.TagCreator.s;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author manoj.kumar 
 */
public class ManetDBClient {
    public static void main(String[] args) {
		try {
			Socket sock = new Socket("localhost", 1010);
			DataOutputStream dout = new DataOutputStream(sock.getOutputStream());
			dout.writeBytes("Start#message#End");
			dout.flush();
			//dout.close();
                        
                        dout = new DataOutputStream(sock.getOutputStream());
			dout.writeBytes("Start#message#End2");
			dout.flush();
			dout.close();
                        
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e);
		}
	}
}
