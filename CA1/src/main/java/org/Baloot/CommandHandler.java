package org.Baloot;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandHandler {

    private Baloot baloot;

    public CommandHandler(){
        this.baloot = new Baloot();
    }
    public static void main(String[] args) throws IOException {
        while(true) {
            InputStream input = System.in;
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            start(buffer.readLine());
        }


    }

    private static String[] parseInput(String input) {
        return input.split(" ", 2);
    }

    private static void start(String input){
        String[] input_parts = parseInput(input);
        String command = input_parts[0];
        String jsonData = "";
        if (input_parts.length == 2) {
            jsonData = input_parts[1];
        }
//        switch (command){
//            case "addUser":
//
//
//        }
        runCommand();

    }
    private static void runCommand() {



    }


}
