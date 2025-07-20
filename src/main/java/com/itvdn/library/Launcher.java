package com.itvdn.library;


public class Launcher {

    public static void main(String[] args) {
        if(args.length == 0) {
            throw new RuntimeException("Missing config filename parameter.");
        }

    }
}