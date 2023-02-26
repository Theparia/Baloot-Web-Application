package org.Baloot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandHandler {
    public static void main(String[] args) throws IOException {
        while(true) {
            InputStream input = System.in;
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            runCommand(buffer.readLine());
        }


    }
    private static void runCommand(String command) {

    }


}
