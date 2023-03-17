package Service;

import Database.Database;
import Domain.Comment;
import Domain.Commodity;
import Domain.Provider;
import Domain.User;
import Service.Exceptions.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Baloot {
    public User findUserByUsername(String username) throws UserNotFound {
        for (User user : Database.getInstance().getUsers()){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        throw new UserNotFound();
    }

    public void addUser(User newUser) throws InvalidUsername {
        try{
            findUserByUsername(newUser.getUsername()).update(newUser);
        } catch (UserNotFound userNotFoundError){
             if(!newUser.getUsername().matches("\\w+"))
                 throw new InvalidUsername();
             Database.getInstance().addUser(newUser);
        }
    }

     public void addProvider(Provider newProvider) {
         Database.getInstance().addProvider(newProvider);
     }

     public Provider findProviderById(Integer providerId) throws ProviderNotFound {
        for (Provider provider : Database.getInstance().getProviders()){
            if(provider.isEqual(providerId)){
                return provider;
            }
        }
        throw new ProviderNotFound();
     }

    public Commodity findCommodityById(Integer commodityId) throws CommodityNotFound {
        for (Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.isEqual(commodityId)){
                return commodity;
            }
        }
        throw new CommodityNotFound();
    }

    public List<Commodity> findCommoditiesByCategory(String category){
        List<Commodity> commoditiesInCategory = new ArrayList<>();
        for (Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.isInCategory(category)){
                commoditiesInCategory.add(commodity);
            }
        }
        return commoditiesInCategory;
    }

     public void addCommodity(Commodity newCommodity) throws ProviderNotFound {
        findProviderById(newCommodity.getProviderId());
        Database.getInstance().addCommodity(newCommodity);
     }

     public void addToBuyList(String username, Integer commodityId) throws CommodityNotFound, UserNotFound, CommodityOutOfStock, CommodityAlreadyExistsInBuyList {
        findCommodityById(commodityId).checkInStock();
        findUserByUsername(username).addToBuyList(findCommodityById(commodityId));
     }

    public void finalizePayment(String userId) throws UserNotFound, CommodityOutOfStock, NotEnoughCredit {
        User user = findUserByUsername(userId);
        List<Commodity> buyListCommodities = user.getBuyList();
        for (Commodity commodity : buyListCommodities){
            commodity.checkInStock();
        }
        for (Commodity commodity : buyListCommodities){
            user.reduceCredit(commodity.getPrice());
            user.addToPurchasedList(commodity);
            commodity.reduceInStock();
        }
        user.resetBuyList();
    }

     public void removeFromBuyList(String username, Integer commodityId) throws UserNotFound, CommodityNotFound, CommodityNotInBuyList {
            findUserByUsername(username).removeFromBuyList(findCommodityById(commodityId));
     }

     public List<Comment> getCommodityComments(String commodityId){
        List<Comment> comments = new ArrayList<>();
        for (Comment comment : Database.getInstance().getComments()){
            if (String.valueOf(comment.getCommodityId()).equals(commodityId)){
                comments.add(comment);
            }
        }
        return comments;
     }

     public String getUsernameByEmail(String email) throws UserNotFound {
        for (User user : Database.getInstance().getUsers()){
            if (user.getEmail().equals(email)){
                return user.getUsername();
            }
        }
        throw new UserNotFound();
     }

    public Comment findCommentById(Integer commentId) throws CommentNotFound {
        for (Comment comment : Database.getInstance().getComments()){
            if(comment.getId().equals(commentId)){
                return comment;
            }
        }
        throw new CommentNotFound();
    }

    public void rateCommodity(String username, Integer commodityId, Integer score) throws UserNotFound, CommodityNotFound, RatingOutOfRange {
        findCommodityById(commodityId).addUserRating(findUserByUsername(username).getUsername(), score);
    }

    public List<Commodity> findCommoditiesByProvider(Integer proveiderId){
        List<Commodity> commodities = new ArrayList<>();
        for(Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.getProviderId().equals(proveiderId))
                commodities.add(commodity);
        }
        return commodities;
    }

    private boolean isPriceIntervalValid(float startPrice, float endPrice){
        return startPrice >= 0 && endPrice >=0 && startPrice <= endPrice;
    }

    public List<Commodity> searchCommoditiesByPrice(float startPrice, float endPrice) throws InvalidPriceInterval {
        if(!isPriceIntervalValid(startPrice, endPrice))
            throw new InvalidPriceInterval();
        List<Commodity> commodities = new ArrayList<>();
        for(Commodity commodity : Database.getInstance().getCommodities()){
            if(commodity.getPrice() >= startPrice && commodity.getPrice() <= endPrice)
                commodities.add(commodity);
        }
        return commodities;
    }

    public void addUserCredit(String username, float credit) throws UserNotFound, NegativeCredit {
        findUserByUsername(username).addCredit(credit);
    }

    private boolean isVoteValid(int vote){
        return vote == 1 || vote == 0 || vote == -1;
    }

    public void voteComment(Integer commentId, String username, int vote) throws InvalidCommentVote, CommentNotFound, UserNotFound{
        if(!isVoteValid(vote))
            throw new InvalidCommentVote();
        findCommentById(commentId).addVote(findUserByUsername(username).getUsername(), vote);
    }

    public double getAverageRating(String providerId){
        List<Commodity> providedCommodities = findCommoditiesByProvider(Integer.valueOf(providerId));
        double averageRating = providedCommodities.stream()
                .mapToDouble(commodity -> commodity.getRating())
                .average()
                .orElse(Double.NaN);
        return averageRating;
    }

    public void printData(){
        System.out.println("Users:");
        for(User user : Database.getInstance().getUsers()){
            System.out.println(user.getUsername());
        }
        System.out.println("Providers:");
        for(Provider provider : Database.getInstance().getProviders()){
            System.out.println(provider.getId());
        }
        System.out.println("Commodities:");
        for(Commodity commodity : Database.getInstance().getCommodities()){
            System.out.println(commodity.getId());
        }
        System.out.println("Comments:");
        for(Comment comment : Database.getInstance().getComments()){
            System.out.println(comment.getText());
        }
    }
}

