package com.example.utils;

import java.util.Random;

public class RandomNumberGenerator {

    public static String generateEightDigitRandomNumber() {
        Random random = new Random();
        int number = random.nextInt(100000000);
        return String.format("%08d", number);
    }
}

