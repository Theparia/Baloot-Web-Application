package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            Response response = runCommand(command, jsonData);
            response.print();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private static Response runCommand(String command, String jsonData) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        switch (command){
            case "addUser":
                if(!jsonData.isEmpty()) {
                    User user = mapper.readValue(jsonData, User.class);
                    return baloot.addUser(user);
                }
                break;
            case "addProvider":
                if(!jsonData.isEmpty()) {
                    Provider provider = mapper.readValue(jsonData, Provider.class);
                    return  baloot.addProvider(provider);
                }
                break;
            case "addCommodity":
                if(!jsonData.isEmpty()) {
                    Commodity commodity = mapper.readValue(jsonData, Commodity.class);
                    return baloot.addCommodity(commodity);
                }
                break;
            case "getCommoditiesList":
                return baloot.getCommoditiesList();
            case "rateCommodity":
                if(!jsonData.isEmpty()) {
                    Map inputDataMap;
                    inputDataMap = mapper.readValue(jsonData, Map.class);
                    return baloot.rateCommodity((String) inputDataMap.get("username"),(Integer) inputDataMap.get("commodityId"), (Integer) inputDataMap.get("score"));
                }
            case "addToBuyList":
                if(!jsonData.isEmpty()) {
                    Map inputDataMap;
                    inputDataMap = mapper.readValue(jsonData, Map.class);
                    return baloot.addToBuyList((String) inputDataMap.get("username"),(Integer) inputDataMap.get("commodityId"));
                }
            case "removeFromBuyList":
                if(!jsonData.isEmpty()) {
                    Map inputDataMap;
                    inputDataMap = mapper.readValue(jsonData, Map.class);
                    return baloot.removeFromBuyList((String) inputDataMap.get("username"), (Integer) inputDataMap.get("commodityId"));
                }
            case "getCommodityById":
                if(!jsonData.isEmpty()) {
                    Map inputDataMap;
                    inputDataMap = mapper.readValue(jsonData, Map.class);
                    return baloot.getCommodityById((Integer) inputDataMap.get("id"));
                }
            case "getCommoditiesByCategory":
                if(!jsonData.isEmpty()) {
                    Map inputDataMap;
                    inputDataMap = mapper.readValue(jsonData, Map.class);
                    return baloot.getCommoditiesByCategory(String.valueOf(inputDataMap.get("category")));
                }
            case "getBuyList":
                if(!jsonData.isEmpty()) {
                    Map inputDataMap;
                    inputDataMap = mapper.readValue(jsonData, Map.class);
                    return baloot.getBuyList(String.valueOf(inputDataMap.get("username")));
                }
        }
        return null;
    }




}
