package Presentation;

import Presentation.Server.InterfaceServer;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import org.junit.*;
import Database.Database;

import static org.junit.Assert.*;


public class InterfaceTest {
    private static InterfaceServer interfaceServer;
    private String validUsername;
    private Integer validCommodityId;

    @BeforeClass
    public static void preClass(){
        final String USERS_URI = "http://5.253.25.110:5000/api/users";
        final String COMMODITIES_URI = "http://5.253.25.110:5000/api/commodities";
        final String PROVIDERS_URI = "http://5.253.25.110:5000/api/providers";
        final String COMMENTS_URI = "http://5.253.25.110:5000/api/comments";
        final int PORT = 5000;
        interfaceServer = new InterfaceServer();
        interfaceServer.start(USERS_URI, COMMODITIES_URI, PROVIDERS_URI, COMMENTS_URI, PORT);
    }

    @Before
    public void setUp(){
        validUsername = Database.getInstance().getUsers().get(0).getUsername();
        validCommodityId = Database.getInstance().getCommodities().get(0).getId();
    }

    @After
    public void  tearDown(){
        validUsername = "";
        validCommodityId = -1;
    }

    @AfterClass
    public static void postClass() {
        interfaceServer = null;
    }

    @Test
    public void testUpdateRatingCorrectly(){
        HttpResponse<String> response = Unirest.get("http://localhost:5000/rateCommodity/" + validUsername +"/" + validCommodityId + "/1").asString();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testRatingOutOfRangeOneToTen() {
        HttpResponse<String> response = Unirest.get("http://localhost:5000/rateCommodity/" + validUsername +"/" + validCommodityId + "/11").asString();
        assertEquals(403, response.getStatus());
    }

    @Test
    public void testNonExistingUsernameForRating() {
        HttpResponse<String> response = Unirest.get("http://localhost:5000/rateCommodity/p@ria/"+ validCommodityId + "/1").asString();
        assertEquals(403, response.getStatus());
    }

    @Test
    public void testNonExistingCommodityForRating() {
        HttpResponse<String> response = Unirest.get("http://localhost:5000/rateCommodity/" + validUsername +"/-1/1").asString();
        assertEquals(403, response.getStatus());
    }

    @Test
    public void testSearchCommoditiesByValidPriceInterval(){
        HttpResponse<String> response = Unirest.get("http://localhost:5000/commodities/search/5000/15000").asString();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testSearchCommoditiesByInvalidPriceInterval(){
        HttpResponse<String> response = Unirest.get("http://localhost:5000/commodities/search/1000/10").asString();
        assertEquals(403, response.getStatus());
    }
}