package org.Baloot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class BalootTest {
    private static Baloot baloot;
    private static User[] users;
    private static Provider[] providers;
    private static Commodity[] commodities;

    @BeforeClass
    public static void preClass() {
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
    public void setUp() {
        baloot = new Baloot();
        for (User user : users)
            baloot.addUser(user);
        for (Provider provider : providers)
            baloot.addProvider(provider);
        for (Commodity commodity : commodities)
            baloot.addCommodity(commodity);
    }

    @After
    public void tearDown() {
        baloot = null;
    }

    @AfterClass
    public static void postClass() {
        users = null;
        providers = null;
        commodities = null;
    }

    //    rateCommodity
    @Test
    public void test_update_rating_correctly() {
        baloot.rateCommodity("paria", 1, 5);
        assertEquals("6.25", baloot.getCommodityById(1).getData().get("rating").asText());
    }

    @Test
    public void test_rating_out_of_range_1_to_10() {
        Response response = baloot.rateCommodity("parnian", 1, 11);
        assertFalse(response.isSuccess());
        assertEquals("Rating Out of Range", response.getData().get("Error").asText());
    }

    @Test
    public void test_replace_rating_for_existing_user() {
        baloot.rateCommodity("paria", 1, 5);
        baloot.rateCommodity("paria", 1, 7);
        assertEquals("7.0", baloot.findCommodityById(1).getUsersRating().get("paria").toString());
        assertEquals(2, baloot.findCommodityById(1).getUsersRating().size());
    }

    @Test
    public void test_commodity_does_not_exist_for_rating() {
        Response response = baloot.rateCommodity("paria", 4, 9);
        assertFalse(response.isSuccess());
        assertEquals("Commodity does not exist.", response.getData().get("Error").asText());
    }

    @Test
    public void test_user_does_not_exist_for_rating() {
        Response response = baloot.rateCommodity("amin", 1, 5);
        assertFalse(response.isSuccess());
        assertEquals("User does not exist.", response.getData().get("Error").asText());
    }

    //    getCommoditiesByCategory
    @Test
    public void test_get_commodities_by_category_correctly() {
        Response response = baloot.getCommoditiesByCategory("Phone");
        assertTrue(response.isSuccess());
        assertEquals("[{\"id\":3,\"name\":\"iPhone 5s\",\"providerId\":2,\"price\":1000.0,\"categories\":[\"Phone\",\"Technology\"],\"rating\":9.5}]",
                response.getData().get("commoditiesListByCategory").toString());
    }

    @Test
    public void test_get_commodities_by_non_existing_category() {
        Response response = baloot.getCommoditiesByCategory("Art");
        assertTrue(response.isSuccess());
        assertEquals("", response.getData().get("commoditiesListByCategory").toString());
    }
}
