/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

/**
 *
 * @author manoj.kumar
 */
public class Protocol {
    // Static variable reference of single_instance
    // of type Singleton

    private static Protocol single_instance = null;

    // Declaring a variable of type String
    public String s;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private Protocol() {
        s = "Hello I am a string part of Singleton class";
    }

    // Static method
    // Static method to create instance of Singleton class
    public static Protocol getInstance() {
        if (single_instance == null) {
            single_instance = new Protocol();
        }

        return single_instance;
    }
    
    
}
