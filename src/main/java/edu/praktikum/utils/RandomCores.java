package edu.praktikum.utils;

import java.util.Random;

public class RandomCores {
    public static String randomString(int length) {
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 128;
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (float) (rightLimit - leftLimit + 1));
            buffer.append(Character.toChars(randomLimitedInt));
        }
        return buffer.toString();
    }

}

