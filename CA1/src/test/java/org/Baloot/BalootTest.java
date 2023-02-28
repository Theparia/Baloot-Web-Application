package org.Baloot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BalootTest {
    private static Baloot baloot;
    private static User[] users;
    private static Provider[] providers;
    private static Commodity[] commodities;

    @BeforeClass
    public static void setup(){
        baloot = new Baloot();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            users = objectMapper.readValue(new File("src/test/database/users.json"), User[].class);
            providers = objectMapper.readValue(new File("src/test/database/providers.json"), Provider[].class);
            commodities = objectMapper.readValue(new File("src/test/database/commodities.json"), Commodity[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Before
    public void setBalootParameters(){
        for(User user : users)
            baloot.addUser(user);
        for(Provider provider: providers)
            baloot.addProvider(provider);
        for (Commodity commodity: commodities)
            baloot.addCommodity(commodity);
    }

    @Test
    public void test_rating_out_of_range_1_to_10(){
        Response response = baloot.rateCommodity("1", "1", 11);
        assertFalse(response.isSuccess());
        assertEquals("Rating Out of Range", response.getData().get("Error").asText());
    }

}
