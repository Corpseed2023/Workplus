package com.example.desktime.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordConfig {

    int length = 7;

    public static char[] geek_Password(int len)
    {
        // A strong password has Cap_chars, Lower_chars,
        // numeric value and symbols. So we are using all of
        // them to generate our password
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";


        String values = Capital_chars + Small_chars +
                numbers ;

        // Using random method
        Random rndm_method = new Random();

        char[] password = new char[len];

        for (int i = 0; i < len; i++)
        {

            password[i] =
                    values.charAt(rndm_method.nextInt(values.length()));

        }
        return password;
    }

}
