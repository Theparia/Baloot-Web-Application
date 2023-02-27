package org.Baloot;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class CommandHandler {

    private static Baloot baloot = new Baloot();

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

    private static void start(String input) throws IOException {
        String[] input_parts = parseInput(input);
        String command = input_parts[0];
        String jsonData = "";
        if (input_parts.length == 2) {
            jsonData = input_parts[1];
        }
        try {
            runCommand(command, jsonData);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private static void runCommand(String command, String jsonData) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        switch (command){
            case "addUser":
                if(!jsonData.isEmpty()) {
                    User user = mapper.readValue(jsonData, User.class);
                    baloot.addUser(user);
                }
//                else //TODO: print error?
                System.out.println("User Added");

                break;
            case "addProvider":
                if(!jsonData.isEmpty()) {
                    Provider provider = mapper.readValue(jsonData, Provider.class);
                    baloot.addProvider(provider);
                }
                System.out.println("Provider Added");
                break;
            case "addCommodity":
                System.out.println("Adding Commodity");
                if(!jsonData.isEmpty()) {
                    Commodity commodity = mapper.readValue(jsonData, Commodity.class);
                    baloot.addCommodity(commodity);
                }
                break;

        }
//        baloot.printData(); //checking

    }


}
