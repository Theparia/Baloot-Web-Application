package Service;

import Domain.Commodity;
import Domain.User;
import Presentation.Server.*;
import Service.Exceptions.*;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class BalootTest {
    private Baloot baloot;
    final String USERS_URI = "http://5.253.25.110:5000/api/users";
    final String COMMODITIES_URI = "http://5.253.25.110:5000/api/commodities";
    final String PROVIDERS_URI = "http://5.253.25.110:5000/api/providers";
    final String COMMENTS_URI = "http://5.253.25.110:5000/api/comments";

    @BeforeClass
    public static void preClass() {
    }

    @Before
    public void setUp() throws Exception {
        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.importBalootDatabase(USERS_URI, COMMODITIES_URI, PROVIDERS_URI, COMMENTS_URI);
        baloot = new Baloot();
    }

    @Test
    public void Given_EverythingCorrectly_Then_RateIsSuccessful() throws UserNotFound, RatingOutOfRange, CommodityNotFound {
        float prevRating = baloot.findCommodityById(1).getRating();
        baloot.rateCommodity("amir", 1, 5);
        float currRating = baloot.findCommodityById(1).getRating();
        float correctNewRating = (prevRating + 5)/2;
        assertEquals(String.valueOf(correctNewRating), String.valueOf(currRating));
    }

    @Test
    public void Given_EverythingCorrectly_When_UserRateTwice_Then_RateIsSuccessfulAndReplaced() throws UserNotFound, RatingOutOfRange, CommodityNotFound {
        float prevRating = baloot.findCommodityById(1).getRating();
        baloot.rateCommodity("amir", 1, 5);
        baloot.rateCommodity("amir", 1, 7);
        float currRating = baloot.findCommodityById(1).getRating();
        float correctNewRating = (prevRating + 7)/2;
        assertEquals(String.valueOf(correctNewRating), String.valueOf(currRating));
    }

    @Test(expected = RatingOutOfRange.class)
    public void Given_RatingOutOfRange_Then_RateWillFail() throws UserNotFound, CommodityNotFound, RatingOutOfRange {
        baloot.rateCommodity("amir", 1, 11);
    }

    @Test(expected = UserNotFound.class)
    public void Given_MissedUser_Then_RateWillFail() throws UserNotFound, CommodityNotFound, RatingOutOfRange {
        baloot.rateCommodity("parnian", 1, 8);
    }

    @Test(expected = CommodityNotFound.class)
    public void Given_MissedCommodity_Then_RateWillFail() throws UserNotFound, CommodityNotFound, RatingOutOfRange {
        baloot.rateCommodity("amir", 100, 8);
    }

    @Test
    public void Given_EverythingCorrectly_Then_AddToBuyListIsSuccessful() throws UserNotFound, CommodityNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        User user = baloot.findUserByUsername("amir");
        baloot.addToBuyList("amir", 1);
        assertTrue(user.getBuyList().contains(baloot.findCommodityById(1)));
    }

    @Test(expected = CommodityAlreadyExistsInBuyList.class)
    public void Given_DuplicateCommodity_Then_AddToBuyListWillFail() throws UserNotFound, CommodityNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        baloot.addToBuyList("amir", 17);
        baloot.addToBuyList("amir", 17);
    }

    @Test(expected = UserNotFound.class)
    public void Given_MissedUser_Then_AddToBuyListWillFail() throws UserNotFound, CommodityNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        baloot.addToBuyList("parnian", 17);
    }

    @Test(expected = CommodityNotFound.class)
    public void Given_MissedCommodity_Then_AddToBuyListWillFail() throws UserNotFound, CommodityNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        baloot.addToBuyList("amir", 100);
    }

    @Test(expected = CommodityOutOfStock.class)
    public void Given_CommodityOutOfStock_Then_AddToBuyListWillFail() throws UserNotFound, CommodityNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList, NotEnoughCredit {
        baloot.addToBuyList("mahdi", 22);
        baloot.finalizePayment("mahdi");
        baloot.addToBuyList("mahdi", 22);
        baloot.finalizePayment("mahdi");
        baloot.addToBuyList("mahdi", 22);
        baloot.finalizePayment("mahdi");
        baloot.addToBuyList("mahdi", 22);
        baloot.finalizePayment("mahdi");
        baloot.addToBuyList("mahdi", 22);
        baloot.finalizePayment("mahdi");
        baloot.addToBuyList("mahdi", 22);
        baloot.finalizePayment("mahdi");
    }

    @Test
    public void Given_EverythingCorrectly_Then_SearchCommodityByPriceRangeIsSuccessful() throws InvalidPriceInterval, CommodityNotFound {
        List<Commodity> filteredCommodities = baloot.searchCommoditiesByPrice(10000, 11000);
        assertTrue(filteredCommodities.contains(baloot.findCommodityById(1)));
    }

    @Test(expected = InvalidPriceInterval.class)
    public void Given_StartIntervalHigherThanEndInterval_Then_SearchCommodityByPriceRangeWillFail() throws InvalidPriceInterval, CommodityNotFound {
        baloot.searchCommoditiesByPrice(11000, 10000);
    }

    @Test(expected = InvalidPriceInterval.class)
    public void Given_EndIntervalLessThanZero_Then_SearchCommodityByPriceRangeWillFail() throws InvalidPriceInterval, CommodityNotFound {
        baloot.searchCommoditiesByPrice(10000, -10);
    }

    @After
    public void tearDown() {
        baloot = null;
    }

    @AfterClass
    public static void postClass() {
    }
}
