package com.qpros.utils;

public class RandomString {

    private static final String CHAR_LIST ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int RANDOM_STRING_LENGTH = 10;

       //TODO add massive methods for data generator for now will be using faker




    public static int getRandomNumber(){
        double x = Math.random();
        return (int)x;
    }
}
